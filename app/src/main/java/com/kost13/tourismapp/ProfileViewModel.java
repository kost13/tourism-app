package com.kost13.tourismapp;

import android.provider.ContactsContract;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;

public class ProfileViewModel extends ViewModel {

    private User user;

    public static final String DATA_ID_USERS = "users";

    ProfileViewModel(String userId, OnDataReadyCallback callback){
        Log.d("ProfileViewModel", userId);
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

    public User getUser(){
        return user;
    }
}
