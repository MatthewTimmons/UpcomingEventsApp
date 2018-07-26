package com.matthewtimmons.upcomingeventsapp.models;

import com.google.firebase.firestore.DocumentSnapshot;

import java.io.Serializable;

public abstract class Event implements Serializable {
    String id;
    String title;
    String date;
    String imageUrl;
    String eventType;

    public Event(DocumentSnapshot documentSnapshot) {
        this.id = documentSnapshot.getId();
        this.title = documentSnapshot.getString("title");
        this.date = documentSnapshot.getString("date");
        this.imageUrl = documentSnapshot.getString("imageUrl");
        this.eventType = documentSnapshot.getString("eventType");
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String releaseDate) {
        this.date = releaseDate;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
