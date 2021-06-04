package com.kost13.tourismapp.places;

import android.app.AlertDialog;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.kost13.tourismapp.R;
import com.kost13.tourismapp.routes.RouteBasicData;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class PlacesFragment extends Fragment {

    private GoogleMap map;
    private View view;
    private Map<Marker, RouteBasicData> placesMap;

    private OnMapReadyCallback callback = (GoogleMap googleMap) -> {

            LatLng warsaw = new LatLng(52.22987, 21.01199);
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(warsaw));
            googleMap.moveCamera(CameraUpdateFactory.zoomTo(12.0f));
            map = googleMap;

            googleMap.setOnMapLongClickListener(latLng -> addPlace(latLng));
            googleMap.setOnMarkerClickListener(this::onMarkerClicked);
            tryAddMarker();
    };

    private boolean onMarkerClicked(Marker marker){
        RouteBasicData data = placesMap.getOrDefault(marker, null);
        if(data == null){
            return false;
        }
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(requireActivity());
        View layoutView = getLayoutInflater().inflate(R.layout.dialog_place_details, null);

        TextView titleView = layoutView.findViewById(R.id.textViewTitle);
        titleView.setText(data.getTitle());

        TextView descView = layoutView.findViewById(R.id.textViewDescription);
        descView.setText(data.getDescription());

        ImageView img = layoutView.findViewById(R.id.imageViewPlace);
        addPlaceImage(img, data.getImageUri());

        Button closeButton = layoutView.findViewById(R.id.buttonClose);
        dialogBuilder.setView(layoutView);
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
        closeButton.setOnClickListener(view -> alertDialog.dismiss());

        return true;
    }

    private void addPlaceImage(ImageView view, Uri img) {
        int width = Resources.getSystem().getDisplayMetrics().widthPixels;

        if (img != null) {
            Picasso.with(getContext()).load(img).resize(width, width)
                    .centerCrop()
                    .into(view);
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }

    public PlacesFragment() {
        placesMap = new HashMap<>();
    }

    private void tryAddMarker() {
        PlacesViewModel placesViewModel = new ViewModelProvider(requireActivity()).get(PlacesViewModel.class);
        LatLng pos = placesViewModel.getPosition();
        if (pos != null) {
            MarkerOptions markerOptions = new MarkerOptions().position(pos);
            RouteBasicData data = placesViewModel.getBasicData();
            if (data != null) {
                markerOptions = markerOptions.title(data.getTitle());
            }
            Marker marker  = map.addMarker(markerOptions);
            placesMap.put(marker, data);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_places, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }

        this.view = view;
    }

    private void addPlace(LatLng latLng) {
        String[] options = {"Add place", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setItems(options, (dialog, which) -> {
            switch (which) {
                case 0:
                    openPlaceBuilder(latLng);
                    break;
                default:
                    break;
            }
        });
        builder.show();
    }

    private void openPlaceBuilder(LatLng latLng) {
        PlacesViewModel placesViewModel = new ViewModelProvider(requireActivity()).get(PlacesViewModel.class);
        placesViewModel.setPosition(latLng);
        NavHostFragment.findNavController(PlacesFragment.this).navigate(R.id.action_PlacesFragment_to_CreatePlaceFragment);
    }
}