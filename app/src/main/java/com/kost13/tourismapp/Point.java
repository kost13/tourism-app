package com.kost13.tourismapp;

import com.google.firebase.firestore.GeoPoint;

public class Point {
    private GeoPoint geoPoint;
    private String poiId;

    public Point() {}

    public Point(GeoPoint point){
        setGeoPoint(point);
        setPoiId(null);
    }

    public Point(GeoPoint point, String poiId){
        setGeoPoint(point);
        setPoiId(poiId);
    }

    public GeoPoint getGeoPoint() {
        return geoPoint;
    }

    public void setGeoPoint(GeoPoint geoPoint) {
        this.geoPoint = geoPoint;
    }

    public String getPoiId() {
        return poiId;
    }

    public void setPoiId(String poiId) {
        this.poiId = poiId;
    }
}
