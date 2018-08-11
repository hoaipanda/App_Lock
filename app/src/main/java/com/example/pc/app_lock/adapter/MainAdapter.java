package com.example.pc.app_lock.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pc.app_lock.AppUtil;
import com.example.pc.app_lock.R;
import com.example.pc.app_lock.SharePreLock;
import com.example.pc.app_lock.data.AppInfo;

import java.util.ArrayList;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
    private Context context;
    private ArrayList<AppInfo> listApp;
    public MainAdapter(Context context, ArrayList<AppInfo> listApp){
        this.context = context;
        this.listApp = listApp;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_main, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final AppInfo appInfo = listApp.get(position);
        holder.tvNameApp.setText(appInfo.getAppname());
        holder.imIcon.setImageDrawable(appInfo.getIcon());
        final boolean isLock = SharePreLock.getInstance(context).getAppLock(appInfo.getPackageName());
        if (isLock){
            holder.imKey.setImageResource(R.drawable.lock);
        }else {
            holder.imKey.setImageResource(R.drawable.open_lock);
        }
        holder.imKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLock){
                    appInfo.setCheck(false);
                }else{
                    appInfo.setCheck(true);
                }
                if (onItemClickedListener!=null){
                    onItemClickedListener.onItemClick(appInfo);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return listApp.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imIcon;
        TextView tvNameApp;
        TextView tvSubApp;
        ImageView imKey;
        public ViewHolder(View itemView) {
            super(itemView);
            imIcon = itemView.findViewById(R.id.imIcon);
            tvNameApp = itemView.findViewById(R.id.tvNameApp);
            tvSubApp = itemView.findViewById(R.id.subName);
            imKey = itemView.findViewById(R.id.imKey);
        }
    }

    public interface OnItemClickedListener {
        void onItemClick(AppInfo appInfo);
    }

    public OnItemClickedListener onItemClickedListener;

    public void setOnItemClickedListener(OnItemClickedListener onItemClickedListener) {
        this.onItemClickedListener = onItemClickedListener;
    }
}
