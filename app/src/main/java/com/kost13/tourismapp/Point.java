package com.kost13.tourismapp;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.GeoPoint;

public class Point {
    private GeoPoint loc;
    private String poi;

    public Point() {}

    public Point(GeoPoint loc){
        setLoc(loc);
        setPoi(null);
    }

    public Point(GeoPoint loc, String poi){
        setLoc(loc);
        setPoi(poi);
    }

    public GeoPoint getLoc() {
        return loc;
    }

    public LatLng getLatLng() {
        return new LatLng(loc.getLatitude(), loc.getLongitude());
    }

    public void setLoc(GeoPoint loc) {
        this.loc = loc;
    }

    public String getPoi() {
        return poi;
    }

    public void setPoi(String poi) {
        this.poi = poi;
    }
}
