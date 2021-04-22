package com.kost13.tourismapp;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    private static final int MAX_DESC_LEN = 100;
    boolean editEnabled;
    private ProfileViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent intent = getIntent();
        String userId = intent.getStringExtra("UID");

        editEnabled = userId.equals(Auth.getCurrentUser());

        Log.d("ProfileActivity uuid", userId);

        viewModel = new ProfileViewModel(userId);

        viewModel.getUserData(this::setupView);

        viewModel.getUserRoutesData(this::setupRoutes);
    }

    private void setupRoutes() {

        List<Route> routes = viewModel.getRoutes();

        if (routes == null) {
            return;
        }

        LinearLayout layout = (LinearLayout) findViewById(R.id.routesProfileLayout);

        for (Route route : routes) {
            View itemView = LayoutInflater.from(getBaseContext()).inflate(R.layout.route_row, null, false);
            setupRouteView(itemView, route);
            layout.addView(itemView);
        }
    }

    private void setupRouteView(View view, Route route) {
        TextView title = view.findViewById(R.id.titleTextView);
        title.setText(route.getTitle());
        title.setOnClickListener(view1 -> showRoute(route.getId()));

        TextView length = view.findViewById(R.id.routeLengthTextView);
        length.setText(String.format("%1$,.3f km", route.computeLength()));

        TextView pois = view.findViewById(R.id.routePoisTextView);
        pois.setText(route.getPoisNumber() + " POIs");

        TextView description = view.findViewById(R.id.routeDescriptionTextView);
        description.setText(chopString(route.getDescription()));

        ImageView img = view.findViewById(R.id.routeImageView);
        if (route.getImage() != null && !route.getImage().isEmpty()) {
            setRouteImage(img, route.getImage());
        } else {
            img.setVisibility(View.GONE);
        }
    }

    private void setRouteImage(ImageView view, String url) {
        if (url != null && !url.isEmpty()) {
            int width = Resources.getSystem().getDisplayMetrics().widthPixels;
            int size = 4 * width / 10;
            Picasso.with(this).load(url)
                    .resize(size, size)
                    .centerCrop()
                    .into(view);
        }
    }

    private void showRoute(String routeId) {
        Log.d("ProfileActivity", "Open route " + routeId);
    }

    private String chopString(String str) {
        if (str.length() <= MAX_DESC_LEN) {
            return str;
        }

        return str.substring(0, MAX_DESC_LEN) + "...";
    }

    private void setupView() {

        User user = viewModel.getUser();
        if (user == null) {
            return;
        }

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

        if (!url.startsWith("https://") && !url.startsWith("http://")) {
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

    private void showProfileImage(String url) {
        if (url != null && !url.isEmpty()) {
            ImageView imageView = (ImageView) findViewById(R.id.profileImageView);
            int width = Resources.getSystem().getDisplayMetrics().widthPixels;
            int size = 4 * width / 10;
            Picasso.with(this).load(url)
                    .resize(size, size)
                    .centerCrop()
                    .into(imageView);
        }
    }
}