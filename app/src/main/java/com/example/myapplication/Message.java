package com.example.myapplication;


import java.util.Date;

public class Message {
    private String senderId;
    private String receiverId;
    private String message;

    public String getType() {
        return type;
    }

    public Double getLat() {
        return lat;
    }

    public Double getLng() {
        return lng;
    }

    private String type; // "text" or "location"
    private Double lat;
    private Double lng;

    private @com.google.firebase.firestore.ServerTimestamp Date timestamp;


    public Message() {} // Needed for Firebase

    public Message(String senderId, String receiverId, String message, Date timestamp) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public String getMessage() {
        return message;
    }

    public Date getTimestamp() {
        return timestamp;
    }
}
