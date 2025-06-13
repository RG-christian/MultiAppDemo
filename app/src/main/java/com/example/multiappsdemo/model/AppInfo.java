package com.example.multiappsdemo.model;

public class AppInfo {
    public String title;
    public String description;
    public boolean isSelected; // Pour garder la sélection

    public AppInfo(String title, String description) {
        this.title = title;
        this.description = description;
        this.isSelected = false;
    }
}
