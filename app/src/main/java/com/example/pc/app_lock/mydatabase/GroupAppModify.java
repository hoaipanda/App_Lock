package com.example.pc.app_lock.mydatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.pc.app_lock.AppUtil;
import com.example.pc.app_lock.data.GroupApp;
import com.example.pc.app_lock.data.TimeApp;

import java.util.ArrayList;

;import static com.example.pc.app_lock.mydatabase.GroupAppDB.TABLE_NAME_IM;


public class GroupAppModify {

    private GroupAppDB imageDB;

    public GroupAppModify(Context context) {
        imageDB = new GroupAppDB(context);
    }

    public ArrayList<GroupApp> getListGroup() {
        ArrayList<GroupApp> groupApps = new ArrayList<GroupApp>();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME_IM;
        SQLiteDatabase db = imageDB.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                GroupApp groupApp = new GroupApp();
                groupApp.setId(cursor.getInt(0));
                groupApp.setNameGroup(cursor.getString(1));
                groupApp.setState(cursor.getInt(2));
                groupApps.add(groupApp);
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();
        return groupApps;
    }

    public GroupApp getGroupById(int id) {
        SQLiteDatabase db = imageDB.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_NAME_IM + " WHERE " + AppUtil.ID_GROUP + " ='" + id + "'";

        Cursor cursor = db.rawQuery(selectQuery, null);
        GroupApp groupApp1 = null;
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                GroupApp groupApp = new GroupApp();
                groupApp.setId(cursor.getInt(0));
                groupApp.setNameGroup(cursor.getString(1));
                groupApp.setState(cursor.getInt(2));
                groupApp1 = groupApp;
                cursor.moveToNext();
            }

        }
        cursor.close();
        db.close();
        return groupApp1;
    }

    public ArrayList getGroupByState(int state) {
        SQLiteDatabase db = imageDB.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_NAME_IM + " WHERE " + AppUtil.STATE_GROUP + " ='" + state + "'";

        Cursor cursor = db.rawQuery(selectQuery, null);
        ArrayList<GroupApp> listRs = new ArrayList<>();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                GroupApp groupApp = new GroupApp();
                groupApp.setId(cursor.getInt(0));
                groupApp.setNameGroup(cursor.getString(1));
                groupApp.setState(cursor.getInt(2));
                listRs.lastIndexOf(groupApp);
                cursor.moveToNext();
            }

        }
        cursor.close();

        return listRs;
    }

    public void updateGroup(GroupApp groupApp) {
        SQLiteDatabase db = imageDB.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(AppUtil.NAME_GROUP, groupApp.getNameGroup());
        values.put(AppUtil.STATE_GROUP, groupApp.isState());
        db.update(imageDB.TABLE_NAME_IM, values, AppUtil.ID_GROUP + "=?", new String[]{String.valueOf(groupApp.getId())});
        db.close();
    }

    public void addGroup(GroupApp groupApp) {
        SQLiteDatabase db = imageDB.getWritableDatabase();
        ContentValues values = new ContentValues();
        Integer temp = null;
        values.put(AppUtil.ID_GROUP, temp);
        values.put(AppUtil.NAME_GROUP, groupApp.getNameGroup());
        values.put(AppUtil.STATE_GROUP, groupApp.isState());
        db.insert(TABLE_NAME_IM, null, values);
        db.close();
    }


    public void deleteGroupApp(int groupApp) {
        SQLiteDatabase db = imageDB.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME_IM + " WHERE " + AppUtil.ID_GROUP + "='" + groupApp + "'");
        db.close();
    }

    public void deleteAll() {
        SQLiteDatabase db = imageDB.getWritableDatabase();
        db.execSQL("delete from " + TABLE_NAME_IM);
        db.close();
    }
}
