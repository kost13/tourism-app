package com.kost13.tourismapp.places;

import android.net.Uri;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.GeoPoint;

public class Place {
    private String title;
    private String userId;
    private String description;
    private String image;
    private String id;
    private Uri imageUri;
    private GeoPoint loc;

    public Place(){}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }

    public GeoPoint getLoc() {
        return loc;
    }

    public void setLoc(GeoPoint loc) {
        this.loc = loc;
    }

    public void setLatLng(LatLng pos){
        this.loc = new GeoPoint(pos.latitude, pos.longitude);
    }
}
