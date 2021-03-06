package com.kost13.tourismapp.users;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.kost13.tourismapp.Database;
import com.kost13.tourismapp.ItemSelectedCallback;
import com.kost13.tourismapp.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

public class ProfilesAdapter extends RecyclerView.Adapter<ProfilesAdapter.ProfileViewHolder> {

    private final ArrayList<User> users;
    Task<QuerySnapshot> query;
    ItemSelectedCallback userClickedCallback;

    public ProfilesAdapter() {
        users = new ArrayList<>();

    }

    public void search(String value, String key, boolean guidesOnly) {
        clear();
        query = Database.findUsers(value, key);
        query.addOnCompleteListener(task -> {
            if(!task.isSuccessful()){
                return;
            }
            users.clear();
            for (QueryDocumentSnapshot document : task.getResult()) {
                User user = document.toObject(User.class);
                user.setId(document.getId());
                if(!guidesOnly || user.getIsGuide()){
                    users.add(user);
                }
//                notifyItemInserted(users.size() - 1);
            }
            notifyItemRangeInserted(0, users.size());
        });
    }

    public void addOnUserClickListener(ItemSelectedCallback callback) {
        userClickedCallback = callback;
    }

    public void clear() {
        int s = users.size();
        users.clear();
        notifyItemRangeRemoved(0, s);
    }


    @NonNull
    @Override
    public ProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.profile_row, parent, false);
        return new ProfileViewHolder(context, itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileViewHolder holder, int position) {
        User user = users.get(position);
        holder.bind(user);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ProfileViewHolder extends RecyclerView.ViewHolder {

        private static final int MAX_DESC_LEN = 78;
        private final int IMG_SIZE = 4 * Resources.getSystem().getDisplayMetrics().widthPixels / 10;
        TextView nameTV;
        TextView locationTV;
        TextView bioTV;
        ImageView imgView;
        Context context;


        public ProfileViewHolder(Context context, @NonNull View itemView) {
            super(itemView);
            this.context = context;
            nameTV = itemView.findViewById(R.id.nameTextView);
            locationTV = itemView.findViewById(R.id.locationTextView);
            bioTV = itemView.findViewById(R.id.textViewBio);
            imgView = itemView.findViewById(R.id.profileImageView);
        }

        public void bind(User user) {
            nameTV.setText(user.getName());
            locationTV.setText(user.getLocation());
            bioTV.setText(chopString(user.getBio()));
            setProfileImage(imgView, user.getProfileImageUrl());
            nameTV.setOnClickListener((View) -> userClickedCallback.onItemSelected(user.getId()));
        }

        private String chopString(String str) {
            if (str.length() <= MAX_DESC_LEN) {
                return str;
            }
            return str.substring(0, MAX_DESC_LEN) + "...";
        }

        private void setProfileImage(ImageView view, String url) {
            RequestCreator rc;
            if (url != null && !url.isEmpty()) {
                rc = Picasso.with(context).load(url);
            } else {
                rc = Picasso.with(context).load(R.drawable.profile);
            }
            rc.resize(IMG_SIZE, IMG_SIZE)
                    .centerCrop()
                    .into(view);
        }
    }
}
