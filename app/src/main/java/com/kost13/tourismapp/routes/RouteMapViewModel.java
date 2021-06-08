package com.kost13.tourismapp.routes;

import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.kost13.tourismapp.Auth;
import com.kost13.tourismapp.Database;
import com.kost13.tourismapp.OnDataReadyCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        String routeId = Database.getRoutesDb().document().getId();

        List<Point> pointsList = commitPOIs(routeId);

        Database.saveRouteImage(basicData.getImageUri(), (imgPath) -> {
            Route route = new Route();
            route.setTitle(basicData.getTitle());
            route.setDescription(basicData.getDescription());
            route.setUserId(Auth.getCurrentUser());
            route.setPoints(pointsList);
            route.setImage(imgPath);
            Database.getRoutesDb().document(routeId).set(route).addOnCompleteListener(task -> {
                points.clear();
                pois.clear();
                basicData = null;
                callback.onDataReady();
            });
        });
    }

    private List<Point> commitPOIs(String routeId){
        List<Point> pointsList = new ArrayList<>();
        Map<LatLng, String> pointsMap = new HashMap<>();

        for(PointOfInterest poi : pois){
            poi.setRoute(routeId);
            String id = Database.getRoutePoisDb().document().getId();
            Database.saveRouteImage(poi.imageUri(), (imgPath) -> {
                poi.setImage(imgPath);
                Database.getRoutePoisDb().document(id).set(poi);
            });

            pointsMap.put(poi.getLatLng(), id);
        }

        for(LatLng p : points){
            Point point = new Point(p);
            String poi = pointsMap.getOrDefault(p, "");
            if(!poi.isEmpty()){
                point.setPoi(poi);
            }
            pointsList.add(point);
        }

        return pointsList;
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

    public void clear(){
        points.clear();
        pois.clear();
        basicData = null;
    }
}
