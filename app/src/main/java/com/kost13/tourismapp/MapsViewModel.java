package com.kost13.tourismapp;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;

public class MapsViewModel extends ViewModel {

    private static String TAG = "MapsViewModel";
    private ArrayList<LatLng> routes;

    public static final String DATA_ID_ROUTES = "routes";

    public MapsViewModel(){
        routes = new ArrayList<LatLng>();
    }

    public void downloadRoutes(OnDataReadyCallback callback) {
        Database.getRoutesDocsReference().get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            routes.clear();
                            try {
                                @SuppressWarnings("unchecked")
                                ArrayList<GeoPoint> data = (ArrayList<GeoPoint>) document.get("points");
                                for (GeoPoint gp : data) {
                                    routes.add(new LatLng(gp.getLatitude(), gp.getLongitude()));
                                }
                                callback.onDataReady(DATA_ID_ROUTES);
                            } catch (ClassCastException | NullPointerException exception) {
                                Log.d(TAG, "Cannot read points");
                                return;
                            }
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
        );
    }

    public ArrayList<LatLng> getRoutes() {
        return routes;
    }
}

