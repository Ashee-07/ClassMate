package com.example.classmate.models;

public class Event {
    private String id;
    private String title;
    private String description;
    private String date;
    private String venue;
    private String attachmentUrl;
    private String fileName;

    public Event() {}

    public Event(String id, String title, String description, String date, String venue) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.date = date;
        this.venue = venue;
    }

    public Event(String id, String title, String description, String date, String venue, String attachmentUrl, String fileName) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.date = date;
        this.venue = venue;
        this.attachmentUrl = attachmentUrl;
        this.fileName = fileName;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getVenue() { return venue; }
    public void setVenue(String venue) { this.venue = venue; }
    public String getAttachmentUrl() { return attachmentUrl; }
    public void setAttachmentUrl(String attachmentUrl) { this.attachmentUrl = attachmentUrl; }
    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }
}