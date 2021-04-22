package com.kost13.tourismapp;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class MapsViewModel extends ViewModel {

    private static final String TAG = "MapsViewModel";
    private List<PointOfInterest> pois;
    private Route route;

    public MapsViewModel() {
        pois = new ArrayList<>();
    }

    public void downloadRoute(OnDataReadyCallback callback) {
        String routeId = "jRP5OOxRLrr51zQxcGen";
        Database.getRoutesDb().document(routeId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                route = task.getResult().toObject(Route.class);
                if (route != null) {
                    route.setId(routeId);
                }

                callback.onDataReady();
            } else {
                Log.d("firebase", "Error getting documents: ", task.getException());
            }
        });
    }

    public void downloadPois(OnDataReadyCallback callback) {
        String routeId = "jRP5OOxRLrr51zQxcGen";

        Database.getRoutePoisDb().whereEqualTo("route", routeId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                pois.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    PointOfInterest poi = document.toObject(PointOfInterest.class);
                    if (poi != null) {
                        poi.setId(document.getId());
                        pois.add(poi);
                    }
                }
                Log.d("firebase", "POIs size " + pois.size());
                callback.onDataReady();
            } else {
                Log.d("firebase", "Error getting documents: ", task.getException());
            }
        });
    }

    public Route getRoute() {
        return route;
    }

    public List<PointOfInterest> getPois() {
        return pois;
    }

    public LatLngBounds getRouteBounds() {
        if (route.getPoints().isEmpty()) {
            return null;
        }

        double max_lat = Double.MIN_VALUE;
        double min_lat = Double.MAX_VALUE;
        double max_lng = Double.MIN_VALUE;
        double min_lng = Double.MAX_VALUE;

        for (Point point : route.getPoints()) {
            LatLng latlng = point.getLatLng();
            min_lat = Double.min(min_lat, latlng.latitude);
            max_lat = Double.max(max_lat, latlng.latitude);

            min_lng = Double.min(min_lng, latlng.longitude);
            max_lng = Double.max(max_lng, latlng.longitude);
        }

        return new LatLngBounds(new LatLng(min_lat, min_lng), new LatLng(max_lat, max_lng));
    }

}

