package com.scalx.locationly.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MessageType {
    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("location")
    @Expose
    private LocationType location;
    @SerializedName("username")
    @Expose
    private String username;

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public MessageType(String text, String username, LocationType location) {
        this.text = text;
        this.location = location;
        this.username = username;
    }

    public MessageType(String text, String username, double lat, double lon) {
        this.text = text;
        this.location = new LocationType(lat,lon);;
        this.username = username;
    }

    public MessageType(){}

    @Override
    public String toString() {
        return "MessageType{" +
                "text='" + text + '\'' +
                ", location=" + location.getLon()+","+location.getLat() +
                ", username='" + username + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
