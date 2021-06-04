package com.kost13.tourismapp.users;

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

import com.kost13.tourismapp.R;


public class SearchGuidesFragment extends Fragment {

    RecyclerView profilesView;


    public SearchGuidesFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void search(String name, String location) {
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

        profilesView = view.findViewById(R.id.profilesRecyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        profilesView.setLayoutManager(layoutManager);

        Button searchButton = view.findViewById(R.id.searchGuideButton);
        EditText searchName = view.findViewById(R.id.searchGuideName);
        EditText searchLocation = view.findViewById(R.id.searchGuideLocation);

        searchButton.setOnClickListener(view1 -> search(searchName.getText().toString(), searchLocation.getText().toString()));

        Button clearButton = view.findViewById(R.id.clearButton);
        clearButton.setOnClickListener(view1 -> {
            searchName.setText("");
            searchLocation.setText("");
            profilesView.setAdapter(new ProfilesAdapter());
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_search_guides, container, false);
    }
}