package com.example.classmate.models;

public class Exam {
    private String id;
    private String subject;
    private String date;
    private String time;
    private String attachmentUrl;
    private String fileName;

    public Exam() {}

    public Exam(String id, String subject, String date, String time) {
        this.id = id;
        this.subject = subject;
        this.date = date;
        this.time = time;
    }

    public Exam(String id, String subject, String date, String time, String attachmentUrl, String fileName) {
        this.id = id;
        this.subject = subject;
        this.date = date;
        this.time = time;
        this.attachmentUrl = attachmentUrl;
        this.fileName = fileName;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }
    public String getAttachmentUrl() { return attachmentUrl; }
    public void setAttachmentUrl(String attachmentUrl) { this.attachmentUrl = attachmentUrl; }
    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }
}