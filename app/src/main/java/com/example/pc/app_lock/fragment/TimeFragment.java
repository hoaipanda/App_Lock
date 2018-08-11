package com.example.pc.app_lock.fragment;


import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.pc.app_lock.AppUtil;
import com.example.pc.app_lock.R;
import com.example.pc.app_lock.SharePreHour;
import com.example.pc.app_lock.SharePreLock;
import com.example.pc.app_lock.SharePreMin;
import com.example.pc.app_lock.SharePreTime;
import com.example.pc.app_lock.adapter.GroupDialogAdapter;
import com.example.pc.app_lock.adapter.TimeAdapter;
import com.example.pc.app_lock.data.AppInfo;
import com.example.pc.app_lock.data.AppOfGroup;
import com.example.pc.app_lock.data.GroupApp;
import com.example.pc.app_lock.data.TimeApp;
import com.example.pc.app_lock.mydatabase.AppModify;
import com.example.pc.app_lock.mydatabase.GroupAppModify;
import com.example.pc.app_lock.mydatabase.TimeModify;
import com.example.pc.app_lock.service.AlarmReceiver;
import com.example.pc.app_lock.service.GroupService;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class TimeFragment extends Fragment {

    private RecyclerView rvTime,rvGroup;
    private FloatingActionButton imAdd;
    private TimeAdapter timeAdapter;
    private ArrayList<TimeApp> listTimeApp = new ArrayList<>();
    private TimeModify modify;
    private TimeApp mTimeApp;
    private GroupAppModify groupAppModify;
    private ArrayList<GroupApp> listGroup = new ArrayList<>();
    private GroupDialogAdapter groupDialogAdapter;
    private Dialog dialog,dialogDel;
    private ArrayList<AppInfo> listInfo = new ArrayList<>();
    private AppModify appModify;
    private Intent notificationIntent;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    public TimeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_time, container, false);
        rvTime = view.findViewById(R.id.rvTime);
        imAdd = view.findViewById(R.id.imAdd);
        modify = new TimeModify(getContext());
        groupAppModify = new GroupAppModify(getContext());
        appModify = new AppModify(getContext());
        listInfo = AppUtil.getListApp(getContext());
        listGroup = groupAppModify.getListGroup();
        updateRv();



        timeAdapter.setOnItemClickedListener(new TimeAdapter.OnItemClickedListener() {
            @Override
            public void onTvTimeClick(final TimeApp timeApp) {
                mTimeApp = timeApp;
                Calendar calendar = Calendar.getInstance();
                final int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        timeApp.setHour(hourOfDay);
                        timeApp.setMin(minute);
                        modify.updateTime(timeApp);
                        timeAdapter.notifyDataSetChanged();
                        if (timeApp.getState()==1){
                            ArrayList<AppOfGroup> list = appModify.getListAppByGroup(timeApp.getId_group());
                            for (AppOfGroup appOfGroup:list){
                                SharePreHour.getInstance(getActivity()).addTimeHour(appOfGroup.getPackageName(),timeApp.getHour());
                                SharePreMin.getInstance(getActivity()).addTimeMin(appOfGroup.getPackageName(),timeApp.getMin());
                                cancelAlarm(appOfGroup.getId());
                                scheduleNotification(appOfGroup.getId(),timeApp.getHour(),timeApp.getMin(),timeApp.getDay(),appOfGroup.getPackageName());
                            }
                        }
                    }
                }, hour, minute, true);


                timePickerDialog.show();
            }

            @Override
            public void onImDelClick(TimeApp timeApp) {
                mTimeApp = timeApp;
                dialogDel = new Dialog(getActivity());
                dialogDel.requestWindowFeature(Window.FEATURE_NO_TITLE);
                Window view = ((Dialog) dialogDel).getWindow();
                view.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogDel.setContentView(R.layout.dialog_delete);
                dialogDel.setCancelable(false);
                ImageView imClose = dialogDel.findViewById(R.id.imClose);
                TextView tvYes = dialogDel.findViewById(R.id.tvYes);
                TextView tvNo = dialogDel.findViewById(R.id.tvNo);

                imClose.setOnClickListener(lsDel);
                tvNo.setOnClickListener(lsDel);
                tvYes.setOnClickListener(lsDel);

                dialogDel.show();
            }

            @Override
            public void onLyGroupClick(final TimeApp timeApp) {
                dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                Window view = ((Dialog) dialog).getWindow();
                view.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setContentView(R.layout.dialog_group);
                dialog.setCancelable(false);
                ImageView imClose = dialog.findViewById(R.id.imClose);
                imClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                rvGroup = dialog.findViewById(R.id.rvGroup);

                updateRvGroup();
                groupDialogAdapter.setOnItemClickedListener(new GroupDialogAdapter.OnItemClickedListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onItemClick(GroupApp groupApp) {
                        timeApp.setId_group(groupApp.getId());
                        modify.updateTime(timeApp);
                        timeAdapter.notifyDataSetChanged();
                        dialog.dismiss();
                        changeTime(timeApp);
                    }
                });

                dialog.show();
            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onImStateClick(TimeApp timeApp) {
                modify.updateTime(timeApp);
                changeTime(timeApp);
            }

            @Override
            public void onlyDay(TimeApp timeApp) {
                if (timeApp.getState()==1){
                    ArrayList<AppOfGroup> list = appModify.getListAppByGroup(timeApp.getId_group());
                    for (AppOfGroup appOfGroup:list){
                        SharePreTime.getInstance(getActivity()).addTimeDay(appOfGroup.getPackageName(),timeApp.getDay());
                    }
                }
            }
        });

        imAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int min = calendar.get(Calendar.MINUTE);
                TimeApp timeApp = new TimeApp(0,"",2,hour,min);
                modify.addTime(timeApp);
                updateRv();
            }
        });

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void changeTime(TimeApp timeApp){
        GroupApp groupApp = groupAppModify.getGroupById(timeApp.getId_group());
        ArrayList<AppOfGroup> list = appModify.getListAppByGroup(timeApp.getId_group());
        if (timeApp.getState()==1){
            groupApp.setState(1);
            for (TimeApp timeApp1:listTimeApp){
                if (timeApp1!=timeApp){
                    timeApp1.setState(0);
                    modify.updateTime(timeApp1);
                }
            }
            updateRv();
            for (GroupApp groupApp1 : listGroup){
                if (groupApp1!=groupApp){
                    groupApp1.setState(0);
                    groupAppModify.updateGroup(groupApp1);
                }
            }

            for (AppInfo appInfo:listInfo){
                SharePreLock.getInstance(getActivity()).removeAppLock(appInfo.getPackageName());
            }
            for (AppOfGroup appOfGroup:list){
//                SharePreLock.getInstance(getActivity()).addAppLock(appOfGroup.getPackageName(),true);
//                SharePreTime.getInstance(getActivity()).addTimeDay(appOfGroup.getPackageName(),timeApp.getDay());
//                SharePreHour.getInstance(getActivity()).addTimeHour(appOfGroup.getPackageName(),timeApp.getHour());
//                SharePreMin.getInstance(getActivity()).addTimeMin(appOfGroup.getPackageName(),timeApp.getMin());
                scheduleNotification(appOfGroup.getId(),timeApp.getHour(),timeApp.getMin(),timeApp.getDay(),appOfGroup.getPackageName());
            }
        }else {
            groupApp.setState(0);
            for (AppOfGroup appOfGroup:list){
//                SharePreLock.getInstance(getActivity()).removeAppLock(appOfGroup.getPackageName());
//                SharePreTime.getInstance(getActivity()).removeDay(appOfGroup.getPackageName());
//                SharePreHour.getInstance(getActivity()).removeHour(appOfGroup.getPackageName());
//                SharePreMin.getInstance(getActivity()).removeMin(appOfGroup.getPackageName());
                cancelAlarm(appOfGroup.getId());
            }
        }
        groupAppModify.updateGroup(groupApp);
        timeAdapter.notifyDataSetChanged();
        getActivity().sendBroadcast(new Intent("UPDATE_GROUP"));
    }

    private View.OnClickListener lsDel = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.imClose:
                    dialogDel.dismiss();
                    break;
                case R.id.tvYes:
                    modify.deleteTimeApp(mTimeApp);
                    updateRv();
                    dialogDel.dismiss();
                    break;
                case R.id.tvNo:
                    dialogDel.dismiss();
                    break;
            }
        }
    };

    private void updateRvGroup(){
        listGroup = groupAppModify.getListGroup();
        groupDialogAdapter = new GroupDialogAdapter(getContext(),listGroup);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),1);
        rvGroup.setLayoutManager(gridLayoutManager);
        rvGroup.setAdapter(groupDialogAdapter);
    }


    private void updateRv() {
        listTimeApp = modify.getListTime();
        timeAdapter = new TimeAdapter(getContext(), listTimeApp);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);
        rvTime.setLayoutManager(gridLayoutManager);
        rvTime.setAdapter(timeAdapter);
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void scheduleNotification(int id,int hour, int minute, String repeatDay, String app) {

        notificationIntent = new Intent(getActivity(), AlarmReceiver.class);
        notificationIntent.putExtra(AppUtil.PACKAGENAME,app);

        pendingIntent = PendingIntent.getBroadcast(getActivity(), id, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void setAlarm(int hour, int minute) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

    }

    public void cancelAlarm(int id) {
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent1 = new Intent(getActivity(), AlarmReceiver.class);
        PendingIntent pendingIntent1 = PendingIntent.getBroadcast(getActivity(), id, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent1);
        }
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
