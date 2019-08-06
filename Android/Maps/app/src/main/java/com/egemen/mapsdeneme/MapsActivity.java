package com.egemen.mapsdeneme;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.view.inputmethod.InputMethodManager;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.io.Serializable;

import retrofit2.Call;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, Serializable,
        GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener {

    private GoogleMap mMap;
    LocationManager locationManager;
    LocationListener locationListener;
    EditText editText;
    String message;
    Button button,button_iptal;
    MarkerManager markerManager;
    private Marker marker;
    private static final int REQUEST_ACCESS_FINE_LOCATION = 1;
    private static String[] PERMISSIONS_MAPS = {Manifest.permission.ACCESS_FINE_LOCATION};
    NotificationManagerCompat notManager;
    NotificationCompat.Builder notBuilder;
    MapsActivity instance;



    ///////////////////////////////////////////////////////////////////////////////////////////
    /*
    Intent resultIntent = new Intent(this, MapsActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "1")
                .setSmallIcon(R.mipmap.ic_launcher_loca)
                .setContentTitle("Bildirim")
                .setContentText("Deneme içeriği")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(resultPendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
     */
    // Local
    //String APIUrl = "http://192.168.16.232:8082/";


    /*
    public void getAllMessages() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Api api = retrofit.create(Api.class);

        Call<Server> call = api.cagir(200);
        call.enqueue(new Callback<Server>() {
            @Override
            public void onResponse(Call<Server> call, Response<Server> response) {
                if (response.body() == null) {
                    System.out.println("Message Not Found -getAllMessages()");
                    return;
                }
                dat = response.body().getData();
                List<Server> results = new ArrayList<>();
                for (int i = 0; i < dat.size(); i++) {
                    if (dat.get(i) != null) {
                        results.add(new Server(dat.get(i).message, dat.get(i).locationType));
                    }
                }
                for (Server it : results) {
                    LatLng messageLoc = new LatLng(it.getLocation().getLat(), it.getLocation().getLon());
                    mMap.addMarker(new MarkerOptions().title(it.getMessage()).position(messageLoc));
                }
            }

            @Override
            public void onFailure(Call<Server> call, Throwable t) {
                Log.d("---------:", t.getMessage());

            }
        });
    }
    */

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.app_name);
            String description = getString(R.string.common_google_play_services_install_title);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("1", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        marker = null;
        createNotificationChannel();
        Intent resultIntent = new Intent(this, MapsActivity.class);
/*
        ///////////////////////////////////////////////////*
        User user = new User("gencaysyn", "Gençay", "Sayın", "123456", "gencaysyn@gmail.com");
        ///////////////////////////////////////////////////
        Gson gson = new Gson();
        String jsonStr = gson.toJson(user);

        final ApiManager apm = new ApiManager();
        System.out.println("///////////" + jsonStr);
        apm.request(apm.api.postUser(user), new ResponseHandler() {
            @Override
            public void onSucces(ApiHandler data, int statusCode, String message) {
                if(statusCode == 200){
                    Log.d("Deneme","Data:"+data.getUser().toString());
                }
            }

            @Override
            public void onFail(Call<ApiHandler> call, Throwable t) {

            }


        });
        /////////////////////////////////////////////////////////
*/
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        notBuilder = new NotificationCompat.Builder(this, "1")
                .setSmallIcon(R.mipmap.ic_launcher_loca)
                .setContentTitle("Bulunduğun konumda yeni mesajlar var!")
                .setContentText("Mesajları görmek için tıkla")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(resultPendingIntent)
                .setAutoCancel(true);

        notManager = NotificationManagerCompat.from(this);
        //Cihaz adını alma kodu
        //Settings.Secure.getString(getContentResolver())
        //notManager.notify(1, notBuilder.build());
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null)
            mapFragment.getMapAsync(this);
        editText = findViewById(R.id.editText);
        button = findViewById(R.id.button);
        button_iptal = findViewById(R.id.button3);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message = editText.getText().toString();
                if (marker == null || message.length() == 0)
                    Toast.makeText(MapsActivity.this, "Marker ya da Mesaj Eklenmedi!", Toast.LENGTH_SHORT).show();
                else {
                    String deviceName = Settings.Secure.getString(getContentResolver(), "bluetooth_name");
                    markerManager.postMessage(new MessageType(deviceName, message, new LocationType(marker.getPosition().latitude, marker.getPosition().longitude)));
                    marker.setTitle(deviceName);
                    marker.setSnippet(message);
                    editText.setText("");
                    Toast.makeText(getApplicationContext(), "Mesaj Oluşturuldu.", Toast.LENGTH_SHORT).show();
                    closeKeyboard();
                    //getDistMessages(marker.getPosition().latitude, marker.getPosition().longitude);
                }
                switchLayoutProperty(1);
            }
        });



        button_iptal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.setText("");
                //marker=null;
                marker.remove();
                closeKeyboard();
                switchLayoutProperty(1);
            }
        });

        instance = this;




    }


    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        markerManager = new MarkerManager(mMap,getApplicationContext());

        if (checkPermission()) {
            try {
                mMap.setMyLocationEnabled(true);
                mMap.setOnMyLocationButtonClickListener(this);
                mMap.setOnMyLocationClickListener(this);

                /*LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                Criteria criteria = new Criteria();
                locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
                Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
                if (location != null) {
                    System.out.println("Merak " + location.getLatitude() + location.getLongitude());
                    getDistMessages(location.getLatitude(), location.getLongitude());
                }*/

            } catch (Exception e) {
                System.out.println("Mavi nokta hatası" + e.getMessage());
            }
        } else {
            button.setEnabled(false);
            System.out.println("reddedildi 2");
            //verifyMapsPermissions(this);



        }

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {
                //LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));
                markerManager.getDistMessages(location.getLatitude(),location.getLongitude());
                System.out.println("Lokasyon değişti");

                //Toast.makeText(getApplicationContext(), "lat:"+location.getLatitude()+"lon:"+location.getLongitude(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if (Build.VERSION.SDK_INT >= 24) {
            if (!checkPermission()) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                button.setEnabled(true);
            }
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }

        mMap.setOnMapClickListener(this);
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (grantResults.length > 0) {
            if (requestCode == 1) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_ACCESS_FINE_LOCATION) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                if (checkPermission()) {
                    mMap.setMyLocationEnabled(true);
                }
            }
        }
    }

    private boolean checkPermission() {
        // Ask for permission if it wasn't granted yet
        return (ContextCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED);
    }

    public static void verifyMapsPermissions(FragmentActivity activity) {
        // Check if we have write permission

        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_MAPS,
                    REQUEST_ACCESS_FINE_LOCATION
            );
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if (marker != null)
            marker.remove();

        MarkerOptions newMarker = new MarkerOptions()
                .position(new LatLng(latLng.latitude, latLng.longitude))
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher_flag_location_red));
        this.marker = mMap.addMarker(newMarker);
        button.setEnabled(true);
        System.out.println(latLng.latitude + "---" + latLng.longitude);

        switchLayoutProperty(0);
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(getApplicationContext(), "Güncellendi\n" + "Lon:" + location.getLatitude() + " Lat:" + location.getLongitude(), Toast.LENGTH_SHORT).show();
        System.out.println("Lat:" + location.getLatitude() + " Lon:" + location.getLongitude());
        markerManager.getDistMessages(location.getLatitude(), location.getLongitude());

    }

    public void switchLayoutProperty(int value) {
        //  RelativeLayout relativelayout1;
        //relativelayout1=(RelativeLayout)findViewById(R.id.relativelayout1);

        RelativeLayout relativelayout1 = findViewById(R.id.relativelayout1);

        ViewGroup.LayoutParams params = relativelayout1.getLayoutParams();
        android.view.Display display = ((android.view.WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        if (value == 0) {
            params.height = (display.getHeight() * 3) / 4;
            relativelayout1.setLayoutParams(params);
        } else {
            params.height = display.getHeight();
            relativelayout1.setLayoutParams(params);
        }
    }
    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
