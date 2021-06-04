package com.kost13.tourismapp.users;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.kost13.tourismapp.Database;
import com.kost13.tourismapp.OnDataReadyCallback;
import com.kost13.tourismapp.routes.Route;

import java.util.ArrayList;
import java.util.List;

public class ProfileViewModel extends ViewModel {

    private User user;
    private String userId;
    private final List<Route> routes;

    public ProfileViewModel(){
        routes = new ArrayList<>();
    }

    ProfileViewModel(String userId) {
        this.userId = userId;
        routes = new ArrayList<>();
        Log.d("ProfileViewModel", userId);
    }

    void setUserId(String userId){
        this.userId = userId;
    }

    void setUser(User user){ this.user = user;}

    void commitUser(OnDataReadyCallback callback){

        Database.saveProfileImage(user.imageUri(), (String url) -> {
            user.setProfileImageUrl(url);
            Database.getUsersDb().child(userId).setValue(user).addOnCompleteListener(task -> {
                callback.onDataReady();
            });
        });
    }

    void clear(){
        userId = null;
        routes.clear();
    }

    public void getUserData(OnDataReadyCallback callback) {
        Database.getUsersDb().child(userId).get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e("firebase", "Error getting data", task.getException());
            } else {
                Log.d("firebase", String.valueOf(task.getResult().getValue()));

                user = task.getResult().getValue(User.class);
                if (user != null) {
                    user.setId(userId);
                    callback.onDataReady();
                }
            }
        });
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

    public User getUser() {
        return user;
    }

    public List<Route> getRoutes() {
        return routes;
    }
}
