package com.kost13.tourismapp;

import android.net.Uri;

import com.google.android.gms.maps.model.LatLng;
import com.google.common.collect.Lists;
import com.google.maps.android.SphericalUtil;

import java.util.List;

public class Route {
    private String title;
    private String userId;
    private String description;
    private String image;
    private List<Point> points;
    private String id;
    private Uri imageUri;

    public Route() {
    }

    public Route(String title, String description, String image, List<Point> points) {
        setTitle(title);
        setDescription(description);
        setImage(image);
        setPoints(points);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<Point> getPoints() {
        return points;
    }

    public void setPoints(List<Point> points) {
        this.points = points;
    }

    public double computeLength() {

        final double METERS_TO_KM = 0.0001;

        if (points.isEmpty()) {
            return 0.0;
        }

        double length = 0.0;
        Point prev_point = points.get(0);
        for (int i = 1; i < points.size(); i++) {
            length += SphericalUtil.computeDistanceBetween(prev_point.latLng(), points.get(i).latLng());
            prev_point = points.get(i);
        }

        return length*METERS_TO_KM;
    }

    public int getPoisNumber() {
        int poi_count = 0;
        for (Point point : points) {
            if (point.getPoi() != null) {
                poi_count++;
            }
        }

        return poi_count;
    }

    public List<LatLng> generatePointCoordinates() {
        return Lists.transform(points, Point::latLng);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Uri imageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
