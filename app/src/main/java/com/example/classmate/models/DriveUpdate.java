package com.example.classmate.models;

public class DriveUpdate {
    private String id;
    private String companyName;
    private String position;
    private String deadline;
    private String description;
    private String infoUrl;

    public DriveUpdate() {}

    public DriveUpdate(String id, String companyName, String position, String deadline, String description, String infoUrl) {
        this.id = id;
        this.companyName = companyName;
        this.position = position;
        this.deadline = deadline;
        this.description = description;
        this.infoUrl = infoUrl;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }
    public String getDeadline() { return deadline; }
    public void setDeadline(String deadline) { this.deadline = deadline; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getInfoUrl() { return infoUrl; }
    public void setInfoUrl(String infoUrl) { this.infoUrl = infoUrl; }
}