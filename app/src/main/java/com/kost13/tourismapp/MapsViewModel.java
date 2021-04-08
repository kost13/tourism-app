package com.kost13.tourismapp;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MapsViewModel extends ViewModel {

    private static String TAG = "MapsViewModel";
    private List<PointOfInterest> pois;
    private Route route;

    public static final String DATA_ID_ROUTES = "routes";
    public static final String DATA_ID_ROUTE_POIS = "pois";

    public MapsViewModel(){
        pois = new ArrayList<PointOfInterest>();
    }

    public void downloadRoute(OnDataReadyCallback callback) {
        String routeId = "jRP5OOxRLrr51zQxcGen";
        Database.getRoutesDb().document(routeId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    route = task.getResult().toObject(Route.class);
                    if (route != null) {
                        route.setId(routeId);
                    }

//                    for (QueryDocumentSnapshot document : task.getResult()) {
//                        Route route = document.toObject(Route.class);
//                        if(route != null){
//                            route.setId(document.getId());
//                            routes.add(route);
//                        }
//                    }
//                    Log.d("firebase", "routes size " + String.valueOf(routes.size()));
                    callback.onDataReady(DATA_ID_ROUTES);
                } else {
                    Log.d("firebase", "Error getting documents: ", task.getException());
                }
            }
        });
    }

    public void downloadPois(OnDataReadyCallback callback) {
        String routeId = "jRP5OOxRLrr51zQxcGen";

        Database.getRoutePoisDb().whereEqualTo("route", routeId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    pois.clear();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        PointOfInterest poi = document.toObject(PointOfInterest.class);
                        if (poi != null) {
                            poi.setId(document.getId());
                            pois.add(poi);
                        }
                    }
                    Log.d("firebase", "POIs size " + String.valueOf(pois.size()));
                    callback.onDataReady(DATA_ID_ROUTE_POIS);
                } else {
                    Log.d("firebase", "Error getting documents: ", task.getException());
                }
            }
        });
    }

//        Database.getRoutesDb().get().addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        DocumentSnapshot document = task.getResult();
//                        if (document.exists()) {
//                            routes.clear();
//                            try {
//                                @SuppressWarnings("unchecked")
//                                ArrayList<GeoPoint> data = (ArrayList<GeoPoint>) document.get("points");
//                                for (GeoPoint gp : data) {
//                                    routes.add(new LatLng(gp.getLatitude(), gp.getLongitude()));
//                                }
//                                callback.onDataReady(DATA_ID_ROUTES);
//                            } catch (ClassCastException | NullPointerException exception) {
//                                Log.d(TAG, "Cannot read points");
//                                return;
//                            }
//                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
//                        } else {
//                            Log.d(TAG, "No such document");
//                        }
//                    } else {
//                        Log.d(TAG, "get failed with ", task.getException());
//                    }
//                }
//        );
//    }

//    public ArrayList<LatLng> getRoutes() {
//        return routes;
//    }

    public Route getRoute(){
        return route;
    }

    public List<PointOfInterest> getPois(){
        return pois;
    }

    public LatLngBounds getRouteBounds(){
        if(route.getPoints().isEmpty()){
            return null;
        }

        double max_lat = Double.MIN_VALUE;
        double min_lat = Double.MAX_VALUE;
        double max_lng = Double.MIN_VALUE;
        double min_lng = Double.MAX_VALUE;

        for(Point point : route.getPoints()){
            LatLng latlng = point.getLatLng();
            min_lat = Double.min(min_lat, latlng.latitude);
            max_lat = Double.max(max_lat, latlng.latitude);

            min_lng = Double.min(min_lng, latlng.longitude);
            max_lng = Double.max(max_lng, latlng.longitude);
        }

        return new LatLngBounds(new LatLng(min_lat, min_lng), new LatLng(max_lat, max_lng));
    }

}

