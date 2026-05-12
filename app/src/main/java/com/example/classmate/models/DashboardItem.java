package com.example.classmate.models;

public class DashboardItem {
    private String title;
    private int iconRes;
    private int bgColor;

    public DashboardItem(String title, int iconRes, int bgColor) {
        this.title = title;
        this.iconRes = iconRes;
        this.bgColor = bgColor;
    }

    public String getTitle() { return title; }
    public int getIconRes() { return iconRes; }
    public int getBgColor() { return bgColor; }
}