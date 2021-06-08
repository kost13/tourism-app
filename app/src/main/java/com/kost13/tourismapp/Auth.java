package com.kost13.tourismapp;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.kost13.tourismapp.users.User;

import java.util.Arrays;
import java.util.List;

public class Auth {
    private static final int RC_SIGN_IN = 123;
    private static FirebaseAuth firebaseAuth;
    private static FirebaseAuth.AuthStateListener firebaseAuthListener;
    private static User user;

    private static void signIn(AppCompatActivity caller) {
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());
        user = null;

        // Create and launch sign-in intent
        caller.startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers).setLogo(R.drawable.ic_iconfinder_achievement_6843078).setTheme(R.style.Theme_TourismApp)
                        .build(),
                RC_SIGN_IN);
    }

    public static int signInCode() {
        return RC_SIGN_IN;
    }

    public static void verifyAuth(AppCompatActivity caller) {
        if (firebaseAuth == null) {
            firebaseAuth = FirebaseAuth.getInstance();
            firebaseAuthListener = firebaseAuth -> {
                if (firebaseAuth.getCurrentUser() == null) {
                    signIn(caller);
                }
            };
        }
    }

    public static String getCurrentUser() {
        return firebaseAuth.getUid();
    }

    public static User getCurrentUserObject() {
        return user;
    }


    public static void downloadUser(OnDataReadyCallback callback) {
        if(user != null){
            callback.onDataReady();
        }
        if(firebaseAuth == null || getCurrentUser() == null){
            return;
        }
        Database.getUsersDb().document(getCurrentUser()).get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e("firebase", "Error getting data", task.getException());
            } else {
                user = task.getResult().toObject(User.class);
                if (user != null) {
                    user.setId(getCurrentUser());
                    callback.onDataReady();
                }
            }
        });
    }


    public static void attachListener() {
        firebaseAuth.addAuthStateListener(firebaseAuthListener);
    }

    public static void detachListener() {
        firebaseAuth.removeAuthStateListener(firebaseAuthListener);
    }

    public static void commitNewUser(String email, String name){
        User newUser = new User();
        newUser.setName(name);
        newUser.setEmail(email);
        Database.getUsersDb().document(getCurrentUser()).get().addOnCompleteListener(
                task -> {
                    if(!task.getResult().exists()){
                        Database.getUsersDb().document(getCurrentUser()).set(newUser);
                    }
                }
        );
    }

}
