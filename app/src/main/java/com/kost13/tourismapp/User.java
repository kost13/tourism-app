package com.kost13.tourismapp;

import java.io.Serializable;

public class User implements Serializable {
    private String firstName;
    private String lastName;
    private String bio;
    private String id;

    public User() {}

    public User(String firstName, String lastName, String bio){
        this.firstName = firstName;
        this.lastName = lastName;
        this.bio = bio;
    }

    public void setId(String id){
        this.id = id;
    }

    public String getId() { return id; }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getFullName(){
        return getFirstName() + " " + getLastName();
    }
}
