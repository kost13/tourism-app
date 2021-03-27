package com.kost13.tourismapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.solver.state.State;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {

    private ProfileViewModel viewModel;
    boolean editEnabled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent intent = getIntent();
        String userId = intent.getStringExtra("UID");

        editEnabled = userId.equals(Auth.getCurrentUser());

        Log.d("ProfileActivity uuid", userId);

        viewModel = new ProfileViewModel(userId, this);

    }

    private void setupView(User user){

        ImageView editProfile = (ImageView) findViewById(R.id.editProfile);
        editProfile.setVisibility(editEnabled ? View.VISIBLE : View.INVISIBLE);

        TextView nameView = findViewById(R.id.nameTextView);
        nameView.setText(user.getName());

        TextView bioView = findViewById(R.id.profileBio);
        bioView.setText(user.getBio());

        TextView languages = findViewById(R.id.languagesTextView);
        languages.setText(String.join(", ", user.getLanguages()));

        TextView locations = findViewById(R.id.locationsTextView);
        locations.setText(String.join(", ", user.getLocations()));

        TextView phone = findViewById(R.id.phoneValue);
        phone.setText(user.getTelephone());

        TextView email = findViewById(R.id.emailValue);
        email.setText(user.getEmail());

        TextView webpage = findViewById(R.id.webpageValue);
        webpage.setText(user.getWebpage());

        showProfileImage(user.getProfileImageUrl());
    }

    public void onDataReady(String dataId){
        if(dataId == "user"){
            setupView(viewModel.getUser());
        }
    }

    public void phoneClicked(View view) {
        String phone_number = viewModel.getUser().getTelephone();
        Uri phone = Uri.parse("tel:" + phone_number);
        Intent intent = new Intent(Intent.ACTION_DIAL, phone);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Log.d("ProfileActivity", "Can't handle this intent!");
        }
    }

    public void webpageClicked(View view) {
        String url = viewModel.getUser().getWebpage();

        if (!url.startsWith("https://") && !url.startsWith("http://")){
            url = "http://" + url;
        }

        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Log.d("ProfileActivity", "Can't handle this intent!");
        }
    }

    public void emailClicked(View view) {
        String address = viewModel.getUser().getEmail();
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        String[] addresses = {address};
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Log.d("ProfileActivity", "Can't handle this intent!");
        }
    }

    private void showProfileImage(String url){
        if(url != null && !url.isEmpty()){
            ImageView imageView = (ImageView) findViewById(R.id.profileImageView);
            int width = Resources.getSystem().getDisplayMetrics().widthPixels;
            int size = 4*width/10;
            Picasso.with(this).load(url)
                    .resize(size, size)
                    .centerCrop()
                    .into(imageView);
        }
    }
}