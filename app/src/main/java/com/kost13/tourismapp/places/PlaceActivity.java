package com.kost13.tourismapp.places;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.kost13.tourismapp.Auth;
import com.kost13.tourismapp.MainActivity;
import com.kost13.tourismapp.R;

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
        if(place != null){
            redirectToMainActivity(place);
        }
    }

    private void redirectToMainActivity(String placeId){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(MainActivity.PLACE_ENTRY, placeId);
        startActivity(intent);
    }
}