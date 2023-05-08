package com.example.qrcodescanner.retrofit;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EventList {
    @SerializedName("result")
    private List<Event> events;

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }
}