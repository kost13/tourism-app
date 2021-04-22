package com.kost13.tourismapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;

import androidx.navigation.fragment.NavHostFragment;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;

public class MainActivity extends AppCompatActivity {

    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FragmentManager supportFragmentManager = getSupportFragmentManager();
        NavHostFragment navHostFragment =
                (NavHostFragment) supportFragmentManager.findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();

//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });


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
            case R.id.action_settings:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean openProfile() {

//        NavDirections action = ProfileFragmentDirections.ac

        Bundle bundle = new Bundle();
        bundle.putString("userId", Auth.getCurrentUser());
        navController.navigate(R.id.action_global_ProfileFragment, bundle);


//        Intent intent = new Intent(this, ProfileActivity.class);
//        intent.putExtra("UID", Auth.getCurrentUser());
//        startActivity(intent);
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
                // Successfully signed in
                Log.d("signIn", "Successfully logging in");
                // ...
            } else {
                Log.d("signIn", "Error when logging in");
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        }


    }

    @Override
    protected void onPause() {
        super.onPause();
        Auth.detachListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Auth.verifyAuth(this);
        Auth.attachListener();


    }
}