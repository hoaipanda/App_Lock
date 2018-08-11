package com.example.pc.app_lock;

import android.content.Context;
import android.content.SharedPreferences;

public class SharePreHour {
    private static SharePreHour instance;
    public Context context;

    private SharePreHour(Context context) {
        this.context = context;
    }

    public static SharePreHour getInstance(Context context) {
        if (instance == null) {
            instance = new SharePreHour(context);
        }
        return instance;
    }

    public void addTimeHour(String key, int values) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(AppUtil.DATAHOUR,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, values);
        editor.apply();
    }

    public int getTimeHour(String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(AppUtil.DATAHOUR,Context.MODE_PRIVATE);
        return sharedPreferences.getInt(key, -1);
    }

    public void removeHour(String key) {
        SharedPreferences mySPrefs = context.getSharedPreferences(AppUtil.DATAHOUR,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySPrefs.edit();
        editor.remove(key);
        editor.apply();
    }
}



