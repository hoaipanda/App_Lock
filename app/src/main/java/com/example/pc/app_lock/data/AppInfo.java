package com.example.pc.app_lock.data;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

public class AppInfo implements Serializable {
    private String appname;
    private Drawable icon;
    private String packageName;
    private boolean isCheck;

    public AppInfo() {
    }

    public AppInfo(String appname, String packageName, Drawable icon, boolean isCheck) {
        this.appname = appname;
        this.packageName = packageName;
        this.icon = icon;
        this.isCheck = isCheck;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getAppname() {
        return appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }


    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }
}
