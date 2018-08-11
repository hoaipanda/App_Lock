package com.example.pc.app_lock.fragment;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pc.app_lock.AppUtil;
import com.example.pc.app_lock.SharePreLock;
import com.example.pc.app_lock.data.AppOfGroup;
import com.example.pc.app_lock.mydatabase.AppModify;
import com.example.pc.app_lock.mydatabase.GroupAppModify;
import com.example.pc.app_lock.R;
import com.example.pc.app_lock.activity.AddGroupActivity;
import com.example.pc.app_lock.activity.MainActivity;
import com.example.pc.app_lock.adapter.GroupAppAdapter;
import com.example.pc.app_lock.data.GroupApp;

import java.util.ArrayList;

public class GroupFragment extends Fragment {

    private ArrayList<GroupApp> listGroup = new ArrayList<>();
    private RecyclerView rvGroup;
    private GroupAppAdapter adapter;
    private GroupAppModify groupAppModify;
    private AppModify appModify;
    private FloatingActionButton imAdd;
    private Dialog dialog, dialogDel,dialogActive;
    private EditText edNameGroup;
    private View view;
    private int idDel;
    private ArrayList<AppOfGroup> listAppOfGroup = new ArrayList<>();


    public GroupFragment() {
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateRv();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_group, container, false);
        rvGroup = view.findViewById(R.id.rvGroup);
        imAdd = view.findViewById(R.id.imAdd);
        groupAppModify = new GroupAppModify(getContext());
        appModify = new AppModify(getContext());

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction("UPDATE_GROUP");
        getActivity().registerReceiver(receiver,filter);
        Log.e("hoaiii","vaoday");
        updateRv();
        imAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        adapter.setOnItemClickedListener(new GroupAppAdapter.OnItemClickedListener() {
            @Override
            public void onItemViewClick(GroupApp groupApp) {
                showDialogActive(groupApp);
            }

            @Override
            public void onEditClick(int id) {
                Intent intent = new Intent(getContext(), AddGroupActivity.class);
                intent.putExtra(AppUtil.ID_GROUP, id);
                startActivity(intent);
            }

            @Override
            public void onDelClick(int id) {
                idDel = id;
                showDialogDel();

            }


        });

    }

    private void updateRv() {
        listGroup = groupAppModify.getListGroup();
        adapter = new GroupAppAdapter(getContext(), listGroup);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);
        rvGroup.setLayoutManager(gridLayoutManager);
        rvGroup.setAdapter(adapter);

    }

    private void showDialogActive(final GroupApp groupApp) {
        dialogActive = new Dialog(getActivity());
        dialogActive.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window view = ((Dialog) dialogActive).getWindow();
        view.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogActive.setContentView(R.layout.dialog_change_state);
        dialogActive.setCancelable(false);
        ImageView imClose = dialogActive.findViewById(R.id.imClose);
        TextView tvNo = dialogActive.findViewById(R.id.tvNo);
        TextView tvYes = dialogActive.findViewById(R.id.tvYes);
        TextView tvReport = dialogActive.findViewById(R.id.tvReport);
        if (groupApp.isState()==0){
            tvReport.setText("Would you like to activate this group?");
        }else {
            tvReport.setText("Would you like to un activate this group?");
        }
        imClose.setOnClickListener(lsActive);
        tvNo.setOnClickListener(lsActive);
        tvYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogActive.dismiss();
                if (groupApp.isState()==0){
                    groupApp.setState(1);
                    for (GroupApp groupApp1:listGroup){
                        if (groupApp1!=groupApp){
                            groupApp1.setState(0);
                            groupAppModify.updateGroup(groupApp1);
                            ArrayList<AppOfGroup> list = appModify.getListAppByGroup(groupApp1.getId());
                            for (AppOfGroup appOfGroup:list){
                                SharePreLock.getInstance(getContext()).addAppLock(appOfGroup.getPackageName(),false);
                            }
                        }
                    }
                    listAppOfGroup = appModify.getListAppByGroup(groupApp.getId());
                    for (AppOfGroup appOfGroup:listAppOfGroup){
                        SharePreLock.getInstance(getContext()).addAppLock(appOfGroup.getPackageName(),true);
                    }
                    Toast.makeText(getActivity(), "The group has been  activated", Toast.LENGTH_LONG).show();
                }else {
                    groupApp.setState(0);
                    listAppOfGroup = appModify.getListAppByGroup(groupApp.getId());
                    for (AppOfGroup appOfGroup:listAppOfGroup){
                        SharePreLock.getInstance(getContext()).addAppLock(appOfGroup.getPackageName(),false);
                    }
                    Toast.makeText(getActivity(), "This group is disabled", Toast.LENGTH_LONG).show();
                }
                groupAppModify.updateGroup(groupApp);
                adapter.notifyDataSetChanged();
            }
        });
        dialogActive.show();
    }
    private void showDialogDel() {
        dialogDel = new Dialog(getActivity());
        dialogDel.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window view = ((Dialog) dialogDel).getWindow();
        view.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogDel.setContentView(R.layout.dialog_delete);
        dialogDel.setCancelable(false);
        ImageView imClose = dialogDel.findViewById(R.id.imClose);
        TextView tvNo = dialogDel.findViewById(R.id.tvNo);
        TextView tvYes = dialogDel.findViewById(R.id.tvYes);
        imClose.setOnClickListener(lsDel);
        tvNo.setOnClickListener(lsDel);
        tvYes.setOnClickListener(lsDel);
        dialogDel.show();
    }

    private void showDialog() {
        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window view = ((Dialog) dialog).getWindow();
        view.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_add_group);
        dialog.setCancelable(false);
        edNameGroup = dialog.findViewById(R.id.edNameStory);
        ImageView imClose = dialog.findViewById(R.id.imClose);
        TextView tvCreate = dialog.findViewById(R.id.tvCreate);
        imClose.setOnClickListener(lsDialog);
        tvCreate.setOnClickListener(lsDialog);
        dialog.show();
    }

    private View.OnClickListener lsActive = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.imClose:
                    dialogActive.dismiss();
                    break;
                case R.id.tvNo:
                    dialogActive.dismiss();
                    break;
            }
        }
    };

    private View.OnClickListener lsDel = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.imClose:
                    dialogDel.dismiss();
                    break;
                case R.id.tvNo:
                    dialogDel.dismiss();
                    break;
                case R.id.tvYes:
                    dialogDel.dismiss();
                    groupAppModify.deleteGroupApp(idDel);
                    updateRv();
                    Toast.makeText(getActivity(), "Delete successful", Toast.LENGTH_LONG).show();
                    break;

            }
        }
    };

    private View.OnClickListener lsDialog = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.imClose:
                    dialog.dismiss();
                    break;
                case R.id.tvCreate:
                    String name = edNameGroup.getText().toString();
                    if (name.length() > 0) {
                        groupAppModify.addGroup(new GroupApp(name, 0));
                        Intent intent = new Intent(getActivity(), AddGroupActivity.class);
                        ArrayList<GroupApp> list = groupAppModify.getListGroup();
                        intent.putExtra(AppUtil.ID_GROUP, list.get(list.size() - 1).getId());
                        intent.putExtra(AppUtil.TYPE_ADD, AppUtil.TYPE_CREATE);
                        startActivity(intent);
                        dialog.dismiss();
                    } else {
                        Toast.makeText(getActivity(), "No name selected", Toast.LENGTH_LONG).show();
                        Vibrator vibrator = (Vibrator) getActivity().getSystemService(MainActivity.VIBRATOR_SERVICE);
                        vibrator.vibrate(500);
                    }
                    break;
            }
        }
    };


}
