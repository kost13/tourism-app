package com.kost13.tourismapp.places;

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
import android.widget.Toast;

import com.kost13.tourismapp.R;
import com.kost13.tourismapp.routes.RouteBasicData;
import com.kost13.tourismapp.routes.RouteDataSetupFragment;

public class CreatePlaceFragment extends Fragment {

    public CreatePlaceFragment() {}


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_place, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button saveButton = view.findViewById(R.id.buttonSave);
        saveButton.setOnClickListener(this::saveRoute);

        Button cancelButton = view.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(this::cancel);
    }

    private void saveRoute(View view){
        Toast.makeText(getContext(), "Saving place...", Toast.LENGTH_LONG).show();
        PlacesViewModel placesViewModel = new ViewModelProvider(requireActivity()).get(PlacesViewModel.class);
        placesViewModel.setBasicData(getPlaceInfo());
        placesViewModel.commitPlaceToDatabase(() -> {
            Toast.makeText(getContext(), "Place added", Toast.LENGTH_SHORT).show();
            close(view);
        });
    }

    private void cancel(View view){
        PlacesViewModel placesViewModel = new ViewModelProvider(requireActivity()).get(PlacesViewModel.class);
        placesViewModel.setPosition(null);
        placesViewModel.setBasicData(null);
        close(view);
    }

    private void close(View view){
        NavHostFragment.findNavController(CreatePlaceFragment.this).popBackStack();
    }

    private RouteBasicData getPlaceInfo(){
        RouteDataSetupFragment fragment = (RouteDataSetupFragment) getChildFragmentManager().findFragmentById(R.id.routeDataSetupFragment);
        return fragment.getData();
    }
}