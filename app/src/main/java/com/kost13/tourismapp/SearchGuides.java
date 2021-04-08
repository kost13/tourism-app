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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchGuides#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchGuides extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    RecyclerView profilesView;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SearchGuides() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchGuides.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchGuides newInstance(String param1, String param2) {
        SearchGuides fragment = new SearchGuides();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private void search(String name) {
        Log.d("Search name", name);

        ProfilesAdapter profilesAdapter = new ProfilesAdapter();
        profilesView.setAdapter(profilesAdapter);
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