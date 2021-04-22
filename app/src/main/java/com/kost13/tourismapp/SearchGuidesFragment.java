package com.kost13.tourismapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class SearchGuidesFragment extends Fragment {

    RecyclerView profilesView;


    public SearchGuidesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void search(String name) {
        Log.d("Search name", name);

        ProfilesAdapter profilesAdapter = new ProfilesAdapter();
        profilesView.setAdapter(profilesAdapter);
        profilesAdapter.addOnUserClickListener(itemId -> {
            Bundle userId = new Bundle();
            userId.putString("userId", itemId);
            NavHostFragment.findNavController(SearchGuidesFragment.this).navigate(R.id.action_SearchGuidesFragment_to_ProfileFragment, userId);
        });
        profilesAdapter.search(name);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        profilesView = (RecyclerView) view.findViewById(R.id.profilesRecyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        profilesView.setLayoutManager(layoutManager);

        Button searchButton = (Button) view.findViewById(R.id.searchGuideButton);
        EditText searchName = (EditText) view.findViewById(R.id.searchGuideName);

        searchButton.setOnClickListener(view1 -> search(searchName.getText().toString()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_guides, container, false);


    }
}