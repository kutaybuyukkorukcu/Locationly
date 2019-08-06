package com.egemen.mapsdeneme;

import android.content.Context;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MarkerManager extends FragmentActivity {
    private Map<String, MarkerOptions> responseMap = new HashMap<>();
    private Map<String, Marker> showMap = new HashMap<>();
    private GoogleMap mMap;
    private ApiManager apm = new ApiManager();
    private Context context;

    public MarkerManager(GoogleMap mMap,Context context) {
        this.mMap = mMap;
        this.context = context;
    }

    public JsonObject createJSON(String deviceName, String mesaj, double lat, double lon) {
        JsonObject object = new JsonObject();
        if(deviceName.equals(""))
            deviceName="anonim";
        object.addProperty("deviceName",deviceName);
        object.addProperty("message", mesaj);
        JsonObject object1 = new JsonObject();
        object1.addProperty("lat", lat);
        object1.addProperty("lon", lon);
        object.add("location", object1);
        return object;
    }

    public void postMessage(MessageType message) {
        apm.request(apm.api.postMassage(message), new ResponseHandler() {
            @Override
            public void onSucces(ResponseBody data, int statusCode, String message) {
                if(statusCode == 200)
                    Toast.makeText(context,"Mesaj başarıyla gönderildi!",Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(context,"Mesaj gönderilirken bir sorun meydana geldi!",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFail(Call<ResponseBody> call, Throwable t) {}
        });
    }


    public void putMarkers() {
        ArrayList<String> keys = new ArrayList<>();
        for (Map.Entry<String, Marker> entry : showMap.entrySet()) {
            String key = entry.getKey();
            if (responseMap.get(key) == null) {
                keys.add(key);
            }
        }
        for (String key : keys) {
            Marker m = showMap.get(key);
            if (m != null)
                m.remove();
            showMap.remove(key);
        }
        for (Map.Entry<String, MarkerOptions> entry : responseMap.entrySet()) {
            if (showMap.get(entry.getKey()) == null) {
                Marker m = mMap.addMarker(entry.getValue());
                showMap.put(entry.getKey(), m);
                //notManager.notify(1, notBuilder.build());
            }
        }
    }
    /*
    public void getDistMessages(double lat, double lon) {
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
                if (response.body() == null) {
                    System.out.println("Message not found -getDistMessages()");
                } else {
                    Log.d("message","Markerlar getirldi -getDistMassage()");
                    responseMap.clear();
                    for (JsonObject it : response.body()) {
                        LatLng messageLoc = new LatLng(Double.valueOf(it.get("location").getAsJsonObject().get("lat").toString()), Double.valueOf(it.get("location").getAsJsonObject().get("lon").toString()));
                        MarkerOptions m = new MarkerOptions()
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher_flag_yellow))
                                .title(it.get("deviceName").toString().replace("\"",""))
                                .snippet(it.get("message").toString().replace("\"",""))
                                .position(messageLoc);
                        responseMap.put(it.get("_id").toString(), m);
                    }
                    putMarkers();
                }

            }

            @Override
            public void onFailure(Call<JsonObject[]> call, Throwable t) {
                System.out.println("İstek gönderilemedi -getDistMassage()" + " " + t.getMessage());
                //Toast.makeText(getApplicationContext(MapsActivity), "Sunucu cevap vermiyor!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    */

    public void getDistMessages(double lat,double lon){
        apm.request(apm.api.getDistMassage(lat,lon), new ResponseHandler() {
            @Override
            public void onSucces(ResponseBody data, int statusCode, String message) {
                if (statusCode == 200) {
                    Toast.makeText(context,"Mesajlar alınırken bir hata meydana geldi!",Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("message","Markerlar getirildi -getDistMassage()");
                    responseMap.clear();
                    for (MessageType it : data.getMessages()) {
                        LatLng messageLoc = new LatLng(it.getLocation().getLat(),it.getLocation().getLon());
                        MarkerOptions m = new MarkerOptions()
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher_flag_yellow))
                                .title(it.getDeviceName())
                                .snippet(it.getText())
                                .position(messageLoc);
                        responseMap.put(it.getId(), m);
                    }
                    putMarkers();
                }
            }

            @Override
            public void onFail(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
}
