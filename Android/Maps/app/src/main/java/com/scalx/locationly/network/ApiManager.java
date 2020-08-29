package com.scalx.locationly.network;

import android.util.Log;
import android.widget.Toast;

import com.scalx.locationly.interfaces.Api;
import com.scalx.locationly.model.ResponseBody;
import com.scalx.locationly.interfaces.ResponseHandler;
import com.scalx.locationly.config.AppParameters;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiManager {

    private Retrofit retrofit = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(AppParameters.REST_API_URL)
            .build();

    public Api api = retrofit.create(Api.class);


    public void request(Call <ResponseBody> call, final ResponseHandler handler){
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                ResponseBody data = response.body();
                int statusCode = response.code();
                String message = response.message();
                Log.d("url",call.request().url().toString());

                if (!response.isSuccessful()) {
                    Log.d("Request-onResponse","Response body is null");
                }
                handler.onSuccess(data,statusCode,message);
                Log.d("Request-onResponse","Status Message:"+message);
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                handler.onFail(call);
                Log.d("Request-onFailure", t.getMessage());
                Log.d("url",call.request().url().toString());
            }
        });

    }
}
