package com.kost13.tourismapp.maps;

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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateRouteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateRouteFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public CreateRouteFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateRouteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateRouteFragment newInstance(String param1, String param2) {
        CreateRouteFragment fragment = new CreateRouteFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_route, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button continueButton = view.findViewById(R.id.buttonContinue);
        continueButton.setOnClickListener(this::onContinueClicked);

    }

    private RouteBasicData getRouteInfo(){
        RouteDataSetupFragment fragment = (RouteDataSetupFragment) getChildFragmentManager().findFragmentById(R.id.routeDataSetupFragment);
        return fragment.getData();
    }

    private void onContinueClicked(View view) {
        RouteMapViewModel routeMapViewModel = new ViewModelProvider(requireActivity()).get(RouteMapViewModel.class);
        routeMapViewModel.setBasicData(getRouteInfo());

        //TODO verify fileds and save route data
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("routeData", getRouteInfo());
        NavHostFragment.findNavController(CreateRouteFragment.this).navigate(R.id.action_CreateRouteFragment_to_BuildRouteMapFragment);
    }


}