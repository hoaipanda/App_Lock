package com.example.pc.app_lock.adapter;

import android.content.Context;
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
import com.example.pc.app_lock.data.AppOfGroup;
import com.example.pc.app_lock.mydatabase.AppModify;

import java.util.ArrayList;

public class AddGroupAdapter extends RecyclerView.Adapter<AddGroupAdapter.ViewHolder>{
    private Context context;
    private ArrayList<AppInfo> listApp;
    public AddGroupAdapter(Context context, ArrayList<AppInfo> listApp){
        this.context = context;
        this.listApp = listApp;
    }
    @NonNull
    @Override
    public AddGroupAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_main, parent, false);
        return new AddGroupAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AddGroupAdapter.ViewHolder holder, int position) {
        final AppInfo appInfo = listApp.get(position);
        holder.tvNameApp.setText(AppUtil.getAppNameFromPkgName(context,appInfo.getPackageName()));
        holder.imIcon.setImageDrawable(AppUtil.getIconFromPackage(context,appInfo.getPackageName()));
        if (appInfo.isCheck()){
            holder.imKey.setImageResource(R.drawable.lock);
        }else {
            holder.imKey.setImageResource(R.drawable.open_lock);
        }
        holder.imKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

    public AddGroupAdapter.OnItemClickedListener onItemClickedListener;

    public void setOnItemClickedListener(AddGroupAdapter.OnItemClickedListener onItemClickedListener) {
        this.onItemClickedListener = onItemClickedListener;
    }
}
