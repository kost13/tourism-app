package com.kost13.tourismapp;

import android.net.Uri;

import java.io.Serializable;

public class RouteBasicData implements Serializable {

    private String title;
    private String description;
    private Uri imageUri;


    public RouteBasicData(String title, String description, Uri imageUri){
        this.title = title;
        this.description = description;
        this.imageUri = imageUri;
    }

    public String getTitle(){
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Uri getImageUri() {
        return imageUri;
    }
}
