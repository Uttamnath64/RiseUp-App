package com.lit.riseup;

public class AchievementModal {
    String id,achievement_name,description,status;
    public AchievementModal(String id, String achievement_name, String description, String status) {
        this.id = id;
        this.achievement_name = achievement_name;
        this.description = description;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public String getAchievement_name() {
        return achievement_name;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setAchievement_name(String achievement_name) {
        this.achievement_name = achievement_name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
