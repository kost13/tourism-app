package com.kost13.tourismapp.routes;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.GeoPoint;

public class Point {
    private GeoPoint loc;
    private String poi;
    private LatLng latLng;

    public Point() {}

    public Point(LatLng latLng){
        this.latLng = latLng;
    }

    public Point(GeoPoint loc){
        setLoc(loc);
        setPoi(null);
    }

    public Point(GeoPoint loc, String poi){
        setLoc(loc);
        setPoi(poi);
    }

    public GeoPoint getLoc() {
        if(loc != null){
            return loc;
        }

        return new GeoPoint(latLng.latitude, latLng.longitude);
    }

    public LatLng latLng()  {
        if(latLng != null){
            return latLng;
        }
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
