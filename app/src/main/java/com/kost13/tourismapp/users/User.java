package com.kost13.tourismapp.users;

import android.net.Uri;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
    private String name;
    private String bio;
    private String id;
    private String telephone;
    private String email;
    private String location;
    private String webpage;
    private ArrayList<String> languages;
    private String profileImageUrl;
    private boolean isGuide = false;
    private Uri image;

    public User() {
    }

    public User(String name, String bio, String telephone, String email, String location, String webpage, ArrayList<String> languages, String profileImageUrl) {
        this.name = name;
        this.bio = bio;
        this.telephone = telephone;
        this.email = email;
        this.location = location;
        this.webpage = webpage;
        this.languages = languages;
        this.setProfileImageUrl(profileImageUrl);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getWebpage() {
        return webpage;
    }

    public void setWebpage(String webpage) {
        this.webpage = webpage;
    }

    public ArrayList<String> getLanguages() {
        return languages;
    }

    public void setLanguages(ArrayList<String> languages) {
        this.languages = languages;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public boolean getIsGuide() {
        return isGuide;
    }

    public void setIsGuide(boolean guide) {
        isGuide = guide;
    }

    public Uri imageUri() {
        return image;
    }

    public void setImage(Uri image) {
        this.image = image;
    }
}
