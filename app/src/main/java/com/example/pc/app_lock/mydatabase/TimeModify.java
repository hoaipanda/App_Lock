package com.example.pc.app_lock.mydatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.pc.app_lock.AppUtil;
import com.example.pc.app_lock.data.TimeApp;

import java.util.ArrayList;

import static com.example.pc.app_lock.mydatabase.TimeDB.TABLE_TIME;

public class TimeModify {

    private TimeDB timeDB;

    public TimeModify(Context context) {
        timeDB = new TimeDB(context);
    }

    public ArrayList<TimeApp> getListTime() {
        ArrayList<TimeApp> groupApps = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_TIME;

        SQLiteDatabase db = timeDB.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                TimeApp timeApp = new TimeApp();
                timeApp.setId(cursor.getInt(0));
                timeApp.setState(cursor.getInt(1));
                timeApp.setDay(cursor.getString(2));
                timeApp.setId_group(cursor.getInt(3));
                timeApp.setHour(cursor.getInt(4));
                timeApp.setMin(cursor.getInt(5));
                groupApps.add(timeApp);
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();
        return groupApps;
    }


    public void addTime(TimeApp timeApp) {
        SQLiteDatabase db = timeDB.getWritableDatabase();
        ContentValues values = new ContentValues();
        Integer temp = null;
        values.put(AppUtil.ID_TIME, temp);
        values.put(AppUtil.STATE_TIME, timeApp.getState());
        values.put(AppUtil.DAY_TIME, timeApp.getDay());
        values.put(AppUtil.ID_GROUP, timeApp.getId_group());
        values.put(AppUtil.HOUR_TIME, timeApp.getHour());
        values.put(AppUtil.MIN_TIME, timeApp.getMin());
        db.insert(TABLE_TIME, null, values);
        db.close();
    }

    public void updateTime(TimeApp timeApp){
        SQLiteDatabase db = timeDB.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(AppUtil.STATE_TIME, timeApp.getState());
        values.put(AppUtil.DAY_TIME,timeApp.getDay());
        values.put(AppUtil.ID_GROUP, timeApp.getId_group());
        values.put(AppUtil.HOUR_TIME, timeApp.getHour());
        values.put(AppUtil.MIN_TIME, timeApp.getMin());
        db.update(timeDB.TABLE_TIME,values,AppUtil.ID_TIME+"=?",new String[] { String.valueOf(timeApp.getId())});
        db.close();
    }


    public void deleteTimeApp(TimeApp timeApp) {
        SQLiteDatabase db = timeDB.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_TIME + " WHERE " + AppUtil.ID_TIME + "='" + timeApp.getId() + "'");
        db.close();
    }

    public void deleteAll() {
        SQLiteDatabase db = timeDB.getWritableDatabase();
        db.execSQL("delete from " + TABLE_TIME);
        db.close();
    }
}
