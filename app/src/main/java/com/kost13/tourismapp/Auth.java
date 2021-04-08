package com.kost13.tourismapp;

import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;
import java.util.List;

public class Auth {
    private static final int RC_SIGN_IN = 123;
    private static FirebaseAuth firebaseAuth;
    private static FirebaseAuth.AuthStateListener firebaseAuthListener;

    private static void signIn(AppCompatActivity caller) {
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());

        // Create and launch sign-in intent
        caller.startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
    }

    public static final int signInCode() {
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


    public static void attachListener() {
        firebaseAuth.addAuthStateListener(firebaseAuthListener);
    }

    public static void detachListener() {
        firebaseAuth.removeAuthStateListener(firebaseAuthListener);
    }
}
