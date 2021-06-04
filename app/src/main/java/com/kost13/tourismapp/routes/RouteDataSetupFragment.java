package com.kost13.tourismapp.routes;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.kost13.tourismapp.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

public class RouteDataSetupFragment extends Fragment {

    private static final int PICTURE_RESULTS = 47;
    private Uri image;
    private EditText textEditTitle;
    private EditText textEditDescription;
    private ImageView imageView;

    public RouteDataSetupFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_route_data_setup, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textEditTitle = view.findViewById(R.id.titleTextEdit);
        textEditDescription = view.findViewById(R.id.descriptionTextMultiLine);
        imageView = view.findViewById(R.id.imageView);

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

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICTURE_RESULTS && resultCode == Activity.RESULT_OK) {
            image = data.getData();
            updateImage();
        }
    }

    public String getTitle() {
        return textEditTitle.getText().toString();
    }

    public RouteBasicData getData() {
        String title = textEditTitle.getText().toString();
        String description = textEditDescription.getText().toString();
        return new RouteBasicData(title, description, image);
    }

    private void updateImage() {
        String path;
        Context context = getContext();
        if (context == null) {
            return;
        }

        int width = Resources.getSystem().getDisplayMetrics().widthPixels;

        RequestCreator creator;

        if (image != null) {
            path = image.toString();
            creator = Picasso.with(getContext()).load(path);
        } else {
            creator = Picasso.with(getContext()).load(R.drawable.ic_image);
        }

        creator.resize(width, 4 * width / 5)
                .centerCrop()
                .into(imageView);
    }
}