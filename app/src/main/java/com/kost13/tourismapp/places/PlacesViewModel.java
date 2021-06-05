package com.kost13.tourismapp.places;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.kost13.tourismapp.Auth;
import com.kost13.tourismapp.Database;
import com.kost13.tourismapp.OnDataReadyCallback;
import com.kost13.tourismapp.routes.PointOfInterest;
import com.kost13.tourismapp.routes.RouteBasicData;


public class PlacesViewModel extends ViewModel {
    private RouteBasicData basicData;
    private LatLng position;
    private String placeId;
    private Place place;

     public PlacesViewModel(){}

    public PlacesViewModel(String placeId){
         this.placeId = placeId;
     }

    public void setBasicData(RouteBasicData basicData) {
        this.basicData = basicData;
    }

    public RouteBasicData getBasicData(){ return basicData; }

    public void commitPlaceToDatabase(OnDataReadyCallback callback){
        Database.savePlaceImage(basicData.getImageUri(), (imgPath) -> {
            place = new Place();
            place.setTitle(basicData.getTitle());
            place.setDescription(basicData.getDescription());
            place.setUserId(Auth.getCurrentUser());
            place.setImage(imgPath);
            place.setLatLng(position);
            Database.getPlacesDb().add(place).addOnCompleteListener(task -> {
                callback.onDataReady();
            });
        });
    }

    public LatLng getPosition() {
        return position;
    }

    public void setPosition(LatLng position) {
        this.position = position;
    }

    public Place getPlace(){ return place; }

    public void downloadPlace(OnDataReadyCallback callback){
        Database.getPlacesDb().document(placeId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                place = task.getResult().toObject(Place.class);
                place.setId(task.getResult().getId());
                Log.d("firebase", "downloaded place " + placeId);
                callback.onDataReady();
            } else {
                Log.d("firebase", "Error getting documents: ", task.getException());
            }
        });
    }
}
