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

    ProfileViewModel(String userId, ProfileActivity caller){
        Log.d("ProfileViewModel", userId);
        Database.getUsersDb().child(userId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                } else {

                    Log.d("firebase", String.valueOf(task.getResult().getValue()));

                    user = task.getResult().getValue(User.class);
                    if(user != null){
                        user.setId(userId);
                        caller.onDataReady("user");
                    }

                }
            }
        });
    }

    public User getUser(){
        return user;
    }
}
