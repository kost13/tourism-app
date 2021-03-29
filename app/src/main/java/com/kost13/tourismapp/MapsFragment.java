package com.kost13.tourismapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.graphics.Point;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

public class MapsFragment extends Fragment {

    private MapsViewModel mapsViewModel;
    private GoogleMap googleMap;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mapsViewModel = new MapsViewModel();
    }

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
            LatLng initialLocation = new LatLng(52.207144, 21.030708);
//            googleMap.addMarker(new MarkerOptions().position(initialLocation));
            googleMap.getUiSettings().setCompassEnabled(true);
            googleMap.getUiSettings().setZoomControlsEnabled(true);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(initialLocation, 16));
            setNewMap(googleMap);
        }
    };

    private OnDataReadyCallback dataCallback = dataId -> {
        if(dataId == MapsViewModel.DATA_ID_ROUTES){
            tryAddRoutePolygon();
        }
    };

    private void setNewMap(GoogleMap googleMap){
        this.googleMap = googleMap;
        tryAddRoutePolygon();
    }

    private void tryAddRoutePolygon(){
        if(googleMap == null){
            return;
        }

        ArrayList<LatLng> routes = mapsViewModel.getRoutes();
        if(routes == null){
            return;
        }

        addRouteToMap(routes, googleMap);
    }

    private void addRouteToMap(ArrayList<LatLng> route, GoogleMap map){
        map.addPolyline(new PolylineOptions()
                .color(getResources().getColor(R.color.teal_200, getActivity().getTheme()))
                .width(12)
                .addAll(route));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }

        mapsViewModel.downloadRoutes(dataCallback);
    }
}