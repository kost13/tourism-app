package com.kost13.tourismapp;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.kost13.tourismapp.users.User;

public class Database {

    private static final String DB_USERS = "users";
    private static final String DB_ROUTES = "routes";
    private static final String DB_PLACES = "places";
    private static final String DB_ROUTE_POIS = "route_pois";

    private static final String STORAGE_PROFILE = "profile_images";
    private static final String STORAGE_ROUTES = "route_images";
    private static final String STORAGE_PLACES = "place_images";
    private static final int MAX_USERS = 50;
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

    public static CollectionReference getUsersDb() {
        return openFirestoreReference(DB_USERS);
    }

    public static CollectionReference getRoutesDb() {
        return openFirestoreReference(DB_ROUTES);
    }

    public static CollectionReference getPlacesDb() {
        return openFirestoreReference(DB_PLACES);
    }

    public static CollectionReference getRoutePoisDb() {
        return openFirestoreReference(DB_ROUTE_POIS);
    }

    public static Task<QuerySnapshot> findUsers(String value, String key) {
        value = value.trim();
        return getUsersDb().whereGreaterThanOrEqualTo(key, value).whereLessThanOrEqualTo(key, value + "\uf8ff").limit(MAX_USERS).get();
    }

    private static StorageReference getStorage(String folderName) {
        if (storage == null) {
            storage = FirebaseStorage.getInstance().getReference();
        }
        return storage.child(folderName);
    }

    public static void saveRouteImage(Uri image, ImageSavedCallback callback) {
        saveImage(image, callback, STORAGE_ROUTES);
    }

    public static void saveProfileImage(Uri image, ImageSavedCallback callback) {
        saveImage(image, callback, STORAGE_PROFILE);
    }

    public static void savePlaceImage(Uri image, ImageSavedCallback callback) {
        saveImage(image, callback, STORAGE_PLACES);
    }

    private static void saveImage(Uri image, ImageSavedCallback callback, String folderName) {
        if (image == null) {
            callback.imageSaved(null);
            return;
        }

        int uriHash = image.hashCode();
        String user = Auth.getCurrentUser();
        String imageId = user + "_" + uriHash;
        StorageReference ref = getStorage(folderName).child(imageId);
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
