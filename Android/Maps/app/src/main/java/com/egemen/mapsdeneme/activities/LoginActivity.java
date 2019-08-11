package com.egemen.mapsdeneme.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.egemen.mapsdeneme.config.AppParameters;
import com.egemen.mapsdeneme.interfaces.ResponseHandler;
import com.egemen.mapsdeneme.model.ResponseBody;
import com.egemen.mapsdeneme.model.UserType;
import com.egemen.mapsdeneme.network.ApiManager;
import com.egemen.mapsdeneme.utils.BackLocationService;
import com.egemen.mapsdeneme.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import retrofit2.Call;

public class LoginActivity extends AppCompatActivity {
    Button loginButton;
    EditText username, password;
    LocationRequest locationRequest;
    FusedLocationProviderClient fusedLocationProviderClient;
    private int PERMISSION_CODE = 1;
    ApiManager apm;

    SharedPreferences pref;


    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        loginButton = findViewById(R.string.giris);

        username = findViewById(R.string.loginUsername);

        password = findViewById(R.string.loginPassword);
        apm = new ApiManager();
        pref = getSharedPreferences("user_details",MODE_PRIVATE);

        Intent intent = new Intent(LoginActivity.this, MapsActivity.class);
        if(pref.contains("username") && pref.contains("password")){
            startActivity(intent);
        }
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            updateLocation();
                        }
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(LoginActivity.this, "İzin vermelisiniz", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                }).check();



        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserType usr = new UserType(username.getText().toString(),password.getText().toString());

                apm.request(apm.api.postLogin(usr), new ResponseHandler() {
                    @Override
                    public void onSuccess(ResponseBody data, int statusCode, String message) {
                        if(statusCode == 400){
                            Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(getApplicationContext(),"Giriş yapıldı.",Toast.LENGTH_SHORT).show();
                            AppParameters.appUser = data.getUserType();
                            login();
                        }
                    }
                    @Override
                    public void onFail(Call<ResponseBody> call) {
                        Toast.makeText(getApplicationContext(),"Sunucu yanıt vermiyor",Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

    }
    public void onCheckboxClicked(View view) {

        boolean checked = ((CheckBox) view).isChecked();
        if(checked){
            String rememberUsername=username.getText().toString();
            String rememberPassword =password.getText().toString();
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("username",rememberUsername);
            editor.putString("password",rememberPassword);
            editor.apply();
        }
    }
    private void login(){
        if (ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermission();
        } else {

            SharedPreferences.Editor editor = pref.edit();
            Intent intent = new Intent(LoginActivity.this, MapsActivity.class);
            Gson gson =new Gson();
            String json=gson.toJson(AppParameters.appUser);

            editor.putString("user",json);
            editor.apply();

            startActivity(intent);
        }
    }
    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_CODE);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_CODE);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void updateLocation() {
        buildLocationRequest();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, getPendingIntend());
    }
    private PendingIntent getPendingIntend() {
        Intent intent = new Intent(this, BackLocationService.class);
        intent.setAction(BackLocationService.ACTION_PROCESS_UPDATE);
        return PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void buildLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setSmallestDisplacement(10f);
    }

    @Override
    public void onBackPressed() {
        if (AppParameters.doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        AppParameters.doubleBackToExitPressedOnce = true;
        Toast.makeText(this,R.string.exit, Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                AppParameters.doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    public void signUp(View view){
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }
}
