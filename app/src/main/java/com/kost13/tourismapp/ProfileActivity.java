package com.kost13.tourismapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ProfileViewModel viewModel = new ProfileViewModel("userId");
        setView(viewModel.getUser());
    }

    private void setView(User user){
        TextView nameView = findViewById(R.id.profileName);
        nameView.setText(user.getFullName());

        TextView bioView = findViewById(R.id.profileBio);
        bioView.setText(user.getBio());
    }
}