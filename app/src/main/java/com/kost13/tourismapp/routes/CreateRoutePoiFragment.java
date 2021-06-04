package com.kost13.tourismapp.routes;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.kost13.tourismapp.R;

import java.util.List;

public class CreateRoutePoiFragment extends Fragment {

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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button continueButton = view.findViewById(R.id.buttonDone);
        continueButton.setOnClickListener(this::onDoneClicked);
    }

    private void onDoneClicked(View view){

        RouteDataSetupFragment fragment = (RouteDataSetupFragment) getChildFragmentManager().findFragmentById(R.id.routeDataSetupFragment);
        RouteBasicData data = fragment.getData();

        RouteMapViewModel routeMapViewModel = new ViewModelProvider(requireActivity()).get(RouteMapViewModel.class);
        List<PointOfInterest> pois = routeMapViewModel.getPois();
        if(!pois.isEmpty()){
            PointOfInterest poi = pois.get(pois.size()-1);
            poi.setTitle(data.getTitle());
            poi.setDescription(data.getDescription());
            poi.setImageUri(data.getImageUri());
        }

        NavHostFragment.findNavController(CreateRoutePoiFragment.this).popBackStack();
    }
}