package com.example.tourismapp;

import androidx.lifecycle.ViewModel;

public class ProfileViewModel extends ViewModel {

    private User user;

    ProfileViewModel(String userId){
        //todo: collect user from firebase
        user = new User("Adam", "Kowalczyk", "Bunkrów nie ma!");
        this.user = user;
    }

    public User getUser(){
        return user;
    }
}
