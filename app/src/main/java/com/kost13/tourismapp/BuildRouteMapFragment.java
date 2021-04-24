package com.kost13.tourismapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Bundle;
import android.service.autofill.TextValueSanitizer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class BuildRouteMapFragment extends Fragment {

    private String routeTitle;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            LatLng sydney = new LatLng(-34, 151);
            googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            routeTitle = getArguments().getString("routeTitle");
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_build_route_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }

        TextView title = view.findViewById(R.id.textViewTitle);
        title.setText(routeTitle);

        Button preview = view.findViewById(R.id.buttonPreview);
        preview.setOnClickListener(this::onPreviewClicked);

        Button done = view.findViewById(R.id.buttonDone);
        done.setOnClickListener(this::onDone);
    }

    private void onPreviewClicked(View view){
       NavHostFragment.findNavController(BuildRouteMapFragment.this).navigate(R.id.action_BuildRouteMapFragment_to_MapFragment);
    }

    private void onDone(View view){

        //TODO save data

        ((AppCompatActivity)getContext()).getSupportFragmentManager().popBackStack(R.id.FirstFragment, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        NavHostFragment.findNavController(BuildRouteMapFragment.this).navigate(R.id.action_BuildRouteMapFragment_to_FirstFragment);
    }
}