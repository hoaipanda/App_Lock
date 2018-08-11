package com.example.pc.app_lock.data;

public class TimeApp {
    private int id;
    private int state;
    private String day;
    private int id_group;
    private int hour;
    private int min;

    public TimeApp() {
    }

    public TimeApp(int state, String day, int id_group,int hour,int min) {
        this.state = state;
        this.day = day;
        this.id_group = id_group;
        this.hour = hour;
        this.min = min;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public int getId_group() {
        return id_group;
    }

    public void setId_group(int id_group) {
        this.id_group = id_group;
    }
}
