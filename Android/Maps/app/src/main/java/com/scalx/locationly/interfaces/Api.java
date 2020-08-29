package com.scalx.locationly.interfaces;

import com.scalx.locationly.model.LocationType;
import com.scalx.locationly.model.MessageType;
import com.scalx.locationly.model.ResponseBody;
import com.scalx.locationly.model.UserType;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Api {

    //@POST("api/messages")
    //Call<JsonObject> postMassage(@Body JsonObject all);

    // Old
    @POST("api/messages")
    Call<ResponseBody> postMassage(@Body MessageType message);

    @GET("api/messages/distance")
    Call<ResponseBody> getDistMassage(@Query("lat") double lat, @Query("lon") double lon);

    @POST("api/users")
    Call<ResponseBody> postUser(@Body UserType userType);

    @GET("api/userType")
    Call<ResponseBody> getAllUsers();

    @POST("api/users/login")
    Call<ResponseBody> postLogin(@Body UserType userType);

    @GET("api/users/id")
    Call<ResponseBody> getUser(@Query("id") String id);

    @POST("api/users/checkMessages")
    Call<ResponseBody> checkMessages(@Field("_id") String userId, @Field("location") LocationType location);

}
