package com.kost13.tourismapp.users;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.kost13.tourismapp.Database;
import com.kost13.tourismapp.OnDataReadyCallback;
import com.kost13.tourismapp.places.Place;
import com.kost13.tourismapp.routes.Route;

import java.util.ArrayList;
import java.util.List;

public class ProfileViewModel extends ViewModel {

    private User user;
    private String userId;
    private final List<Route> routes;
    private final List<Place> places;

    public ProfileViewModel(){
        this(null);
    }

    ProfileViewModel(String userId) {
        this.userId = userId;
        routes = new ArrayList<>();
        places = new ArrayList<>();
    }

    void setUserId(String userId){
        this.userId = userId;
    }

    void setUser(User user){ this.user = user;}

    void commitUser(OnDataReadyCallback callback){

        Database.saveProfileImage(user.imageUri(), (String url) -> {
            user.setProfileImageUrl(url);
            Database.getUsersDb().document(userId).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    callback.onDataReady();
                }
            });
        });
    }

    void clear(){
        userId = null;
        routes.clear();
    }

    public void getUserData(OnDataReadyCallback callback) {
        Database.getUsersDb().document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                } else {
                    user = task.getResult().toObject(User.class);
                    if (user != null) {
                        user.setId(userId);
                        callback.onDataReady();
                    }
                }
            }});
    }

    public void getUserRoutesData(OnDataReadyCallback callback) {
        Database.getRoutesDb().whereEqualTo("userId", userId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                routes.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Route route = document.toObject(Route.class);
                    if (route != null) {
                        route.setId(document.getId());
                        routes.add(route);
                    }
                }
                Log.d("firebase", "routes size " + routes.size());
                callback.onDataReady();
            } else {
                Log.d("firebase", "Error getting documents: ", task.getException());
            }

        });
    }

    public void getUserPlacesData(OnDataReadyCallback callback) {
        Database.getPlacesDb().whereEqualTo("userId", userId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                places.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Place place = document.toObject(Place.class);
                    if (place != null) {
                        place.setId(document.getId());
                        places.add(place);
                    }
                }
                Log.d("firebase", "places size " + routes.size());
                callback.onDataReady();
            } else {
                Log.d("firebase", "Error getting documents: ", task.getException());
            }

        });
    }

    public User getUser() {
        return user;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public List<Place> getPlaces() {
        return places;
    }
}
