package com.scalx.locationly.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import com.scalx.locationly.config.AppParameters;
import com.scalx.locationly.interfaces.ResponseHandler;
import com.scalx.locationly.model.LocationType;
import com.scalx.locationly.model.MessageType;
import com.scalx.locationly.model.ResponseBody;
import com.scalx.locationly.network.ApiManager;
import com.google.android.gms.location.LocationResult;

import java.util.ArrayList;

import retrofit2.Call;

public class BackLocationService extends BroadcastReceiver {
    public static final String ACTION_PROCESS_UPDATE = "com.example.myapplication.UPDATE_LOCATION";
    static Location newLoc;
    private static final int minDistance = 20;
    private ApiManager apm = new ApiManager();
    NotificationManagementSystem nms;
    @Override
    public void onReceive(Context context, Intent intent) {
        nms = new NotificationManagementSystem(context);
        nms.mostra(1);
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_PROCESS_UPDATE.equals(action)) {
                LocationResult result = LocationResult.extractResult(intent);
                if (result != null) {
                    Location location = result.getLastLocation();

                    try {
                        if(newLoc == null)
                            newLoc = location;
                        if(newLoc.distanceTo(location) > minDistance){
                            LocationType loc = new LocationType(location.getLatitude(),location.getLongitude());
                            apm.request(apm.api.checkMessages(AppParameters.appUser.getId(), loc), new ResponseHandler() {
                                @Override
                                public void onSuccess(ResponseBody data, int statusCode, String message) {
                                    if(statusCode == 200){
                                        Log.d("Receive","Bulunduğun konumda "+data.getMessages().size()+" adet yeni mesaj var.");
                                        if(!data.getMessages().isEmpty()){
                                            nms.sendCustomNotification("Hey Maceracı!","Bulunduğun konumda "+data.getMessages().size()+" adet yeni mesaj var.");
                                        }
                                    }
                                }

                                @Override
                                public void onFail(Call<ResponseBody> call) {

                                }
                            });

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
}
