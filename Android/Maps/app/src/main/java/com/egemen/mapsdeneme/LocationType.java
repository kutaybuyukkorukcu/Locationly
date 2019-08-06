package com.egemen.mapsdeneme;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class LocationType {

    @SerializedName("lat")
    @Expose
    private Double lat;
    @SerializedName("lon")
    @Expose
    private Double lon;

    public LocationType(Double lat, Double lon) {

        this.lat = lat;
        this.lon = lon;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }
}
