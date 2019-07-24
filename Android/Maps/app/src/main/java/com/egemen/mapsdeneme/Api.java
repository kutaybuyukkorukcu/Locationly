package com.egemen.mapsdeneme;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Api {
    @GET("api/messages")
    Call<Server> cagir(@Query("status") int status);


    @POST("api/messages")
    Call<JsonObject> at(@Body JsonObject all);

    @POST("api/distMessages")
    Call<JsonObject[]> dist(@Body JsonObject dist);

}
