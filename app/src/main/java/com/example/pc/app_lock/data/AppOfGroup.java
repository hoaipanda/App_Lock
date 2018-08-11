package com.example.pc.app_lock.data;

public class AppOfGroup {
    private int id;
    private String packageName;
    private int id_group;

    public AppOfGroup() {
    }

    public AppOfGroup(String packageName, int id_group) {
        this.packageName = packageName;
        this.id_group = id_group;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public int getId_group() {
        return id_group;
    }

    public void setId_group(int id_group) {
        this.id_group = id_group;
    }
}
