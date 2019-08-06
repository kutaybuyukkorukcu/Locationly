package com.egemen.mapsdeneme;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface Api {

    //@POST("api/messages")
    //Call<JsonObject> postMassage(@Body JsonObject all);

    @POST("api/messages")
    Call<ResponseBody> postMassage(@Body MessageType message);

    @POST("api/distMessages")
    Call<JsonObject[]> postDistMassage(@Body JsonObject dist);

    @GET("api/meessages")
    Call<ResponseBody> getDistMassage(@Query("lat") double lat,@Query("lon") double lon);

    @POST("api/user")
    Call<ResponseBody> postUser(@Body User user);

    @GET("api/user")
    Call<String> getUser(@Query("_id") String id);

}
