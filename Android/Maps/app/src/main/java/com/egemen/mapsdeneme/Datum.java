package com.egemen.mapsdeneme;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("location")
    @Expose
    public Location location;



}
