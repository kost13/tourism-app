package com.kost13.tourismapp.users;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.kost13.tourismapp.Auth;
import com.kost13.tourismapp.R;
import com.kost13.tourismapp.maps.Route;
import com.kost13.tourismapp.maps.RouteMapViewModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProfileFragment extends Fragment {

    private static final int MAX_DESC_LEN = 100;
    boolean editEnabled;
    private ProfileViewModel viewModel;
    private View currentView;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(ProfileViewModel.class);

        if (getArguments() != null) {
            String userId = getArguments().getString("userId");
            Log.d("profile fragmnet", "userID " + userId);

            editEnabled = userId.equals(Auth.getCurrentUser());

            Log.d("ProfileActivity uuid", userId);
            viewModel.clear();
            viewModel.setUserId(userId);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        viewModel.getUserData(this::setupView);
        viewModel.getUserRoutesData(this::setupRoutes);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }


    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        currentView = view;
        setupView();
        setupRoutes();
    }

    private void setupRoutes() {

        if(currentView == null){
            return;
        }

        List<Route> routes = viewModel.getRoutes();

        if (routes == null) {
            return;
        }

        LinearLayout layout = (LinearLayout) currentView.findViewById(R.id.routesProfileLayout);

        for (Route route : routes) {
            View itemView = LayoutInflater.from(getContext()).inflate(R.layout.route_row, null, false);
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
            Picasso.with(getContext()).load(url)
                    .resize(size, size)
                    .centerCrop()
                    .into(view);
        }
    }

    private void showRoute(String routeId) {
        Log.d("ProfileActivity", "Open route " + routeId);
        Bundle route = new Bundle();
        route.putString("routeId", routeId);
        NavHostFragment.findNavController(ProfileFragment.this).navigate(R.id.action_ProfileFragment_to_MapFragment, route);
    }

    private String chopString(String str) {
        if (str.length() <= MAX_DESC_LEN) {
            return str;
        }

        return str.substring(0, MAX_DESC_LEN) + "...";
    }

    private void setupView() {

        if(currentView == null){
            return;
        }

        User user = viewModel.getUser();
        if (user == null) {
            return;
        }

        ImageView editProfile = (ImageView) currentView.findViewById(R.id.editProfile);
        editProfile.setVisibility(editEnabled ? View.VISIBLE : View.INVISIBLE);

        TextView nameView = currentView.findViewById(R.id.nameTextView);
        nameView.setText(user.getName());

        TextView bioView = currentView.findViewById(R.id.profileBio);
        bioView.setText(user.getBio());

        TextView languages = currentView.findViewById(R.id.languagesTextView);
        languages.setText(String.join(", ", user.getLanguages()));

        TextView locations = currentView.findViewById(R.id.locationsTextView);
        locations.setText(String.join(", ", user.getLocations()));

        TextView phone = currentView.findViewById(R.id.phoneValue);
        phone.setText(user.getTelephone());
        phone.setOnClickListener(this::phoneClicked);

        TextView email = currentView.findViewById(R.id.emailValue);
        email.setText(user.getEmail());
        email.setOnClickListener(this::emailClicked);

        TextView webpage = currentView.findViewById(R.id.webpageValue);
        webpage.setText(user.getWebpage());
        webpage.setOnClickListener(this::webpageClicked);

        showProfileImage(user.getProfileImageUrl());

        ImageButton editButton = currentView.findViewById(R.id.editProfile);
        editButton.setOnClickListener(this::editProfile);
    }


    public void phoneClicked(View view) {
        String phone_number = viewModel.getUser().getTelephone();
        Uri phone = Uri.parse("tel:" + phone_number);
        Intent intent = new Intent(Intent.ACTION_DIAL, phone);

        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
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

        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
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

        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Log.d("ProfileActivity", "Can't handle this intent!");
        }
    }

    private void showProfileImage(String url) {
        if (url != null && !url.isEmpty()) {
            ImageView imageView = (ImageView) currentView.findViewById(R.id.profileImageView);
            int width = Resources.getSystem().getDisplayMetrics().widthPixels;
            int size = 4 * width / 10;
            Picasso.with(getContext()).load(url)
                    .resize(size, size)
                    .centerCrop()
                    .into(imageView);
        }
    }

    public void editProfile(View view){
        NavHostFragment.findNavController(ProfileFragment.this).navigate(R.id.action_ProfileFragment_to_ProfileEditFragment);
    }
}