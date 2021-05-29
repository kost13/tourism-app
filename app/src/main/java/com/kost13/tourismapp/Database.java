package com.kost13.tourismapp;

import android.net.Uri;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Database {

    private static final String DB_USERS = "users";
    private static final String DB_ROUTES = "routes";
    private static final String DB_ROUTE_POIS = "route_pois";

    private static final String STORAGE_PROFILE = "profile_images";
    private static final String STORAGE_ROUTES = "route_images";

    private static FirebaseDatabase firebaseDatabase;
    private static FirebaseFirestore firestore;
    private static StorageReference storage;

    private static DatabaseReference openDatabaseReference(String reference) {
        if (firebaseDatabase == null) {
            firebaseDatabase = FirebaseDatabase.getInstance();
        }

        return firebaseDatabase.getReference().child(reference);
    }

    private static CollectionReference openFirestoreReference(String reference) {
        if (firestore == null) {
            firestore = FirebaseFirestore.getInstance();
        }
        return firestore.collection(reference);
    }

    public static DatabaseReference getUsersDb() {
        return openDatabaseReference(DB_USERS);
    }

    public static CollectionReference getRoutesDb() {
        return openFirestoreReference(DB_ROUTES);
    }

    public static CollectionReference getRoutePoisDb() {
        return openFirestoreReference(DB_ROUTE_POIS);
    }

    public static Query findUserByName(String name) {
        name = name.trim();
        if (name.isEmpty()) {
            // all users
            return getUsersDb();
        }
        return getUsersDb().orderByChild("name").startAt(name).endAt(name + "\uf8ff");

    }

    private static StorageReference getStorage(String folderName){
        if(storage == null){
            storage = FirebaseStorage.getInstance().getReference();
        }
        return storage.child(folderName);
    }

    public static StorageReference getRouteImageStorage(){
        return getStorage(STORAGE_ROUTES);
    }

    public static StorageReference getProfileImageStorage(){
        return getStorage(STORAGE_PROFILE);
    }

    public static void saveImage(Uri image, ImageSavedCallback callback){
        if(image == null){
            callback.imageSaved(null);
            return;
        }

        int uriHash = image.hashCode();
        String user = Auth.getCurrentUser();
        String imageId = user + "_" + uriHash;
        StorageReference ref = getRouteImageStorage().child(imageId);
        ref.putFile(image).addOnSuccessListener(taskSnapshot -> {
            if (taskSnapshot.getMetadata() != null) {
                if (taskSnapshot.getMetadata().getReference() != null) {
                    Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                    result.addOnSuccessListener(uri -> callback.imageSaved(uri.toString()));
                }
            }
        });
    }
}
