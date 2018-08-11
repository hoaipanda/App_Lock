package com.example.pc.app_lock.service;

import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.WakefulBroadcastReceiver;
import com.example.pc.app_lock.AppUtil;
import com.example.pc.app_lock.SharePreLock;

import java.util.ArrayList;


/**
 * Created by dell on 3/5/2018.
 */

public class AlarmReceiver extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String app = intent.getStringExtra(AppUtil.PACKAGENAME);
        SharePreLock.getInstance(context).addAppLock(app, true);
    }


}
