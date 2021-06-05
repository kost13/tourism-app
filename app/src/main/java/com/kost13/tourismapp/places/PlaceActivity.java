package com.kost13.tourismapp.places;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;

import com.kost13.tourismapp.R;
import com.kost13.tourismapp.places.PlaceViewFragment;

public class PlaceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        Uri data = intent.getData();
        String place = data.getPathSegments().get(0);

        PlaceViewFragment fragment = (PlaceViewFragment) getSupportFragmentManager().findFragmentById(R.id.placeLayout);
        if(place != null){
            Log.d("PlaceActivity", place);
            fragment.setup(place);
        }


    }
}