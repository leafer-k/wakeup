package com.example.timemanager;

import java.io.Serializable;

public class Event implements Serializable {
    private int id;
    private int hour;
    private int minute;
    private String description;
    private boolean isActive;
    private boolean cycle;

    public void setId(int id) {
        this.id = id;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setCycle(boolean cycle) {
        this.cycle = cycle;
    }


    public int getId() {
        return id;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public String getDescription() {
        return description;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public boolean isCycle() {
        return cycle;
    }



    public Event(int id, int hour, int minute, String description, boolean isActive, boolean cycle) {
        this.id = id;
        this.hour = hour;
        this.minute = minute;
        this.description = description;
        this.isActive = isActive;
        this.cycle = cycle;
    }


}
