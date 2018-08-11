package com.example.pc.app_lock.data;

public class GroupApp {
    private int id;
    private String nameGroup;
    private int state;

    public GroupApp() {
    }

    public GroupApp(String nameGroup, int state) {
        this.nameGroup = nameGroup;
        this.state = state;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNameGroup() {
        return nameGroup;
    }

    public void setNameGroup(String nameGroup) {
        this.nameGroup = nameGroup;
    }

    public int isState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
