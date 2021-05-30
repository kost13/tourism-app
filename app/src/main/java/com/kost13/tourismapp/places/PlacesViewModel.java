package com.kost13.tourismapp.places;

import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;
import com.kost13.tourismapp.Auth;
import com.kost13.tourismapp.Database;
import com.kost13.tourismapp.OnDataReadyCallback;
import com.kost13.tourismapp.maps.RouteBasicData;


public class PlacesViewModel extends ViewModel {
    private RouteBasicData basicData;
    private LatLng position;

    public void setBasicData(RouteBasicData basicData) {
        this.basicData = basicData;
    }

    public RouteBasicData getBasicData(){ return basicData; }

    public void commitPlaceToDatabase(OnDataReadyCallback callback){
        Database.savePlaceImage(basicData.getImageUri(), (imgPath) -> {
            Place place = new Place();
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
}
