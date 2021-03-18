package com.kost13.tourismapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {

    private ProfileViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent intent = getIntent();
        String userId = intent.getStringExtra("UID");

        Log.d("ProfileActivity uuid", userId);

        viewModel = new ProfileViewModel(userId, this);

    }

    private void setupView(User user){
        TextView nameView = findViewById(R.id.profileName);
        nameView.setText(user.getFullName());

        TextView bioView = findViewById(R.id.profileBio);
        bioView.setText(user.getBio());
    }

    public void onDataReady(String dataId){
        if(dataId == "user"){
            setupView(viewModel.getUser());
        }
    }
}