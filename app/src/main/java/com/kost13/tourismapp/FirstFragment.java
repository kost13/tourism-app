package com.kost13.tourismapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.kost13.tourismapp.users.ProfileFragment;
import com.kost13.tourismapp.users.User;

public class FirstFragment extends Fragment {

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_main, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.myProfileButton).setOnClickListener(v -> openProfile());
        view.findViewById(R.id.searchGuidesButton).setOnClickListener(v -> navigate(R.id.action_FirstFragment_to_SearchGuidesFragment));
        view.findViewById(R.id.routeButton).setOnClickListener(v -> navigate(R.id.action_FirstFragment_to_CreateRouteFragment));
        view.findViewById(R.id.placeButton).setOnClickListener(v -> navigate(R.id.action_FirstFragment_to_PlacesFragment));


        Auth.downloadUser(() -> {
            User user = Auth.getCurrentUserObject();
            if(user != null){
                int visibility = user.getIsGuide() ? View.VISIBLE : View.GONE;
                view.findViewById(R.id.routeButton).setVisibility(visibility);
            }
        });

    }

    private void navigate(int actionId){
        NavHostFragment.findNavController(FirstFragment.this).navigate(actionId);
    }

    private void openProfile(){
        Bundle bundle = new Bundle();
        bundle.putString(ProfileFragment.USER_ID, Auth.getCurrentUser());
        NavHostFragment.findNavController(FirstFragment.this).navigate(R.id.action_FirstFragment_to_ProfileFragment, bundle);
    }
}