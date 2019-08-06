package com.egemen.mapsdeneme;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MessageType {
    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("location")
    @Expose
    private LocationType location;
    @SerializedName("deviceName")
    @Expose
    private String deviceName;

    @SerializedName("_id")
    @Expose
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocationType getLocation() {
        return location;
    }

    public void setLocation(LocationType location) {
        this.location = location;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public MessageType(String text, String deviceName, LocationType location) {
        this.text = text;
        this.location = location;
        this.deviceName = deviceName;
    }

    public MessageType(){}
}
