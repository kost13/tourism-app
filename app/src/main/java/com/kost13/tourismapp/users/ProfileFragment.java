package com.kost13.tourismapp.users;

import android.content.Intent;
import android.content.res.Resources;
import android.net.PlatformVpnProfile;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.kost13.tourismapp.places.Place;
import com.kost13.tourismapp.routes.Route;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import org.w3c.dom.Text;

import java.util.List;

public class ProfileFragment extends Fragment {

    private static final int MAX_DESC_LEN = 100;
    public static final String USER_ID = "userId";
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
            String userId = getArguments().getString(USER_ID);
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
        viewModel.getUserPlacesData(this::setupPlaces);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }


    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        currentView = view;
        setupView();
        setupRoutes();
        setupPlaces();
    }

    private void setupPlaces(){
        if(currentView == null){
            return;
        }

        List<Place> places = viewModel.getPlaces();

        if (places == null) {
            return;
        }

        LinearLayout layout = (LinearLayout) currentView.findViewById(R.id.placesProfileLayout);

        if(layout.getChildCount() > 0){
            return;
        }

        TextView placesTV = currentView.findViewById(R.id.places);
        placesTV.setVisibility(places.isEmpty() ? View.GONE : View.VISIBLE);

        for (Place place : places) {
            if(!place.getPublicVisibility() && Auth.getCurrentUser().compareTo(place.getUserId()) != 0){
                continue;
            }
            View itemView = LayoutInflater.from(getContext()).inflate(R.layout.route_row, null, false);
            setupPlaceView(itemView, place);
            layout.addView(itemView);
        }
    }

    private void setupRoutes() {

        if(currentView == null){
            return;
        }

        List<Route> routes = viewModel.getRoutes();
        User user = viewModel.getUser();

        if (routes == null || user == null) {
            return;
        }

        TextView routesTV = currentView.findViewById(R.id.tourRoutes);
        routesTV.setVisibility((routes.isEmpty() || !user.getIsGuide()) ? View.GONE : View.VISIBLE);

        if(!user.getIsGuide()) {
            return;
        }

        LinearLayout layout = currentView.findViewById(R.id.routesProfileLayout);
        if(layout.getChildCount() > 0){
            return;
        }


        for (Route route : routes) {
            View itemView = LayoutInflater.from(getContext()).inflate(R.layout.route_row, null, false);
            setupRouteView(itemView, route);
            layout.addView(itemView);
        }
    }

    private  void setupPlaceView(View view, Place place){
        TextView title = view.findViewById(R.id.titleTextView);
        title.setText(place.getTitle());
        title.setOnClickListener(view1 -> showPlace(place.getId()));

        TextView length = view.findViewById(R.id.routeLengthTextView);
        length.setVisibility(View.GONE);

        TextView pois = view.findViewById(R.id.routePoisTextView);
        pois.setVisibility(View.GONE);

        if(!place.getPublicVisibility()){
            ImageView lock = view.findViewById(R.id.imageViewLock);
            lock.setVisibility(View.VISIBLE);
        }

        TextView description = view.findViewById(R.id.routeDescriptionTextView);
        description.setText(chopString(place.getDescription()));

        ImageView img = view.findViewById(R.id.routeImageView);
        if (place.getImage() != null && !place.getImage().isEmpty()) {
            setRouteImage(img, place.getImage());
        } else {
            img.setVisibility(View.GONE);
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

    private void showPlace(String placeId){
        Bundle route = new Bundle();
        route.putString("placeId", placeId);
        NavHostFragment.findNavController(ProfileFragment.this).navigate(R.id.action_ProfileFragment_to_PlaceViewFragmet, route);
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

        LinearLayout layout = currentView.findViewById(R.id.routesProfileLayout);
        TextView routesTitle = currentView.findViewById(R.id.tourRoutes);

        if(user.getIsGuide()){
            layout.setVisibility(View.VISIBLE);
            routesTitle.setVisibility(View.VISIBLE);
        } else {
            layout.setVisibility(View.GONE);
            routesTitle.setVisibility(View.GONE);
        }

        ImageView editProfile = currentView.findViewById(R.id.editProfile);
        editProfile.setVisibility(editEnabled ? View.VISIBLE : View.INVISIBLE);
        editProfile.setOnClickListener(this::editProfile);

        TextView nameView = currentView.findViewById(R.id.nameTextView);
        nameView.setText(user.getName());

        TextView bioView = currentView.findViewById(R.id.profileBio);
        bioView.setText(user.getBio());

        TextView locations = currentView.findViewById(R.id.locationsTextView);
        locations.setText(user.getLocation());

        setupPhone(user);
        setupEmail(user);
        setupWebpage(user);

        showProfileImage(user.getProfileImageUrl());

        TextView guideTV = currentView.findViewById(R.id.textViewGuide);
        guideTV.setVisibility(user.getIsGuide() ? View.VISIBLE : View.INVISIBLE);
    }

    void setupPhone(User user){
        TextView phone = currentView.findViewById(R.id.phoneValue);
        TextView label = currentView.findViewById(R.id.phoneLabel);
        String telephone =user.getTelephone();
        if(telephone == null || telephone.isEmpty()){
            phone.setVisibility(View.GONE);
            label.setVisibility(View.GONE);
        } else {
            phone.setVisibility(View.VISIBLE);
            label.setVisibility(View.VISIBLE);
            phone.setText(telephone);
            phone.setOnClickListener(this::phoneClicked);
        }
    }

    void setupEmail(User user){
        TextView email = currentView.findViewById(R.id.emailValue);
        TextView label = currentView.findViewById(R.id.emailLabel);
        String emailVal = user.getEmail();
        if(emailVal == null || emailVal.isEmpty()){
            email.setVisibility(View.GONE);
            label.setVisibility(View.GONE);
        } else {
            email.setVisibility(View.VISIBLE);
            label.setVisibility(View.VISIBLE);
            email.setText(emailVal);
            email.setOnClickListener(this::emailClicked);
        }
    }

    void setupWebpage(User user){
        TextView webpage = currentView.findViewById(R.id.webpageValue);
        TextView label = currentView.findViewById(R.id.webpageLabel);
        String page  = user.getWebpage();
        if(page == null || page.isEmpty()){
            webpage.setVisibility(View.GONE);
            label.setVisibility(View.GONE);
        } else {
            webpage.setVisibility(View.VISIBLE);
            label.setVisibility(View.VISIBLE);
            webpage.setText(page);
            webpage.setOnClickListener(this::webpageClicked);
        }
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
        int width = Resources.getSystem().getDisplayMetrics().widthPixels;
        int size = (int) (0.4 * width);

        RequestCreator creator;

        ImageView imageView = currentView.findViewById(R.id.profileImageView);

        if (url != null && !url.isEmpty()) {
            creator = Picasso.with(getContext()).load(url);
        } else {
            creator = Picasso.with(getContext()).load(R.drawable.profile);
        }

        creator.resize(size, size)
                .centerCrop()
                .into(imageView);
    }

    public void editProfile(View view){
        NavHostFragment.findNavController(ProfileFragment.this).navigate(R.id.action_ProfileFragment_to_ProfileEditFragment);
    }
}