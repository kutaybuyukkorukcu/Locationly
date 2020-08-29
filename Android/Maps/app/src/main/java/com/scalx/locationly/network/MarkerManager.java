package com.scalx.locationly.network;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.scalx.locationly.R;
import com.scalx.locationly.interfaces.ResponseHandler;
import com.scalx.locationly.model.MessageType;
import com.scalx.locationly.model.ResponseBody;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

public class MarkerManager extends FragmentActivity {
    private Map<String, MarkerOptions> responseMap = new HashMap<>();
    private Map<String, Marker> showMap = new HashMap<>();
    private Map<String, Marker> seenMap = new HashMap<>();
    private GoogleMap mMap;
    private ApiManager apm = new ApiManager();
    private Context context;

    public MarkerManager(GoogleMap mMap,Context context) {
        this.mMap = mMap;
        this.context = context;
    }

    public void postMessage(MessageType message) {
        apm.request(apm.api.postMassage(message), new ResponseHandler() {
            @Override
            public void onSuccess(ResponseBody data, int statusCode, String message) {
                if(statusCode == 200)
                    Toast.makeText(context,"Mesaj başarıyla gönderildi!",Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(context,"Mesaj gönderilirken bir sorun meydana geldi!",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFail(Call<ResponseBody> call) {}
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

    public void getDistMessages(double lat,double lon){
        apm.request(apm.api.getDistMassage(lat,lon), new ResponseHandler() {
            @Override
            public void onSuccess(ResponseBody data, int statusCode, String message) {
                if (statusCode != 200) {
                    Toast.makeText(context,"Mesajlar alınırken bir hata meydana geldi!",Toast.LENGTH_SHORT).show();
                } else if(data.getMessages() != null) {
                    Log.d("message","Markerlar getirildi -getDistMassage()");
                    responseMap.clear();
                    for (MessageType it : data.getMessages()) {
                        LatLng messageLoc = new LatLng(it.getLocation().getLat(),it.getLocation().getLon());
                        MarkerOptions m = new MarkerOptions()
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher_flag_yellow))
                                .title(it.getUsername())
                                .snippet(it.getText())
                                .position(messageLoc);
                        responseMap.put(it.getId(), m);
                    }
                    putMarkers();
                }
            }

            @Override
            public void onFail(Call<ResponseBody> call) {

            }
        });
    }
}
