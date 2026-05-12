package com.example.classmate.models;

public class Seminar {
    private String id;
    private String topic;
    private String speaker;
    private String venue;
    private String date;
    private String time;

    public Seminar() {}

    public Seminar(String id, String topic, String speaker, String venue, String date, String time) {
        this.id = id;
        this.topic = topic;
        this.speaker = speaker;
        this.venue = venue;
        this.date = date;
        this.time = time;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }
    public String getSpeaker() { return speaker; }
    public void setSpeaker(String speaker) { this.speaker = speaker; }
    public String getVenue() { return venue; }
    public void setVenue(String venue) { this.venue = venue; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }
}