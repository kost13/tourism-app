package com.kost13.tourismapp.routes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.kost13.tourismapp.R;

public class CreateRouteFragment extends Fragment {

    public CreateRouteFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_route, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button continueButton = view.findViewById(R.id.buttonContinue);
        continueButton.setOnClickListener(this::onContinueClicked);

    }

    private RouteBasicData getRouteInfo() {
        RouteDataSetupFragment fragment = (RouteDataSetupFragment) getChildFragmentManager().findFragmentById(R.id.routeDataSetupFragment);
        return fragment.getData();
    }

    private void onContinueClicked(View view) {
        RouteMapViewModel routeMapViewModel = new ViewModelProvider(requireActivity()).get(RouteMapViewModel.class);
        routeMapViewModel.setBasicData(getRouteInfo());
        NavHostFragment.findNavController(CreateRouteFragment.this).navigate(R.id.action_CreateRouteFragment_to_BuildRouteMapFragment);
    }


}