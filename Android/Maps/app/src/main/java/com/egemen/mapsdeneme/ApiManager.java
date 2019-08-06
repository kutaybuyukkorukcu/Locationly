package com.egemen.mapsdeneme;

import android.content.Context;
import android.util.JsonWriter;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.logging.Handler;

import okhttp3.OkHttpClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiManager {

    Retrofit retrofit = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(AppParameters.REST_API_URL)
            .build();

    Api api = retrofit.create(Api.class);


    public void request(Call<ResponseBody> call, final ResponseHandler handler){
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                com.egemen.mapsdeneme.ResponseBody data = response.body();
                int statusCode = response.code();
                String message = response.message();
                String urla = call.request().url().toString();
                System.out.println("jjjjjj"+urla);

                if (!response.isSuccessful()) {
                    System.out.println("Message Not Found -getAllMessages()");
                } else {
                    handler.onSucces(data,statusCode,message);
                    Log.d("Request-onResponse",message);
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("Request-onFailure", t.getMessage());
                String urla = call.request().url().toString();
                System.out.println("jjjjjj"+urla);
            }
        });


    }
}
