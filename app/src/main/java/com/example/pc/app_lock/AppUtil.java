package com.example.pc.app_lock;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.example.pc.app_lock.data.AppInfo;

import java.util.ArrayList;
import java.util.Collections;

public class AppUtil {
    public static final String DATAPERMISSION = "datapermistion";
    public static final String ISFIRST = "isfirst";
    public static final String DATASTATE = "datastate";
    public static final String DATAHOUR = "datahour";
    public static final String DATAMIN = "datamin";
    public static final String DATATIME = "dataTime";
    public static final String DATAKEY = "datakey";

    public static final String PACKAGENAME = "packagename";

    public static final String DATAPASSWORD = "datapassword";
    public static final String PASSWORD = "password";

    public static final String ID_GROUP = "idgroup";
    public static final String NAME_GROUP = "namegroup";
    public static final String STATE_GROUP = "stategroup";

    public static final String PACKAGE_APP = "packageapp";
    public static final String ID_APP = "idapp";

    public static final String ID_TIME = "idtime";
    public static final String STATE_TIME = "statetime";
    public static final String DAY_TIME = "daytime";
    public static final String HOUR_TIME = "hourtime";
    public static final String MIN_TIME = "mintime";


    public static final String TYPE_ADD = "typeadd";
    public static final int TYPE_CREATE = 1;
    public static final int TYPE_EDIT = 2;
    public static ArrayList<AppInfo> getListApp(Context context) {
        ArrayList<AppInfo> list = new ArrayList<>();

        PackageManager pm = context.getPackageManager();

        Intent i = new Intent(Intent.ACTION_MAIN);
        i.addCategory(Intent.CATEGORY_LAUNCHER);

        ArrayList<ResolveInfo> mList = (ArrayList<ResolveInfo>) pm
                .queryIntentActivities(i, PackageManager.PERMISSION_GRANTED);
        Collections.sort(mList, new ResolveInfo.DisplayNameComparator(pm));


        for (ResolveInfo resInfo : mList) {
            if (!resInfo.activityInfo.applicationInfo.packageName.equals(context.getPackageName())) {
                list.add(new AppInfo(resInfo.activityInfo.applicationInfo.loadLabel(pm)
                        .toString(), resInfo.activityInfo.applicationInfo.packageName, resInfo.activityInfo.applicationInfo.loadIcon(pm), false));
            }

        }

        return list;

    }

    public static String getAppNameFromPkgName(Context context, String Packagename) {
        try {
            PackageManager packageManager = context.getPackageManager();
            ApplicationInfo info = packageManager.getApplicationInfo(Packagename, PackageManager.GET_META_DATA);
            String appName = (String) packageManager.getApplicationLabel(info);
            return appName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static Drawable getIconFromPackage(Context context,String packagename){
        Drawable icon = null;
        try {
             icon = context.getPackageManager().getApplicationIcon(packagename);

        }
        catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return icon;
    }
}
