package com.example.myapplication;

public class User {
    private String id; // Added ID for Firestore document ID
    public String name, phone, email, city, region, userType;

    // Constructor with all fields
    public User( String id,String name, String phone, String email, String city, String region, String userType  ) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.city = city;
        this.region = region;
        this.userType = userType;
    }

    // Default constructor (needed for Firestore)
    public User() {}

    // Getter for ID
    public String getId() {
        return id;
    }

    // Getter methods for other fields
    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getCity() {
        return city;
    }

    public String getRegion() {
        return region;
    }

    public String getUserType() {
        return userType;
    }
}
