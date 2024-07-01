package com.example.myapplication.model;

public class Notification {private final String title;
    private final String details;
    private boolean expanded;

    public Notification(String title, String details) {
        this.title = title;
        this.details = details;
        this.expanded = false;
    }

    public String getTitle() {
        return title;
    }

    public String getDetails() {
        return details;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }
}