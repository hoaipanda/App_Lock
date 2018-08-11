package com.example.pc.app_lock;

import android.content.Context;
import android.content.SharedPreferences;

public class SharePreMin {
    private static SharePreMin instance;
    public Context context;

    private SharePreMin(Context context) {
        this.context = context;
    }

    public static SharePreMin getInstance(Context context) {
        if (instance == null) {
            instance = new SharePreMin(context);
        }
        return instance;
    }

    public void addTimeMin(String key, int values) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(AppUtil.DATAMIN,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, values);
        editor.apply();
    }

    public int getTimeMin(String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(AppUtil.DATAMIN,Context.MODE_PRIVATE);
        return sharedPreferences.getInt(key, -1);
    }

    public void removeMin(String key) {
        SharedPreferences mySPrefs = context.getSharedPreferences(AppUtil.DATAMIN,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySPrefs.edit();
        editor.remove(key);
        editor.apply();
    }
}



