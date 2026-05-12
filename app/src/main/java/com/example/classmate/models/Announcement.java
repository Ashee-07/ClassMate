package com.example.classmate.models;

public class Announcement {
    private String id;
    private String title;
    private String content;
    private long timestamp;
    private boolean isUrgent;
    private String driveLink;

    public Announcement() {}

    public Announcement(String id, String title, String content, long timestamp, boolean isUrgent, String driveLink) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.timestamp = timestamp;
        this.isUrgent = isUrgent;
        this.driveLink = driveLink;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    public boolean isUrgent() { return isUrgent; }
    public void setUrgent(boolean urgent) { isUrgent = urgent; }
    public String getDriveLink() { return driveLink; }
    public void setDriveLink(String driveLink) { this.driveLink = driveLink; }
}