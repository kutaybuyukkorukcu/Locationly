package com.egemen.mapsdeneme;

import android.os.Message;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;
public class Server {
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("location")
    @Expose
    public Location location;
    @SerializedName("data")
    @Expose
    public List<Datum> data=null;


    public Server(String message, Location location) {

        this.message = message;
        this.location = location;
    }
    public Server withMessage(String message) {
        this.message = message;
        return this;
    }

    public Server withLocation(Location location) {
        this.location = location;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }
}
