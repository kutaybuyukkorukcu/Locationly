package com.egemen.mapsdeneme;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.JsonObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, Serializable,
        GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener {

    private GoogleMap mMap;
    LocationManager locationManager;
    LocationListener locationListener;
    EditText editText;
    String message;
    Button button;
    private Marker marker;
    private Map<String, MarkerOptions> markerMap = new HashMap<>();
    private static final int REQUEST_ACCESS_FINE_LOCATION = 1;
    private static String[] PERMISSIONS_MAPS = {
            Manifest.permission.ACCESS_FINE_LOCATION};

    String APIUrl = "http://52.143.175.25:8082/";

    public JsonObject createJSON(String mesaj, double lat, double lon) {
        JsonObject object = new JsonObject();

        object.addProperty("message", mesaj);
        JsonObject object1 = new JsonObject();
        object1.addProperty("lat", lat);
        object1.addProperty("lon", lon);

        object.add("location", object1);


        return object;
    }

    public void postMessage(String mes, double lat, double lon) {

        Retrofit retrofit = new Retrofit.Builder().baseUrl(APIUrl).addConverterFactory(GsonConverterFactory.create()).build();
        Api apis = retrofit.create(Api.class);

        Call<JsonObject> call = apis.at(createJSON(mes, lat, lon));

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    Log.d("snow::::", response.body().toString());
                    System.out.println("Mesaj Gönderildi");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                System.out.println("Mesaj gönderilemedi" + " " + t.getMessage());
            }
        });
    }

    public void putMarkers() {
        //Set markerSet = markerMap.entrySet();
        Iterator it = markerMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry me = (Map.Entry) it.next();
            mMap.addMarker((MarkerOptions) me.getValue());
        }
    }

    public void getDistMessages(double lat, double lon) {

        Retrofit retrofit = new Retrofit.Builder().baseUrl(APIUrl).addConverterFactory(GsonConverterFactory.create()).build();
        Api apis = retrofit.create(Api.class);


        JsonObject resObj = new JsonObject();
        JsonObject object1 = new JsonObject();
        object1.addProperty("lat", lat);
        object1.addProperty("lon", lon);
        resObj.add("location", object1);
        Call<JsonObject[]> call = apis.dist(resObj);

        call.enqueue(new Callback<JsonObject[]>() {
            @Override
            public void onResponse(Call<JsonObject[]> call, Response<JsonObject[]> response) {
                //Log.d("snow::::");
                if (response.body() == null) {
                    System.out.println("Message not found -getDistMessages()");
                } else {
                    //System.out.println("Mesaj Gönderildi" + response.body()[0].get("location").getAsJsonObject().get("lat"));
                    //System.out.println("ID::::::" + response.body()[0].get("_id").toString());
                    markerMap.clear();
                    for (JsonObject it : response.body()) {
                        LatLng messageLoc = new LatLng(Double.valueOf(it.get("location").getAsJsonObject().get("lat").toString()), Double.valueOf(it.get("location").getAsJsonObject().get("lon").toString()));
                        MarkerOptions m = new MarkerOptions().title(it.get("message").toString()).position(messageLoc);
                        try {
                            markerMap.put(it.get("_id").toString(), m);
                        } catch (Exception e) {
                            System.out.println("Zaten var" + e.getMessage());
                        }
                    }
                    putMarkers();
                }

            }

            @Override
            public void onFailure(Call<JsonObject[]> call, Throwable t) {
                System.out.println("Mesaj gönderilemedi" + " " + t.getMessage());
            }
        });
    }

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
                List<Datum> dat;
                if (response.body() == null) {
                    System.out.println("Message Not Found -getAllMessages()");
                    return;
                }
                dat = response.body().getData();
                List<Server> results = new ArrayList<>();
                for (int i = 0; i < dat.size(); i++) {
                    if (dat.get(i) != null) {
                        results.add(new Server(dat.get(i).message, dat.get(i).location));
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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        marker = null;


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        editText = (EditText) findViewById(R.id.editText);
        button = (Button) findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message = editText.getText().toString();
                if (marker == null || message.length() == 0)
                    Toast.makeText(MapsActivity.this, "Marker ya da Mesaj Eklenmedi!", Toast.LENGTH_SHORT).show();
                else {

                    postMessage(message, marker.getPosition().latitude, marker.getPosition().longitude);
                    marker.setTitle(message);
                    editText.setText("");
                    Toast.makeText(getApplicationContext(), "Mesaj Oluşturuldu.", Toast.LENGTH_SHORT).show();
                    //getDistMessages(marker.getPosition().latitude, marker.getPosition().longitude);
                }
            }
        });

        //getAllMessages();

        //getDistMessages(40.9745427, 29.1019080);

        //postMessage("ali",25,35);
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, @DrawableRes int vectorDrawableResourceId) {
        Drawable background = ContextCompat.getDrawable(context, vectorDrawableResourceId);
        background.setBounds(0, 0, background.getIntrinsicWidth(), background.getIntrinsicHeight());
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorDrawableResourceId);
        vectorDrawable.setBounds(40, 20, vectorDrawable.getIntrinsicWidth() + 40, vectorDrawable.getIntrinsicHeight() + 20);
        Bitmap bitmap = Bitmap.createBitmap(background.getIntrinsicWidth(), background.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        background.draw(canvas);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private BitmapDescriptor vectorToBitmap(@DrawableRes int id, @ColorInt int color) {
        Drawable vectorDrawable = ResourcesCompat.getDrawable(getResources(), id, null);
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        DrawableCompat.setTint(vectorDrawable, color);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {

        mMap = googleMap;
        //final BitmapDescriptor icon = bitmapDescriptorFromVector(this, R.drawable.flag_icon);
        /*
        if ((ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) &&
                (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            return;
        }*/

        if (checkPermission()) {
            try {
                mMap.setMyLocationEnabled(true);
                mMap.setOnMyLocationButtonClickListener(this);
                mMap.setOnMyLocationClickListener(this);
                //LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                Criteria criteria = new Criteria();
                locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
                Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
                if (location != null) {
                    System.out.println("Merak " + location.getLatitude() + location.getLongitude());
                    getDistMessages(location.getLatitude(), location.getLongitude());
                }

            } catch (Exception e) {
                System.out.println("Mavi nokta hatası" + e.getMessage());
            }
        } else {
            verifyMapsPermissions(this);

        }

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {
                //mMap.clear();
                //LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));
                getDistMessages(location.getLatitude(), location.getLongitude());
                System.out.println("Lokasyon değişti");
                Toast.makeText(getApplicationContext(),"Lokasyon değişti",Toast.LENGTH_LONG).show();
                System.out.println("-----------" + markerMap.size());
//                for (String name: markerMap.keySet()){
//                    String key = name;
//                    String value = markerMap.get(name).toString();
//                    System.out.println(key + "/////// " + value);
//                }
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

        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

                //Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                //System.out.println("lastLocation: " + lastLocation);
                //LatLng userLastLocation = new LatLng(lastLocation.getLatitude(),lastLocation.getLongitude());
                //mMap.addMarker(new MarkerOptions().title("Your Location").position(userLastLocation));
                //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLastLocation,15));
            }
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

            //Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            //System.out.println("lastLocation: " + lastLocation);
            //LatLng userLastLocation = new LatLng(lastLocation.getLatitude(),lastLocation.getLongitude());
            //mMap.addMarker(new MarkerOptions().title("Your Location").position(userLastLocation));
            //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLastLocation,15));
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
        switch (requestCode) {
            case REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted
                    if (checkPermission()) {
                        mMap.setMyLocationEnabled(true);
                    }
                }
                break;
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
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(latLng.latitude, latLng.longitude))
                .icon(vectorToBitmap(R.drawable.flag_icon, Color.parseColor("#A4C639")))
                .title("Alice Springs"));
        /*MarkerOptions newMarker = new MarkerOptions()
                .position(new LatLng(latLng.latitude, latLng.longitude));

       //this.marker = mMap.addMarker(newMarker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        this.marker = mMap.addMarker(newMarker.icon(icon);
        System.out.println(latLng.latitude + "---" + latLng.longitude);*/

    }
    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        getDistMessages(location.getAltitude(),location.getLongitude());
    }
}
