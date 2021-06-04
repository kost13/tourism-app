package com.kost13.tourismapp.routes;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.kost13.tourismapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MapsFragment extends Fragment {

    private MapsViewModel mapsViewModel;
    private GoogleMap googleMap;
    private boolean routeDetailsVisible;
    private String routeId;
    private View currentView;
    boolean mapPreview = false;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mapsViewModel = new MapsViewModel();
        routeDetailsVisible = true;
        // TODO pass to fragment

        if (getArguments() != null) {
            String preview = getArguments().getString("preview");
            mapPreview = preview != null;

            if(mapPreview){
                RouteMapViewModel routeMapViewModel = new ViewModelProvider(requireActivity()).get(RouteMapViewModel.class);
                mapsViewModel.setRouteMapViewModel(routeMapViewModel);
            } else {
                String route = getArguments().getString("routeId");
                if(route != null){
                    routeId = route;
                } else {
                    routeId = "jRP5OOxRLrr51zQxcGen";
                }
                Log.d("routes fragmnet", "routeId " + routeId);
            }
        } else {
            routeId = "jRP5OOxRLrr51zQxcGen";
        }
    }

    private void setNewMap(GoogleMap googleMap) {
        this.googleMap = googleMap;
        tryAddRoutePolygon();
        tryAddPois();
    }

    private void tryAddRoutePolygon() {
        if (googleMap == null) {
            return;
        }

        Route route = mapsViewModel.getRoute();
        if (route == null) {
            return;
        }

        zoomMapToRoute();
        setRouteData(route);
        addRouteToMap(route, googleMap);
    }

    private void tryAddPois() {
        if (googleMap == null) {
            return;
        }

        List<PointOfInterest> pois = mapsViewModel.getPois();
        if (pois == null) {
            return;
        }


        setPoisData(pois);
        addPoisToMap(pois, googleMap);
    }

    private void zoomMapToRoute() {
        LatLngBounds bounds = mapsViewModel.getRouteBounds();
        if (bounds != null) {
            googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 80));
        }
    }

    private void setRouteData(Route route) {
        TextView title = currentView.findViewById(R.id.routeTitle);
        title.setText(route.getTitle());

        TextView length = currentView.findViewById(R.id.routeLength);
        length.setText(String.format("%1$,.3f km", route.computeLength()));

        TextView pois = currentView.findViewById(R.id.routePois);
        pois.setText(route.getPoisNumber() + " POIs");

        TextView description = currentView.findViewById(R.id.routeDescription);
        description.setText(route.getDescription());

        String url = route.getImage();

        if(url != null){
            ImageView routeImage = currentView.findViewById(R.id.routeImageView);
            setPoiImage(routeImage, url);
        }
    }

    private void addRouteToMap(Route route, GoogleMap map) {
        List<LatLng> points = route.generatePointCoordinates();

        map.addPolyline(new PolylineOptions()
                .color(getResources().getColor(R.color.teal_200, getActivity().getTheme()))
                .width(12)
                .addAll(points));
    }

    private void setPoisData(List<PointOfInterest> pois) {

        LinearLayout layout = currentView.findViewById(R.id.routePoisLayout);

        for (PointOfInterest poi : pois) {
            View itemView = LayoutInflater.from(getContext()).inflate(R.layout.route_poi_row, null, false);
            setupPoiView(itemView, poi);
            layout.addView(itemView);
        }
    }

    private void setupPoiView(View view, PointOfInterest poi) {
        TextView title = view.findViewById(R.id.titleTextView);
        title.setText(poi.getTitle());
        title.setOnClickListener(view1 -> centerMapOn(poi.getLatLng()));

        TextView description = view.findViewById(R.id.poiDescriptionTextView);
        description.setText(poi.getDescription());

        ImageView img = view.findViewById(R.id.poiImageView);
        if (poi.getImage() != null && !poi.getImage().isEmpty()) {
            setPoiImage(img, poi.getImage());
        } else {
            img.setVisibility(View.GONE);
        }
    }

    private void centerMapOn(LatLng position) {
        if (googleMap != null) {
            googleMap.animateCamera(CameraUpdateFactory.newLatLng(position));
        }
    }

    private void setPoiImage(ImageView view, String url) {
        if (url != null && !url.isEmpty()) {
            int width = Resources.getSystem().getDisplayMetrics().widthPixels;
            int size = 4 * width / 10;
            Picasso.with(getContext()).load(url)
                    .resize(size, size)
                    .centerCrop()
                    .into(view);
        }
    }

    private void addPoisToMap(List<PointOfInterest> pois, GoogleMap map) {
        for (PointOfInterest poi : pois) {
            map.addMarker(new MarkerOptions().position(poi.getLatLng()).title(poi.getTitle()));
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    private void setupRouteDetails() {
        if (currentView == null) {
            return;
        }
        ImageButton button = (ImageButton) currentView.findViewById(R.id.showRouteDetailsButton);
        button.setOnClickListener((View.OnClickListener) v -> {
            ScrollView scrollView = (ScrollView) currentView.findViewById(R.id.routeDetailsScrollView);
            if (routeDetailsVisible) {
                scrollView.setVisibility(View.GONE);
                button.setImageResource(R.drawable.ic_baseline_arrow_drop_up_24);
            } else {
                scrollView.setVisibility(currentView.getVisibility());
                button.setImageResource(R.drawable.ic_baseline_arrow_drop_down_24);
            }
            routeDetailsVisible = !routeDetailsVisible;

        });
    }

    private void setupControls(){
        ImageButton close = currentView.findViewById(R.id.button_close);
        close.setOnClickListener((View) -> NavHostFragment.findNavController(MapsFragment.this).popBackStack());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync((map -> {
                map.getUiSettings().setCompassEnabled(true);
                map.getUiSettings().setZoomControlsEnabled(true);
                setNewMap(map);
            }));
        }

        currentView = view;
        setupRouteDetails();
        setupControls();


        if(!mapPreview){
            mapsViewModel.downloadRoute(routeId, this::tryAddRoutePolygon);
            mapsViewModel.downloadPois(routeId, this::tryAddPois);
        }

    }
}