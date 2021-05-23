package com.kost13.tourismapp;

import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;
import java.util.List;

public class RouteMapViewModel extends ViewModel {
    private RouteBasicData basicData;
    private List<LatLng> points;
    private List<PointOfInterest> pois;

    public RouteMapViewModel(){
        setPoints(new ArrayList<>());
        setPois(new ArrayList<>());
    }

    public void setBasicData(RouteBasicData basicData) {
        this.basicData = basicData;
    }

    public void commitRouteToDatabase(OnDataReadyCallback callback){
        //

        System.out.println("hehe");
        callback.onDataReady();

    }

    public RouteBasicData getBasicData() {
        return basicData;
    }

    public List<LatLng> getPoints() {
        return points;
    }

    public void setPoints(List<LatLng> points) {
        this.points = points;
    }

    public List<PointOfInterest> getPois() {
        return pois;
    }

    public void setPois(List<PointOfInterest> pois) {
        this.pois = pois;
    }

    public LatLngBounds getRouteBounds() {
        double max_lat = Double.MIN_VALUE;
        double min_lat = Double.MAX_VALUE;
        double max_lng = Double.MIN_VALUE;
        double min_lng = Double.MAX_VALUE;

        for (LatLng latlng : points) {
            min_lat = Double.min(min_lat, latlng.latitude);
            max_lat = Double.max(max_lat, latlng.latitude);

            min_lng = Double.min(min_lng, latlng.longitude);
            max_lng = Double.max(max_lng, latlng.longitude);
        }

        return new LatLngBounds(new LatLng(min_lat, min_lng), new LatLng(max_lat, max_lng));
    }
}
