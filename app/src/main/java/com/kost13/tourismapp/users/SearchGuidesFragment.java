package com.kost13.tourismapp.users;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kost13.tourismapp.R;


public class SearchGuidesFragment extends Fragment {

    RecyclerView profilesView;
    ProfilesAdapter profilesAdapter;


    public SearchGuidesFragment() {
        profilesAdapter = new ProfilesAdapter();
        profilesAdapter.addOnUserClickListener(itemId -> {
            Bundle userId = new Bundle();
            userId.putString("userId", itemId);
            NavHostFragment.findNavController(SearchGuidesFragment.this).navigate(R.id.action_SearchGuidesFragment_to_ProfileFragment, userId);
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        profilesView = view.findViewById(R.id.profilesRecyclerView);
        profilesView.setAdapter(profilesAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        profilesView.setLayoutManager(layoutManager);

        Button searchButton = view.findViewById(R.id.searchGuideButton);
        EditText searchBar = view.findViewById(R.id.searchGuideName);
        RadioButton locationSearch = view.findViewById(R.id.radioButtonLocation);
        Switch guidesOnly = view.findViewById(R.id.switchGuidesOnly);

        searchButton.setOnClickListener(view1 ->{
            String key = locationSearch.isChecked() ? "location" : "name";
            profilesAdapter.search(searchBar.getText().toString(), key, guidesOnly.isChecked());
        });

        Button clearButton = view.findViewById(R.id.clearButton);
        clearButton.setOnClickListener(view1 -> {
            searchBar.setText("");
            profilesAdapter.clear();
            profilesView.removeAllViews();
            profilesView.refreshDrawableState();
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_search_guides, container, false);
    }
}