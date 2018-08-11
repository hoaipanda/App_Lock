package com.example.pc.app_lock.mydatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.example.pc.app_lock.AppUtil;


public class GroupAppDB extends SQLiteOpenHelper {
    public static final String DATABASE_NAME_IM ="groupappdb";
    public static final String TABLE_NAME_IM ="groupapp";

    public Context context;
    public GroupAppDB(Context context) {
        super(context, DATABASE_NAME_IM, null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlQuery = "CREATE TABLE "+TABLE_NAME_IM +" (" +
                AppUtil.ID_GROUP +" integer primary key AUTOINCREMENT, "+
                AppUtil.NAME_GROUP +" TEXT," +
                AppUtil.STATE_GROUP +" int)";
        db.execSQL(sqlQuery);
        String sql = "INSERT or replace INTO groupapp (namegroup,stategroup) VALUES('Open All App',0)" ;
        db.execSQL(sql);
        String sql1 = "INSERT or replace INTO groupapp (namegroup,stategroup) VALUES('Guest',0)" ;
        db.execSQL(sql1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME_IM);
        onCreate(db);

        Toast.makeText(context, "Drop successfully", Toast.LENGTH_SHORT).show();
    }
}
