package com.egemen.mapsdeneme;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BackLocationService extends BroadcastReceiver {
    public static final String ACTION_PROCESS_UPDATE = "com.example.myapplication.UPDATE_LOCATION";
    static Location newLoc;
    private static final int minDistance = 20;
    private ApiManager apm = new ApiManager();

    @Override
    public void onReceive(Context context, Intent intent) {
        final NotificationManagementSystem nms = new NotificationManagementSystem(context);
        Log.d("Receiver","Sinyal alındı");
        Log.d("Receiver","Sinyal alındı");
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_PROCESS_UPDATE.equals(action)) {
                LocationResult result = LocationResult.extractResult(intent);
                if (result != null) {
                    Location location = result.getLastLocation();

                    try {
                        if(newLoc == null)
                            newLoc = location;
                        System.out.println("****" + location.getLatitude() + "-" + location.getLongitude());
                        System.out.println(newLoc.distanceTo(location));
                        if(newLoc.distanceTo(location) > minDistance){
                            //postMessage(context, "Konum", location.getLatitude(), location.getLongitude());
                            //getDistMessages(context, location.getLatitude(), location.getLongitude());

                            apm.request(apm.api.getDistMassage(location.getLatitude(), location.getLongitude()), new ResponseHandler() {
                                @Override
                                public void onSucces(ResponseBody data, int statusCode, String message) {
                                    if(statusCode ==200){
                                        if(data.getMessages().size() > 0){
                                            Log.d("Receiver",data.getMessages().size()+"tane marker var");
                                            nms.mostra(1);
                                        }
                                    }
                                }
                                @Override
                                public void onFail(Call<ResponseBody> call, Throwable t) {}
                            });
                            Toast.makeText(context,"5 metre hareket ettiniz.",Toast.LENGTH_LONG).show();
                            newLoc = location;
                        }

                    } catch (Exception e) {
                        Toast.makeText(context, "Bir hata meydana geldi!", Toast.LENGTH_SHORT).show();
                        System.out.println(":::" + e.getMessage());
                    }
                }
            }
        }

    }

    public JsonObject createJSON(Context context, String mesaj, double lat, double lon) {
        JsonObject object = new JsonObject();
        String deviceName = Settings.Secure.getString(context.getContentResolver(), "bluetooth_name");
        object.addProperty("deviceName", deviceName);
        object.addProperty("message", mesaj);
        JsonObject object1 = new JsonObject();
        object1.addProperty("lat", lat);
        object1.addProperty("lon", lon);
        object.add("locationType", object1);
        return object;
    }

    /*public void postMessage(final Context context, String mes, double lat, double lon) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(AppParameters.REST_API_URL).addConverterFactory(GsonConverterFactory.create()).build();
        Api apis = retrofit.create(Api.class);
        Call<JsonObject> call = apis.postMassage(createJSON(context, mes, lat, lon));

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body() != null) {
                    Log.d("snow::::", response.body().toString());
                    System.out.println("Mesaj Gönderildi");
                    Toast.makeText(context, "Mesaj Gönderildi", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Response \"null\" döndü", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                System.out.println("Mesaj gönderilemedi" + " " + t.getMessage());
                Toast.makeText(context, "Mesaj Gönderilemedi!", Toast.LENGTH_SHORT).show();
            }
        });
    }*/

    public void getDistMessages(final Context context,double lat, double lon, final ResponseHandler rp) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(AppParameters.REST_API_URL).addConverterFactory(GsonConverterFactory.create()).build();
        Api apis = retrofit.create(Api.class);

        JsonObject resObj = new JsonObject();
        JsonObject object1 = new JsonObject();
        object1.addProperty("lat", lat);
        object1.addProperty("lon", lon);
        resObj.add("location", object1);
        Call<JsonObject[]> call = apis.postDistMassage(resObj);
        call.enqueue(new Callback<JsonObject[]>() {
            @Override
            public void onResponse(Call<JsonObject[]> call, Response<JsonObject[]> response) {
                //Log.d("snow::::");
                if (!response.isSuccessful() || response.body() == null || response.body().length == 0) {
                    System.out.println("Message not found -getDistMessages()");
                } else {
                    Log.d("message",response.body().length+" tane marker bulundu -getDistMassage()");
                    /////////////////////////NOTIFICATION////////////////////////
                    Intent resultIntent = new Intent(context, MapsActivity.class);
                    TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
                    stackBuilder.addNextIntentWithParentStack(resultIntent);
                    PendingIntent resultPendingIntent =
                            stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "1")
                            .setSmallIcon(R.mipmap.ic_launcher_loca)
                            .setContentTitle("Bulunduğunuz konumda yeni mesajlar var")
                            .setContentText("Deneme içeriği")
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                            .setContentIntent(resultPendingIntent)
                            .setAutoCancel(true);

                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                    notificationManager.notify(1, builder.build());
                    //////////////////////////////////////////////////////////////
                }
            }

            @Override
            public void onFailure(Call<JsonObject[]> call, Throwable t) {
                System.out.println("İstek gönderilemedi -getDistMassage()" + " " + t.getMessage());
                //Toast.makeText(getApplicationContext(MapsActivity), "Sunucu cevap vermiyor!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
