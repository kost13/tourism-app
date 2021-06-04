package com.kost13.tourismapp;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.GeoPoint;
import com.google.maps.android.SphericalUtil;

import java.util.List;

public class Utils {
    static public double routeLength(List<LatLng> points){
        final double METERS_TO_KM = 0.0001;

        if (points.isEmpty()) {
            return 0.0;
        }

        double length = 0.0;
        LatLng prev_point = points.get(0);
        for (int i = 1; i < points.size(); i++) {
            length += SphericalUtil.computeDistanceBetween(prev_point, points.get(i));
            prev_point = points.get(i);
        }

        return length*METERS_TO_KM;
    }

    static public LatLng geoPointToLatLng(final GeoPoint gp){
        return new LatLng(gp.getLatitude(), gp.getLongitude());
    }
}
