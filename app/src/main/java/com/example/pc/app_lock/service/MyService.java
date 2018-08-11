package com.example.pc.app_lock.service;

import android.app.ActivityManager;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.pc.app_lock.SharePreHour;
import com.example.pc.app_lock.SharePreLock;
import com.example.pc.app_lock.SharePreMin;
import com.example.pc.app_lock.SharePreTime;
import com.example.pc.app_lock.activity.LockActivity;
import com.example.pc.app_lock.activity.MainActivity;
import com.example.pc.app_lock.adapter.SharePreState;

import java.util.Calendar;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class MyService extends Service {
    private String appPackage;
    private String preApp = "";
    private SharedPreferences pre;
    private Thread thread;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("hoaiii", "bhjdfhd");
//        thread = new Thread(runnable);
//        thread.start();

    }

    private String printForegroundTask() {
        String currentApp = "NULL";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            UsageStatsManager usm = (UsageStatsManager) this.getSystemService(Context.USAGE_STATS_SERVICE);
            long time = System.currentTimeMillis();
            List<UsageStats> appList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 1000, time);
            if (appList != null && appList.size() > 0) {
                SortedMap<Long, UsageStats> mySortedMap = new TreeMap<Long, UsageStats>();
                for (UsageStats usageStats : appList) {
                    mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
                }
                if (mySortedMap != null && !mySortedMap.isEmpty()) {
                    currentApp = mySortedMap.get(mySortedMap.lastKey()).getPackageName();
                }
            }
        } else {
            ActivityManager am = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> tasks = am.getRunningAppProcesses();
            currentApp = tasks.get(0).processName;
        }

        Log.e("AppLockerService", "Current App in foreground is: " + currentApp);
        return currentApp;
    }

    public boolean getForegroundApp() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            UsageStatsManager usm = (UsageStatsManager) this.getSystemService(Context.USAGE_STATS_SERVICE);
            long time = System.currentTimeMillis();
            List<UsageStats> appList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 1000, time);
            if (appList != null && appList.size() > 0) {
                SortedMap<Long, UsageStats> mySortedMap = new TreeMap<Long, UsageStats>();
                for (UsageStats usageStats : appList) {
                    mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
                }
                if (mySortedMap != null && !mySortedMap.isEmpty()) {
                    String s = mySortedMap.get(mySortedMap.lastKey()).getPackageName();
                    if (!s.equals(this.getPackageName())) {
                        appPackage = s;
                        return true;
                    }
                }
            }
        } else {
            ActivityManager am = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> tasks = am.getRunningAppProcesses();
            String s = tasks.get(0).processName;
            if (!s.equals(this.getPackageName())) {
                appPackage = s;
                return true;
            }
        }
        Log.e("AppLockerService", "Current App in foreground is: " + appPackage);
        return false;
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            while (true) {
                try {
                    Log.d("datdb", "runnable: " + preApp + " " + appPackage +" "+ getForegroundApp());
                    if (getForegroundApp()) {
                        if (!appPackage.equals(preApp)) {
                            if (SharePreLock.getInstance(MyService.this).getAppLock(appPackage)) {
                                if (SharePreState.getInstance(MyService.this).getState(appPackage)) {
                                    lockApp(appPackage);
                                } else {
                                    Calendar calendar = Calendar.getInstance();
                                    int hourCrr = calendar.get(Calendar.HOUR_OF_DAY);
                                    int minCrr = calendar.get(Calendar.MINUTE);
                                    int dayCrr = calendar.get(Calendar.DAY_OF_WEEK);
                                    int hour = SharePreHour.getInstance(MyService.this).getTimeHour(appPackage);
                                    int min = SharePreMin.getInstance(MyService.this).getTimeMin(appPackage);
                                    String day = SharePreTime.getInstance(MyService.this).getTimeDay(appPackage);
                                    if (hour != -1 && min != -1) {
                                        if (day.length() > 0) {
                                            String[] listDay = splitString(day);
                                            for (String mDay : listDay) {
                                                switch (mDay) {
                                                    case "2":
                                                        if (hourCrr == hour && minCrr == min && dayCrr == 2) {
                                                            lockApp(appPackage);
                                                        }
                                                        break;
                                                    case "3":
                                                        if (hourCrr == hour && minCrr == min && dayCrr == 3) {
                                                            lockApp(appPackage);
                                                        }
                                                        break;
                                                    case "4":
                                                        if (hourCrr == hour && minCrr == min && dayCrr == 4) {
                                                            lockApp(appPackage);
                                                        }
                                                        break;
                                                    case "5":
                                                        if (hourCrr == hour && minCrr == min && dayCrr == 5) {
                                                            lockApp(appPackage);
                                                        }
                                                        break;
                                                    case "6":
                                                        if (hourCrr == hour && minCrr == min && dayCrr == 6) {
                                                            lockApp(appPackage);
                                                        }
                                                        break;
                                                    case "7":
                                                        if (hourCrr == hour && minCrr == min && dayCrr == 7) {
                                                            lockApp(appPackage);
                                                        }
                                                        break;
                                                    case "8":
                                                        if (hourCrr == hour && minCrr == min && dayCrr == 8) {
                                                            lockApp(appPackage);
                                                        }
                                                        break;

                                                }
                                            }
                                        } else {
                                            if (hourCrr == hour && minCrr == min) {
                                                lockApp(appPackage);
                                            }

                                        }
                                    } else {
                                        lockApp(appPackage);
                                    }
                                }

                            }
                        }
                    } else {
                        preApp = "";
                        if (LockActivity.instance != null){
                            LockActivity.instance.finish();
                        }
                    }

                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }

    };

    private void checkTime(int hour, int min, int day, int hourCrr, int minCrr, int dayCrr){

    }


    private void lockApp(String appPackage) {
        preApp = appPackage;
        Intent i = new Intent();
        i.setClass(MyService.this, LockActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        SharePreState.getInstance(MyService.this).addState(appPackage, true);
    }

    private String[] splitString(String day) {
        String[] values = day.split(" ");
        return values;
    }

}
