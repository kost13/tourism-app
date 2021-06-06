package com.kost13.tourismapp.places;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.kost13.tourismapp.R;
import com.kost13.tourismapp.Utils;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class PlaceViewFragment extends Fragment {


    private String placeId;
    private PlacesViewModel viewModel;
    private View currentView;
    private GoogleMap map;

    public static final String PLACE_ID = "placeId";

    public PlaceViewFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            setup(getArguments().getString(PLACE_ID));
        }
    }

    public void setup(String placeId) {
        this.placeId = placeId;
        viewModel = new PlacesViewModel(placeId);
        viewModel.downloadPlace(() -> {
            viewModel.downloadUser(this::setupView);
        }
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_place_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        currentView = view;
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync((GoogleMap googleMap) -> {
                map = googleMap;
                tryAddMarker();
            });
        }

        ImageButton closeButton = view.findViewById(R.id.button_close);
        closeButton.setOnClickListener(this::close);

        setupView();
    }

    private void close(View v) {
        try {
            NavHostFragment.findNavController(PlaceViewFragment.this).popBackStack();
        } catch (IllegalStateException e){
            System.exit(0);
        }
    }

    private void setupView() {
        if (currentView == null) {
            return;
        }

        Place place = viewModel.getPlace();
        if (place == null) {
            return;
        }

        TextView title = currentView.findViewById(R.id.placeTitle);
        title.setText(place.getTitle());

        TextView description = currentView.findViewById(R.id.routeDescription);
        description.setText(place.getDescription());

        TextView author = currentView.findViewById(R.id.textViewAuthor);
        author.setText(viewModel.getUserName());

        ImageView img = currentView.findViewById(R.id.routeImageView);
        if (place.getImage() != null && !place.getImage().isEmpty()) {
            setPlaceImage(img, place.getImage());
        } else {
            img.setVisibility(View.GONE);
        }

        ImageButton shareBtn = currentView.findViewById(R.id.button_share);
        shareBtn.setOnClickListener(this::share);

        tryAddMarker();

    }

    private void share(View v){
        String url = Utils.sharePath(viewModel.getPlace().getId());
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
        intent.putExtra(Intent.EXTRA_TEXT, url);
        startActivity(Intent.createChooser(intent, "Share this place"));
    }

    private void setPlaceImage(ImageView view, String url) {
        int width = Resources.getSystem().getDisplayMetrics().widthPixels;

        if (url != null && !url.isEmpty()) {
            Picasso.with(getContext()).load(url).resize(width, width)
                    .centerCrop()
                    .into(view);
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }

    private void tryAddMarker() {
        Place place = viewModel.getPlace();
        if (place == null || map == null) {
            return;
        }

        LatLng pos = Utils.geoPointToLatLng(place.getLoc());
        map.moveCamera(CameraUpdateFactory.zoomTo(13.0f));
        map.moveCamera(CameraUpdateFactory.newLatLng(pos));
        map.addMarker(new MarkerOptions().position(pos).title(place.getTitle()));
    }
}