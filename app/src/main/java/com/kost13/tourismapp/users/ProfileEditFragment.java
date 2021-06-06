package com.kost13.tourismapp.users;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.kost13.tourismapp.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

public class ProfileEditFragment extends Fragment {

    private static final int PICTURE_RESULTS = 47;
    private Uri image;
    private ProfileViewModel viewModel;
    private View currentView;

    public ProfileEditFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(ProfileViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_edit, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        currentView = view;
        User user = viewModel.getUser();
        if (user != null) {
            fillFields(user);
        }

        Button upload = view.findViewById(R.id.uploadButton);
        upload.setOnClickListener(view1 -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/jpeg");
            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
            startActivityForResult(intent.createChooser(intent, "Insert Picture"), PICTURE_RESULTS);
        });

        Button clear = view.findViewById(R.id.clearButton);
        clear.setOnClickListener(view1 -> {
            image = null;
            updateImage();
        });

        Button cancel = view.findViewById(R.id.cancelButton);
        cancel.setOnClickListener(this::close);

        Button save = view.findViewById(R.id.saveButton);
        save.setOnClickListener(this::saveData);

    }

    private void fillFields(User user) {

        EditText nameView = currentView.findViewById(R.id.nameTextView);
        nameView.setText(user.getName());

        EditText bioView = currentView.findViewById(R.id.profileBio);
        bioView.setText(user.getBio());

        EditText locations = currentView.findViewById(R.id.locationsTextView);
        locations.setText(user.getLocation());

        EditText phone = currentView.findViewById(R.id.phoneValue);
        phone.setText(user.getTelephone());

        EditText email = currentView.findViewById(R.id.emailValue);
        email.setText(user.getEmail());

        EditText webpage = currentView.findViewById(R.id.webpageValue);
        webpage.setText(user.getWebpage());

        showProfileImage(user.getProfileImageUrl());

        SwitchMaterial guideSwitch = currentView.findViewById(R.id.switchGuide);
        guideSwitch.setChecked(user.getIsGuide());

    }

    private void saveDataFromFileds(User user){
        EditText nameView = currentView.findViewById(R.id.nameTextView);
        user.setName(nameView.getText().toString());

        EditText bioView = currentView.findViewById(R.id.profileBio);
        user.setBio(bioView.getText().toString());

        EditText locations = currentView.findViewById(R.id.locationsTextView);
        user.setLocation(locations.getText().toString());

        EditText phone = currentView.findViewById(R.id.phoneValue);
        user.setTelephone(phone.getText().toString());

        EditText email = currentView.findViewById(R.id.emailValue);
        user.setEmail(email.getText().toString());

        EditText webpage = currentView.findViewById(R.id.webpageValue);
        user.setWebpage(webpage.getText().toString());

        SwitchMaterial guideSwitch = currentView.findViewById(R.id.switchGuide);
        user.setIsGuide(guideSwitch.isChecked());

        user.setImage(image);
    }

    private void saveData(View view){
        User user = viewModel.getUser();

        saveDataFromFileds(user);

        viewModel.setUser(user);
        viewModel.commitUser(() -> {
            Toast.makeText(getContext(), "Profile updated", Toast.LENGTH_LONG).show();
            close(view);
        });
    }

    private void close(View view){
        NavHostFragment.findNavController(ProfileEditFragment.this).popBackStack();
    }

    private void showProfileImage(String url) {
        if (url != null && !url.isEmpty()) {
            ImageView imageView = (ImageView) currentView.findViewById(R.id.profileImageView);
            int width = Resources.getSystem().getDisplayMetrics().widthPixels;
            int size = 4 * width / 10;
            Picasso.with(getContext()).load(url)
                    .resize(size, size)
                    .centerCrop()
                    .into(imageView);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICTURE_RESULTS && resultCode == Activity.RESULT_OK) {
            image = data.getData();
            updateImage();
        }
    }

    private void updateImage() {
        String path;
        Context context = getContext();
        if (context == null) {
            return;
        }

        int width = Resources.getSystem().getDisplayMetrics().widthPixels;
        int size = (int) (0.4 * width);

        RequestCreator creator;

        ImageView imageView = currentView.findViewById(R.id.profileImageView);

        if (image != null) {
            path = image.toString();
            creator = Picasso.with(getContext()).load(path);
        } else {
            creator = Picasso.with(getContext()).load(R.drawable.profile);
        }

        creator.resize(size, size)
                .centerCrop()
                .into(imageView);

    }
}
