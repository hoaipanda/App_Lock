package com.example.pc.app_lock;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import java.util.HashMap;

public class AppLockApplication extends Application {

    public static final String PROPERTY_ID = "UA-62504955-1";
    private static AppLockApplication appInstance;
    HashMap<TrackerName, Tracker> mTrackers = new HashMap<>();

    @Override
    public void onCreate() {
        super.onCreate();
        appInstance = this;
    }

    public enum TrackerName {
        APP_TRACKER // Tracker used only in this app.
        // roll-up tracking.
    }

    /**
     * Get an instance of application
     */
    public static synchronized AppLockApplication getInstance() {
        return appInstance;
    }

    public synchronized Tracker getTracker(TrackerName trackerId) {
        if (!mTrackers.containsKey(trackerId)) {

            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            Tracker t = (trackerId == TrackerName.APP_TRACKER) ? analytics
                    .newTracker(PROPERTY_ID) : analytics
                    .newTracker(R.xml.global_tracker);
            mTrackers.put(trackerId, t);

        }
        return mTrackers.get(trackerId);
    }
}
