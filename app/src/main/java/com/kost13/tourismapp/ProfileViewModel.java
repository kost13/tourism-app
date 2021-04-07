package com.kost13.tourismapp;

import android.provider.ContactsContract;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ProfileViewModel extends ViewModel {

    private User user;
    private String userId;
    private List<Route> routes;

    public static final String DATA_ID_USERS = "users";
    public static final String DATA_ID_ROUTES = "routes";

    ProfileViewModel(String userId){
        this.userId = userId;
        routes = new ArrayList<>();
        Log.d("ProfileViewModel", userId);
    }

    public void getUserData(OnDataReadyCallback callback){
        Database.getUsersDb().child(userId).get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e("firebase", "Error getting data", task.getException());
            } else {
                Log.d("firebase", String.valueOf(task.getResult().getValue()));

                user = task.getResult().getValue(User.class);
                if(user != null){
                    user.setId(userId);
                    callback.onDataReady(DATA_ID_USERS);
                }
            }
        });
    }

    public void getUserRoutesData(OnDataReadyCallback callback){
        Database.getRoutesDb().whereEqualTo("user_id", userId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    routes.clear();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Route route = document.toObject(Route.class);
                        if(route != null){
                            route.setId(document.getId());
                            routes.add(route);
                        }
                    }
                    Log.d("firebase", "routes size " + String.valueOf(routes.size()));
                    callback.onDataReady(DATA_ID_ROUTES);
                } else {
                    Log.d("firebase", "Error getting documents: ", task.getException());
                }

            }
        });

    }

    public User getUser(){
        return user;
    }

    public List<Route> getRoutes() { return routes; }
}
