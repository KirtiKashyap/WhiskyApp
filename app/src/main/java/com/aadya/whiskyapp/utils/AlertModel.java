package com.aadya.whiskyapp.utils;

public class AlertModel {

    private long duration;
    private int drawable, color;
    private String title, message;

    public AlertModel(long setDuration, String title, String message, int drawable, int color) {
        this.title = title;
        this.message = message;
        this.duration = setDuration;
        this.drawable = drawable;
        this.color = color;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long setDuration) {
        this.duration = setDuration;
    }

    public int getDrawable() {
        return drawable;
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
