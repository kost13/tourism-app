package com.kost13.tourismapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;

import java.util.ArrayList;

public class ProfilesAdapter extends RecyclerView.Adapter<ProfilesAdapter.ProfileViewHolder> {

    ChildEventListener childListener;
    Query query;
    private ArrayList<User> users;
    ItemSelectedCallback userClickedCallback;

    public ProfilesAdapter() {
        users = new ArrayList<>();

    }

    public void search(String name) {
        clear();
        childListener = createChildEventListener();
        query = Database.findUserByName(name);
        query.addChildEventListener(childListener);
    }

    public void addOnUserClickListener(ItemSelectedCallback callback){
        userClickedCallback = callback;
    }

    private ChildEventListener createChildEventListener() {
        return new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                User user = snapshot.getValue(User.class);
                user.setId(snapshot.getKey());
                users.add(user);
                notifyItemInserted(users.size() - 1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
    }

    private void clear() {
        if (childListener != null && query != null) {
            query.removeEventListener(childListener);
        }
        users.clear();
    }


    @NonNull
    @Override
    public ProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.profile_row, parent, false);
        return new ProfileViewHolder(itemView);
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

        TextView nameTV;

        public ProfileViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTV = (TextView) itemView.findViewById(R.id.nameTextView);
        }

        public void bind(User user) {
            nameTV.setText(user.getName());
            nameTV.setOnClickListener((View) -> { userClickedCallback.onItemSelected(user.getId()); });
        }
    }
}
