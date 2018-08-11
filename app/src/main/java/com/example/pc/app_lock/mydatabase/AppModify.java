package com.example.pc.app_lock.mydatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.pc.app_lock.AppUtil;
import com.example.pc.app_lock.data.AppOfGroup;

import java.util.ArrayList;

import static com.example.pc.app_lock.mydatabase.AppDB.TABLE_NAME;

public class AppModify {

    private AppDB appDB;

    public AppModify(Context context) {
        appDB = new AppDB(context);
    }

    public ArrayList<AppOfGroup> getListApp() {
        ArrayList<AppOfGroup> groupApps = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = appDB.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                AppOfGroup groupApp = new AppOfGroup();
                groupApp.setId(cursor.getInt(0));
                groupApp.setPackageName(cursor.getString(1));
                groupApp.setId_group(cursor.getInt(2));
                groupApps.add(groupApp);
                cursor.moveToNext();
            }
            ;
        }
        cursor.close();
        db.close();
        return groupApps;
    }

    public ArrayList<AppOfGroup> getListAppByGroup(int id) {
        ArrayList<AppOfGroup> groupApps = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE " + AppUtil.ID_GROUP + "='" + id + "'";
        SQLiteDatabase db = appDB.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                AppOfGroup groupApp = new AppOfGroup();
                groupApp.setId(cursor.getInt(0));
                groupApp.setPackageName(cursor.getString(1));
                groupApp.setId_group(cursor.getInt(2));
                groupApps.add(groupApp);
                cursor.moveToNext();
            }
            ;
        }
        cursor.close();
        db.close();
        return groupApps;
    }

    public void addApp(AppOfGroup app) {
        SQLiteDatabase db = appDB.getWritableDatabase();
        ContentValues values = new ContentValues();
        Integer temp = null;
        values.put(AppUtil.ID_APP, temp);
        values.put(AppUtil.PACKAGE_APP, app.getPackageName());
        values.put(AppUtil.ID_GROUP, app.getId_group());
        db.insert(TABLE_NAME, null, values);
        db.close();
    }


    public void deleteGroupApp(int id) {
        SQLiteDatabase db = appDB.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + AppUtil.ID_GROUP + "='" + id + "'");
        db.close();
    }

    public void deleteAll() {
        SQLiteDatabase db = appDB.getWritableDatabase();
        db.execSQL("delete from " + TABLE_NAME);
        db.close();
    }
}
