package com.egemen.mapsdeneme;

public class LocType {
    private Float lat;
    private Float lon;

    public LocType(Float lat,Float lon){
        this.lat=lat;
        this.lon=lon;
    }

    public Float getLat() {
        return lat;
    }

    public void setLat(Float lat) {
        this.lat = lat;
    }

    public Float getLon() {
        return lon;
    }

    public void setLon(Float lon) {
        this.lon = lon;
    }
}
