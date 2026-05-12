package com.example.classmate.models;

public class StudyMaterial {
    private String id;
    private String title;
    private String subject;
    private String type; // PDF, PPT, Notes
    private String fileUrl;
    private String fileName;

    public StudyMaterial() {}

    public StudyMaterial(String id, String title, String subject, String type, String fileUrl, String fileName) {
        this.id = id;
        this.title = title;
        this.subject = subject;
        this.type = type;
        this.fileUrl = fileUrl;
        this.fileName = fileName;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getFileUrl() { return fileUrl; }
    public void setFileUrl(String fileUrl) { this.fileUrl = fileUrl; }
    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }
}