package com.example.classmate.models;

public class ExamMark {
    private String id;
    private String subject;
    private String examType;
    private String resultUrl;

    public ExamMark() {}

    public ExamMark(String id, String subject, String examType, String resultUrl) {
        this.id = id;
        this.subject = subject;
        this.examType = examType;
        this.resultUrl = resultUrl;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getExamType() { return examType; }
    public void setExamType(String examType) { this.examType = examType; }
    public String getResultUrl() { return resultUrl; }
    public void setResultUrl(String resultUrl) { this.resultUrl = resultUrl; }
}
