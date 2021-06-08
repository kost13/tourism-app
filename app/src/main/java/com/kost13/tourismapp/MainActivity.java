package com.kost13.tourismapp;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;

import androidx.navigation.fragment.NavHostFragment;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.kost13.tourismapp.places.PlaceViewFragment;

public class MainActivity extends AppCompatActivity {

    private NavController navController;

    static public final String PLACE_ENTRY = "place";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Auth.verifyAuth(this);
        Auth.attachListener();

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FragmentManager supportFragmentManager = getSupportFragmentManager();
        NavHostFragment navHostFragment =
                (NavHostFragment) supportFragmentManager.findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();

        Intent intent = getIntent();
        String place = intent.getStringExtra(MainActivity.PLACE_ENTRY);
        if(place != null){
            openPlace(place);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        switch (id) {
            case R.id.action_my_profile:
                return openProfile();
            case R.id.action_sign_out:
                return signOut();
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean openProfile() {
        Bundle bundle = new Bundle();
        bundle.putString("userId", Auth.getCurrentUser());
        navController.navigate(R.id.action_global_ProfileFragment, bundle);
        return true;
    }

    private boolean signOut() {
        AuthUI.getInstance().signOut(this).addOnCompleteListener(task -> {
            Log.d("Logout", "User logged out");
            Auth.attachListener();
        });
        Auth.detachListener();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Auth.signInCode()) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                Log.d("signIn", "Successfully logging in");
                Bundle bundle = data.getExtras();
                if(bundle != null){
                    Auth.commitNewUser(bundle.getString("mEmail"), bundle.getString("mName"));
                    for(String key : bundle.keySet()){
                        Log.e("MainActivityIntent", key + " : " + (bundle.get(key) != null ? bundle.get(key) : "NULL"));
                    }
                }
            } else {
                Log.d("signIn", "Error when logging in");
                Toast.makeText(this, "Error when logging in.", Toast.LENGTH_LONG);
            }
        }
    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        Auth.detachListener();
//    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        Auth.verifyAuth(this);
//        Auth.attachListener();
//    }

    private void openPlace(String placeId){
        Bundle bundle = new Bundle();
        bundle.putString(PlaceViewFragment.PLACE_ID, placeId);
        navController.navigate(R.id.action_global_PlaceViewFragmet, bundle);
    }
}