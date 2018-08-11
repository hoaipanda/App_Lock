package com.example.pc.app_lock;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.pc.app_lock.adapter.SharePreState;

public class SharePreLock {
    private static SharePreLock instance;
    public Context context;

    private SharePreLock(Context context) {
        this.context = context;
    }

    public static SharePreLock getInstance(Context context) {
        if (instance == null) {
            instance = new SharePreLock(context);
        }
        return instance;
    }

    public void addAppLock(String key, boolean values) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(AppUtil.DATAKEY,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, values);
        editor.apply();
    }

    public boolean getAppLock(String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(AppUtil.DATAKEY,Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, false);
    }

    public void removeAppLock(String key) {
        SharedPreferences mySPrefs = context.getSharedPreferences(AppUtil.DATAKEY,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySPrefs.edit();
        editor.remove(key);
        editor.apply();
        SharePreState.getInstance(context).removeState(key);
    }
}
