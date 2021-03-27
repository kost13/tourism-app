package com.kost13.tourismapp;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
    private String name;
    private String bio;
    private String id;
    private String telephone;
    private String email;
    private ArrayList<String> locations;
    private String webpage;
    private ArrayList<String> languages;
    private String profileImageUrl;

    public User() {}

    public User(String name, String bio, String telephone, String email, ArrayList<String> locations, String webpage, ArrayList<String> languages, String profileImageUrl){
        this.name = name;
        this.bio = bio;
        this.telephone = telephone;
        this.email = email;
        this.locations = locations;
        this.webpage = webpage;
        this.languages = languages;
        this.setProfileImageUrl(profileImageUrl);
    }

    public void setId(String id){
        this.id = id;
    }

    public String getId() { return id; }

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

    public ArrayList<String> getLocations() {
        return locations;
    }

    public void setLocations(ArrayList<String> locations) {
        this.locations = locations;
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
}
