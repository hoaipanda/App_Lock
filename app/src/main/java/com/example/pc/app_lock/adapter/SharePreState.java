package com.example.pc.app_lock.adapter;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.pc.app_lock.AppUtil;

public class SharePreState {
    private static SharePreState instance;
    public Context context;

    private SharePreState(Context context) {
        this.context = context;
    }

    public static SharePreState getInstance(Context context) {
        if (instance == null) {
            instance = new SharePreState(context);
        }
        return instance;
    }

    public void addState(String key, boolean values) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(AppUtil.DATASTATE,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, values);
        editor.apply();
    }

    public boolean getState(String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(AppUtil.DATASTATE,Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, false);
    }

    public void removeState(String key) {
        SharedPreferences mySPrefs = context.getSharedPreferences(AppUtil.DATASTATE,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySPrefs.edit();
        editor.remove(key);
        editor.apply();
    }
}



