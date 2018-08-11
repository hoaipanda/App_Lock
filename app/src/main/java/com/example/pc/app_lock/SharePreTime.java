package com.example.pc.app_lock;

import android.content.Context;
import android.content.SharedPreferences;

public class SharePreTime {
    private static SharePreTime instance;
    public Context context;

    private SharePreTime(Context context) {
        this.context = context;
    }

    public static SharePreTime getInstance(Context context) {
        if (instance == null) {
            instance = new SharePreTime(context);
        }
        return instance;
    }

    public void addTimeDay(String key, String values) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(AppUtil.DATATIME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, values);
        editor.apply();
    }

    public String getTimeDay(String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(AppUtil.DATATIME,Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, "");
    }



    public void removeDay(String key) {
        SharedPreferences mySPrefs = context.getSharedPreferences(AppUtil.DATATIME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySPrefs.edit();
        editor.remove(key);
        editor.apply();
    }
}



