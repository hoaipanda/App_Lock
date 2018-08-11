package com.example.pc.app_lock.service;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pc.app_lock.AppUtil;
import com.example.pc.app_lock.R;
import com.example.pc.app_lock.SharePreLock;
import com.example.pc.app_lock.SharePreMin;
import com.example.pc.app_lock.activity.LockActivity;
import com.example.pc.app_lock.activity.MainActivity;
import com.example.pc.app_lock.data.AppOfGroup;
import com.example.pc.app_lock.data.GroupApp;
import com.example.pc.app_lock.mydatabase.AppModify;
import com.example.pc.app_lock.mydatabase.GroupAppModify;
import com.takwolf.android.lock9.Lock9View;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.SortedMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

public class GroupService extends Service {
    private Thread thread;
    private String appCurrent = "", previousApp = "";
    private ArrayList<GroupApp> listGroup = new ArrayList<>();
    private GroupAppModify groupAppModify;
    private ArrayList<AppOfGroup> listApp = new ArrayList<>();
    private AppModify appModify;
    private WindowManager windowManager;
    ImageView imageView;
    private Timer timer;
    private Intent notificationIntent;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;

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
        groupAppModify = new GroupAppModify(this);
        listGroup = groupAppModify.getGroupByState(1);
        appModify = new AppModify(this);
        for (GroupApp groupApp : listGroup) {
            listApp.addAll(appModify.getListAppByGroup(groupApp.getId()));
        }
        timer = new Timer("AppCheckServices");
        timer.schedule(updateTask, 1000L, 1000L);

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        imageView = new ImageView(this);
        imageView.setVisibility(View.GONE);
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP | Gravity.CENTER;
        params.x = ((getApplicationContext().getResources().getDisplayMetrics().widthPixels) / 2);
        params.y = ((getApplicationContext().getResources().getDisplayMetrics().heightPixels) / 2);
        windowManager.addView(imageView, params);
//        thread = new Thread(runnable);
//        thread.start();
    }


    private TimerTask updateTask = new TimerTask() {
        @Override
        public void run() {
            Log.d("datdb", isConcernedAppIsInForeground() + "");
            if (isConcernedAppIsInForeground()) {
                if (imageView != null) {
                    imageView.post(new Runnable() {
                        public void run() {
                            Log.d("datdb", "appCurrent: " + appCurrent + "\tpreviousApp: " + previousApp);
                            if (!appCurrent.matches(previousApp)) {
//                                showDialog();
                                Intent i = new Intent(GroupService.this, LockActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                                previousApp = appCurrent;
                            }
                        }
                    });
                }
            } else {

            }

        }
    };

    public boolean isConcernedAppIsInForeground() {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> task = manager.getRunningTasks(5);
        if (Build.VERSION.SDK_INT <= 20) {
            if (task.size() > 0) {
                ComponentName componentInfo = task.get(0).topActivity;

                if (!componentInfo.getPackageName().equals(appCurrent) && !componentInfo.getPackageName().equals(this.getPackageName())) {
                    previousApp = "";
                }

                if (SharePreLock.getInstance(GroupService.this).getAppLock(componentInfo.getPackageName()) && !componentInfo.getPackageName().equals(this.getPackageName())) {
                    appCurrent = componentInfo.getPackageName();
                    return true;
                }

            }
        } else {
            String mpackageName = manager.getRunningAppProcesses().get(0).processName;
            UsageStatsManager usage = (UsageStatsManager) this.getSystemService(Context.USAGE_STATS_SERVICE);
            long time = System.currentTimeMillis();
            List<UsageStats> stats = usage.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, 0, time);
            if (stats != null) {
                SortedMap<Long, UsageStats> runningTask = new TreeMap<Long, UsageStats>();
                for (UsageStats usageStats : stats) {
                    runningTask.put(usageStats.getLastTimeUsed(), usageStats);
                }
                if (runningTask.isEmpty()) {
                    mpackageName = "";
                } else {
                    mpackageName = runningTask.get(runningTask.lastKey()).getPackageName();
                }
            }
            if (!mpackageName.equals(appCurrent) && !mpackageName.equals(this.getPackageName())) {
                previousApp = "";
            }
            if (SharePreLock.getInstance(GroupService.this).getAppLock(mpackageName) && !mpackageName.equals(this.getPackageName())) {
                appCurrent = mpackageName;
                return true;
            }
        }
        return false;
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void scheduleNotification(int id, int hour, int minute, String repeatDay, String app) {

        notificationIntent = new Intent(this, AlarmReceiver.class);
        notificationIntent.putExtra(AppUtil.PACKAGENAME,app);

        pendingIntent = PendingIntent.getBroadcast(this, id, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        if (repeatDay.length() > 0) {
            String[] listDayRP = splitString(repeatDay);
            for (String dayrp : listDayRP) {
                switch (dayrp) {
                    case "2":
                        setAlarmDay(hour, minute, 2);
                        break;
                    case "3":
                        setAlarmDay(hour, minute, 3);
                        break;
                    case "4":
                        setAlarmDay(hour, minute, 4);
                        break;
                    case "5":
                        setAlarmDay(hour, minute, 5);
                        break;
                    case "6":
                        setAlarmDay(hour, minute, 6);
                        break;
                    case "7":
                        setAlarmDay(hour, minute, 7);
                        break;
                    case "8":
                        setAlarmDay(hour, minute, 1);
                        break;

                }
            }
        } else setAlarm(hour, minute);


    }

    public void cancelAlarm(int id) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent1 = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent1 = PendingIntent.getBroadcast(this, id, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent1);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void setAlarm(int hour, int minute) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

    }


    private void setAlarmDay(int hour, int minute, int day) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, day);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 7 * 24 * 60 * 60 * 1000, pendingIntent);

    }
    private String[] splitString(String day) {
        String[] values = day.split(" ");
        return values;
    }



}
