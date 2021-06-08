package com.kost13.tourismapp.routes;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.firestore.GeoPoint;
import com.kost13.tourismapp.R;
import com.kost13.tourismapp.Utils;
import com.kost13.tourismapp.users.ProfileEditFragment;

import java.util.List;

public class BuildRouteMapFragment extends Fragment {

    private GoogleMap map;
    private Polyline polyline;
    private View view;

    private TextView textViewLength;
    private TextView textViewPOIs;

    RouteMapViewModel routeMapViewModel;


    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {

            if(routeMapViewModel.getPoints().isEmpty()) {
                LatLng warsaw = new LatLng(52.22987, 21.01199);
                googleMap.moveCamera(CameraUpdateFactory.zoomTo(12.0f));
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(warsaw));
            } else {
                LatLngBounds bounds = routeMapViewModel.getRouteBounds();
                if (bounds != null) {
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 80));
                }
            }

            map = googleMap;

            polyline = map.addPolyline(new PolylineOptions()
                    .color(getResources().getColor(R.color.primary_color, getActivity().getTheme()))
                    .width(12));

            googleMap.setOnMapLongClickListener(latLng -> addPoint(latLng));


        }
    };

    private void addPoint(LatLng latLng){

        String[] options = {"Normal Point", "Point of Interest", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Add point");
        builder.setItems(options, (dialog, which) -> {
            switch (which){
                case 0: addNormalPoint(latLng); break;
                case 1: addPointOfInterest(latLng); break;
                default: break;
            }
        });
        builder.show();
    }

    private void addNormalPoint(LatLng latLng){
        routeMapViewModel.getPoints().add(latLng);
        polyline.setPoints(routeMapViewModel.getPoints());
        updateRouteLength();
    }

    private void addPointOfInterest(LatLng latLng){
        addNormalPoint(latLng);

        PointOfInterest poi = createNewPOI(latLng);
        routeMapViewModel.getPois().add(poi);


        Handler handler = new Handler();
        handler.post(() -> NavHostFragment.findNavController(BuildRouteMapFragment.this).navigate(R.id.action_BuildRouteMapFragment_to_CreateRoutePoiFragment));

//        NavHostFragment.findNavController(BuildRouteMapFragment.this).navigate(R.id.action_BuildRouteMapFragment_to_CreateRoutePoiFragment);
    }

    private PointOfInterest createNewPOI(LatLng pos){
        final PointOfInterest poi = new PointOfInterest();
        poi.setLoc(new GeoPoint(pos.latitude, pos.longitude));

        //TODO add GeoHash
        return poi;
    }

    private void updateRouteLength(){
        textViewLength.setText(String.format("%1$,.3f km", Utils.routeLength(routeMapViewModel.getPoints())));
    }

    private void updatePointsOfInterestCount(){
        textViewPOIs.setText(routeMapViewModel.getPois().size() + " POIs");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        routeMapViewModel = new ViewModelProvider(requireActivity()).get(RouteMapViewModel.class);
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
        if(map == null){
            SupportMapFragment mapFragment =
                    (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
            if (mapFragment != null) {
                mapFragment.getMapAsync(callback);
            }
        }

        this.view = view;

        TextView title = view.findViewById(R.id.textViewTitle);
        title.setText(routeMapViewModel.getBasicData().getTitle());

        Button preview = view.findViewById(R.id.buttonPreview);
        preview.setOnClickListener(this::onPreviewClicked);

        Button done = view.findViewById(R.id.buttonDone);
        done.setOnClickListener(this::onDone);

        textViewLength = view.findViewById(R.id.textViewLength);
//        textViewLength.setText("0 km");
        textViewPOIs = view.findViewById(R.id.textViewPOIs);
//        textViewPOIs.setText("0 POIs");

        final List<PointOfInterest> pois = routeMapViewModel.getPois();
        if(!pois.isEmpty() && map != null){
            PointOfInterest last = pois.get(pois.size()-1);
            if(last != null){
                map.addMarker(new MarkerOptions().position(last.getLatLng()).title(last.getTitle()));
            }
        }
        updateRouteLength();
        updatePointsOfInterestCount();
    }

    private void onPreviewClicked(View view) {
        Bundle bundle = new Bundle();
        bundle.putString("preview", "true");
        NavHostFragment.findNavController(BuildRouteMapFragment.this).navigate(R.id.action_BuildRouteMapFragment_to_MapFragment, bundle);

    }

    private void onDone(View view) {
        Toast.makeText(getContext(), "Saving route...", Toast.LENGTH_LONG).show();
        routeMapViewModel.commitRouteToDatabase(() -> {
            Toast.makeText(getContext(), "Route added", Toast.LENGTH_SHORT).show();
            NavHostFragment.findNavController(BuildRouteMapFragment.this).popBackStack(R.id.FirstFragment, false);
        });
    }
}