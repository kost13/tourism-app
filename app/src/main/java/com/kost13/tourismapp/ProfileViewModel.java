package com.kost13.tourismapp;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ProfileViewModel extends ViewModel {

    private User user;
    private final String userId;
    private final List<Route> routes;


    ProfileViewModel(String userId) {
        this.userId = userId;
        routes = new ArrayList<>();
        Log.d("ProfileViewModel", userId);
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
        Database.getRoutesDb().whereEqualTo("user_id", userId).get().addOnCompleteListener(task -> {
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
