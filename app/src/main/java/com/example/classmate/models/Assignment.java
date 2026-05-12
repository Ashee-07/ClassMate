package com.example.classmate.models;

public class Assignment {
    private String id;
    private String title;
    private String subject;
    private String description;
    private String dueDate;
    private String attachmentUrl;
    private String fileName;

    public Assignment() {}

    public Assignment(String id, String title, String subject, String description, String dueDate, String attachmentUrl, String fileName) {
        this.id = id;
        this.title = title;
        this.subject = subject;
        this.description = description;
        this.dueDate = dueDate;
        this.attachmentUrl = attachmentUrl;
        this.fileName = fileName;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getDueDate() { return dueDate; }
    public void setDueDate(String dueDate) { this.dueDate = dueDate; }
    public String getAttachmentUrl() { return attachmentUrl; }
    public void setAttachmentUrl(String attachmentUrl) { this.attachmentUrl = attachmentUrl; }
    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }
}