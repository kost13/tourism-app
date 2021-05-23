package com.kost13.tourismapp;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
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

import java.util.ArrayList;
import java.util.List;

import io.grpc.okhttp.internal.Util;

public class BuildRouteMapFragment extends Fragment {

    private GoogleMap map;
    private Polyline polyline;
    private View view;

    private TextView textViewLength;
    private TextView textViewPOIs;

    RouteMapViewModel routeMapViewModel;


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

//            Context context = getContext();
//
//            if (context != null && (ActivityCompat.checkSelfPermission(context,
//                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) && (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
//
//                // TODO: Consider calling
//                //    ActivityCompat#requestPermissions
//                // here to request the missing permissions, and then overriding
//                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                //                                          int[] grantResults)
//                // to handle the case where the user grants the permission. See the documentation
//                // for ActivityCompat#requestPermissions for more details.
////                return;
//                LatLng warsaw = new LatLng(52.2297700, 21.0117800);
//                googleMap.moveCamera(CameraUpdateFactory.newLatLng(warsaw));
//            } else {
//                googleMap.setMyLocationEnabled(true);
//            }

            if(routeMapViewModel.getPoints().isEmpty()) {
                LatLng warsaw = new LatLng(52.22987, 21.01199);
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(warsaw));
                googleMap.moveCamera(CameraUpdateFactory.zoomTo(12.0f));
            } else {
                LatLngBounds bounds = routeMapViewModel.getRouteBounds();
                if (bounds != null) {
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 80));
                }
            }

            map = googleMap;

            polyline = map.addPolyline(new PolylineOptions()
                    .color(getResources().getColor(R.color.purple_700, getActivity().getTheme()))
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

        updatePointsOfInterestCount();

        Bundle bundle = new Bundle();
        bundle.putSerializable("poi", poi);

        NavHostFragment.findNavController(BuildRouteMapFragment.this).navigate(R.id.action_BuildRouteMapFragment_to_CreateRoutePoiFragment, bundle);
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
//        if (getArguments() != null) {
//            routeBasicData = (RouteBasicData) getArguments().getSerializable("routeData");
//        }
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
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }

        this.view = view;

        TextView title = view.findViewById(R.id.textViewTitle);
        title.setText(routeMapViewModel.getBasicData().getTitle());

        Button preview = view.findViewById(R.id.buttonPreview);
        preview.setOnClickListener(this::onPreviewClicked);

        Button done = view.findViewById(R.id.buttonDone);
        done.setOnClickListener(this::onDone);

        textViewLength = view.findViewById(R.id.textViewLength);
        textViewLength.setText("0 km");
        textViewPOIs = view.findViewById(R.id.textViewPOIs);
        textViewPOIs.setText("0 POIs");
    }

    private void onPreviewClicked(View view) {
        Bundle bundle = new Bundle();
        bundle.putString("preview", "true");
        NavHostFragment.findNavController(BuildRouteMapFragment.this).navigate(R.id.action_BuildRouteMapFragment_to_MapFragment, bundle);
    }

    private void onDone(View view) {
        routeMapViewModel.commitRouteToDatabase(() -> {
            ((AppCompatActivity) getContext()).getSupportFragmentManager().popBackStack(R.id.FirstFragment, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            NavHostFragment.findNavController(BuildRouteMapFragment.this).navigate(R.id.action_BuildRouteMapFragment_to_FirstFragment);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        final List<PointOfInterest> pois = routeMapViewModel.getPois();
        if(!pois.isEmpty()){
            if(map != null){
                PointOfInterest last = pois.get(pois.size()-1);
                if(last != null){
                    map.addMarker(new MarkerOptions().position(last.getLatLng()).title(last.getTitle()));
                }
            } else {
                SupportMapFragment mapFragment =
                        (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
                if (mapFragment != null) {
                    mapFragment.getMapAsync((GoogleMap map) -> {
                        callback.onMapReady(map);
                        for(PointOfInterest p : pois){
                            map.addMarker(new MarkerOptions().position(p.getLatLng()).title(p.getTitle()));
                        }
                    });
                }
            }
        }
        updateRouteLength();
        updatePointsOfInterestCount();
    }
}