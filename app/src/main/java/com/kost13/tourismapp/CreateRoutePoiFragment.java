package com.kost13.tourismapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class CreateRoutePoiFragment extends Fragment {

    PointOfInterest poi;

    public CreateRoutePoiFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_route_poi, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            poi = (PointOfInterest) getArguments().getSerializable("poi");
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button continueButton = view.findViewById(R.id.buttonDone);
        continueButton.setOnClickListener(this::onDoneClicked);
    }

    private void onDoneClicked(View view){

        RouteDataSetupFragment fragment = (RouteDataSetupFragment) getChildFragmentManager().findFragmentById(R.id.routeDataSetupFragment);
        RouteBasicData data = fragment.getData();

        poi.setTitle(data.getTitle());
        poi.setDescription(data.getDescription());
        poi.setImageUri(data.getImageUri());

        NavHostFragment.findNavController(CreateRoutePoiFragment.this).popBackStack();
    }
}