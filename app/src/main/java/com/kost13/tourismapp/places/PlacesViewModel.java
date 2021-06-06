package com.kost13.tourismapp.places;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;
import com.kost13.tourismapp.Auth;
import com.kost13.tourismapp.Database;
import com.kost13.tourismapp.OnDataReadyCallback;
import com.kost13.tourismapp.routes.RouteBasicData;
import com.kost13.tourismapp.users.User;


public class PlacesViewModel extends ViewModel {
    private RouteBasicData basicData;
    private LatLng position;
    private String placeId;
    private boolean publicVisiblity;
    private Place place;
    private User user;

    public PlacesViewModel() {
    }

    public PlacesViewModel(String placeId) {
        this.placeId = placeId;
    }

    public RouteBasicData getBasicData() {
        return basicData;
    }

    public void setBasicData(RouteBasicData basicData) {
        this.basicData = basicData;
    }

    public void commitPlaceToDatabase(OnDataReadyCallback callback) {
        Database.savePlaceImage(basicData.getImageUri(), (imgPath) -> {
            place = new Place();
            place.setTitle(basicData.getTitle());
            place.setDescription(basicData.getDescription());
            place.setUserId(Auth.getCurrentUser());
            place.setImage(imgPath);
            place.setLatLng(position);
            Database.getPlacesDb().add(place).addOnCompleteListener(task -> {
                place.setId(task.getResult().getId());
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

    public Place getPlace() {
        return place;
    }

    public void downloadPlace(OnDataReadyCallback callback) {
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

    public void downloadUser(OnDataReadyCallback callback){
        if(place == null){
            return;
        }
        Database.getUsersDb().child(place.getUserId()).get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e("firebase", "Error getting data", task.getException());
            } else {
                Log.d("firebase", String.valueOf(task.getResult().getValue()));

                user = task.getResult().getValue(User.class);
                if (user != null) {
                    user.setId(task.getResult().getKey());
                    callback.onDataReady();
                }
            }
        });
    }

    public boolean getPublicVisiblity() {
        return publicVisiblity;
    }

    public void setPublicVisiblity(boolean publicVisiblity) {
        this.publicVisiblity = publicVisiblity;
    }

    public String getUserName() {
        if(user != null){
            return user.getName();
        }
        return "";
    }

}
