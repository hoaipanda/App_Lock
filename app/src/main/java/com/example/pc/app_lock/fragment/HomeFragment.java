package com.example.pc.app_lock.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.comix.overwatch.HiveProgressView;
import com.example.pc.app_lock.AppUtil;
import com.example.pc.app_lock.R;
import com.example.pc.app_lock.SharePreLock;
import com.example.pc.app_lock.activity.AddGroupActivity;
import com.example.pc.app_lock.adapter.MainAdapter;
import com.example.pc.app_lock.data.AppInfo;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private ArrayList<AppInfo> listApp = new ArrayList<>();
    private RecyclerView rvMain;
    private MainAdapter adapter;
    private HiveProgressView load;


    public HomeFragment() {
        // Required empty public constructor
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action){
                case "UPDATE_GROUP":
                    LoadApp loadApp = new LoadApp();
                    loadApp.execute();
                    break;
            }
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        rvMain = view.findViewById(R.id.rvMain);
        load = view.findViewById(R.id.load);


        LoadApp loadApp = new LoadApp();
        loadApp.execute();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction("UPDATE_GROUP");
        getActivity().registerReceiver(receiver,filter);

    }

    private class LoadApp extends AsyncTask<String,String,ArrayList<AppInfo>>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            rvMain.setVisibility(View.GONE);
            load.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<AppInfo> doInBackground(String... strings) {
            return AppUtil.getListApp(getContext());
        }

        @Override
        protected void onPostExecute(ArrayList<AppInfo> appInfos) {
            super.onPostExecute(appInfos);
            rvMain.setVisibility(View.VISIBLE);
            load.setVisibility(View.GONE);
            listApp = appInfos;
            adapter = new MainAdapter(getActivity(),listApp);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),1);
            rvMain.setLayoutManager(gridLayoutManager);
            rvMain.setAdapter(adapter);
            adapter.setOnItemClickedListener(new MainAdapter.OnItemClickedListener() {
                @Override
                public void onItemClick(AppInfo appInfo) {
                    if (appInfo.isCheck()){
                        SharePreLock.getInstance(getContext()).addAppLock(appInfo.getPackageName(),true);

                    }else {
                        SharePreLock.getInstance(getContext()).addAppLock(appInfo.getPackageName(),false);
                    }
                    adapter.notifyDataSetChanged();
                }
            });
        }
    }

}
