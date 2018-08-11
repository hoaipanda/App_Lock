package com.example.pc.app_lock.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.comix.overwatch.HiveProgressView;
import com.example.pc.app_lock.AppUtil;
import com.example.pc.app_lock.R;
import com.example.pc.app_lock.SharePreLock;
import com.example.pc.app_lock.adapter.AddGroupAdapter;
import com.example.pc.app_lock.adapter.MainAdapter;
import com.example.pc.app_lock.data.AppInfo;
import com.example.pc.app_lock.data.AppOfGroup;
import com.example.pc.app_lock.data.GroupApp;
import com.example.pc.app_lock.mydatabase.AppModify;
import com.example.pc.app_lock.mydatabase.GroupAppModify;

import java.util.ArrayList;
import java.util.Arrays;

public class AddGroupActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView imSave;
    private Toolbar toolbar;
    private HiveProgressView load;
    private RecyclerView rvAddGroup;
    private AddGroupAdapter addGroupAdapter;
    private AppModify appModify;
    private ArrayList<AppInfo> listApp = new ArrayList<>();
    private int idGroup;
    private GroupAppModify groupAppModify;
    private GroupApp groupApp;
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group);
        load = findViewById(R.id.load);
        rvAddGroup = findViewById(R.id.rvAddGroup);
        imSave = findViewById(R.id.imSave);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        appModify = new AppModify(this);
        groupAppModify = new GroupAppModify(this);

        Intent intent = getIntent();
        idGroup = intent.getIntExtra(AppUtil.ID_GROUP, -1);
        groupApp = groupAppModify.getGroupById(idGroup);


        LoadApp loadApp = new LoadApp();
        loadApp.execute();

        imSave.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imSave:
                appModify.deleteGroupApp(idGroup);
                for (AppInfo appInfo : listApp) {
                    if (appInfo.isCheck()) {
                        if (groupApp.isState()==1){
                            SharePreLock.getInstance(AddGroupActivity.this).addAppLock(appInfo.getPackageName(),true);
                        }
                        appModify.addApp(new AppOfGroup(appInfo.getPackageName(), idGroup));
                        Toast.makeText(AddGroupActivity.this, "Save apps for group!", Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    private class LoadApp extends AsyncTask<String, String, ArrayList<AppInfo>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            rvAddGroup.setVisibility(View.GONE);
            load.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<AppInfo> doInBackground(String... strings) {
            return AppUtil.getListApp(AddGroupActivity.this);
        }

        @Override
        protected void onPostExecute(ArrayList<AppInfo> appInfos) {
            super.onPostExecute(appInfos);
            rvAddGroup.setVisibility(View.VISIBLE);
            load.setVisibility(View.GONE);
            listApp = appInfos;

            if (idGroup != -1) {
                ArrayList<AppOfGroup> list = appModify.getListAppByGroup(idGroup);
               for (AppInfo appInfo:listApp){
                   for (AppOfGroup appOfGroup:list){
                       if (appInfo.getPackageName().equals(appOfGroup.getPackageName())){
                           appInfo.setCheck(true);
                       }
                   }
               }

            }



            addGroupAdapter = new AddGroupAdapter(AddGroupActivity.this, listApp);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(AddGroupActivity.this, 1);
            rvAddGroup.setLayoutManager(gridLayoutManager);
            rvAddGroup.setAdapter(addGroupAdapter);
            addGroupAdapter.setOnItemClickedListener(new AddGroupAdapter.OnItemClickedListener() {
                @Override
                public void onItemClick(AppInfo appInfo) {
                    if (appInfo.isCheck()) {
                        appInfo.setCheck(false);

                    } else {
                        appInfo.setCheck(true);
                    }
                    addGroupAdapter.notifyDataSetChanged();
                }
            });



        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
