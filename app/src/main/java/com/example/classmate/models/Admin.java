package com.example.classmate.models;

public class Admin {
    private String uid;
    private String name;
    private String email;
    private String department;
    private String role;
    private String profileImageUrl;

    public Admin() {}

    public Admin(String uid, String name, String email, String department, String role) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.department = department;
        this.role = role;
    }

    public String getUid() { return uid; }
    public void setUid(String uid) { this.uid = uid; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getProfileImageUrl() { return profileImageUrl; }
    public void setProfileImageUrl(String profileImageUrl) { this.profileImageUrl = profileImageUrl; }
}
