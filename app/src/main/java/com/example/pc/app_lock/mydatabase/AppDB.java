package com.example.pc.app_lock.mydatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.example.pc.app_lock.AppUtil;


public class AppDB extends SQLiteOpenHelper {
    public static final String DATABASE_APP ="appdb";
    public static final String TABLE_NAME ="appofgroup";

    public Context context;
    public AppDB(Context context) {
        super(context, DATABASE_APP, null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlQuery = "CREATE TABLE "+TABLE_NAME +" (" +
                AppUtil.ID_APP +" integer primary key AUTOINCREMENT, "+
                AppUtil.PACKAGE_APP +" TEXT," +
                AppUtil.ID_GROUP +" int)";
        db.execSQL(sqlQuery);

        String sql1 = "INSERT or replace INTO appofgroup (packageapp,idgroup) VALUES('com.android.settings',2)" ;
        db.execSQL(sql1);

        String sql2 =
                "INSERT or replace INTO appofgroup (packageapp,idgroup) VALUES('com.google.android.apps.photos',2)" ;
        db.execSQL(sql2);
        String sql3 =
                "INSERT or replace INTO appofgroup (packageapp,idgroup) VALUES('com.android.gallery3d',2)" ;
        db.execSQL(sql3);
        String sql4 =
                "INSERT or replace INTO appofgroup (packageapp,idgroup) VALUES('com.android.email',2)" ;
        db.execSQL(sql4);
        String sql5 =
                "INSERT or replace INTO appofgroup (packageapp,idgroup) VALUES('com.android.mms',2)" ;
        db.execSQL(sql5);
        String sql6 =
                "INSERT or replace INTO appofgroup (packageapp,idgroup) VALUES('com.android.contacts',2)" ;
        db.execSQL(sql6);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);

        Toast.makeText(context, "Drop successfully", Toast.LENGTH_SHORT).show();
    }
}
