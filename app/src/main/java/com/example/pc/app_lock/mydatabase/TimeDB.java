package com.example.pc.app_lock.mydatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.example.pc.app_lock.AppUtil;


public class TimeDB extends SQLiteOpenHelper {
    public static final String DATABASE_TIME ="timedb";
    public static final String TABLE_TIME ="timetb";

    public Context context;
    public TimeDB(Context context) {
        super(context, DATABASE_TIME, null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlQuery = "CREATE TABLE "+TABLE_TIME +" (" +
                AppUtil.ID_TIME +" integer primary key AUTOINCREMENT, "+
                AppUtil.STATE_TIME +" TEXT," +
                AppUtil.DAY_TIME +" TEXT," +
                AppUtil.ID_GROUP +" int," +
                AppUtil.HOUR_TIME +" int," +
                AppUtil.MIN_TIME +" int)";
        db.execSQL(sqlQuery);

        String sql1 = "INSERT or replace INTO timetb (statetime,daytime,idgroup,hourtime,mintime) VALUES(0,' 2 3 4',2,14,30)" ;
        db.execSQL(sql1);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_TIME);
        onCreate(db);

        Toast.makeText(context, "Drop successfully", Toast.LENGTH_SHORT).show();
    }
}
