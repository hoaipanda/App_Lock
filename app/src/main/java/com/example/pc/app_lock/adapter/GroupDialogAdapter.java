package com.example.pc.app_lock.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pc.app_lock.R;
import com.example.pc.app_lock.data.AppInfo;
import com.example.pc.app_lock.data.GroupApp;

import java.util.ArrayList;

public class GroupDialogAdapter extends RecyclerView.Adapter<GroupDialogAdapter.ViewHolder> {
    private Context context;
    private ArrayList<GroupApp> listGroup = new ArrayList<>();
    public GroupDialogAdapter(Context context,ArrayList<GroupApp> listGroup){
        this.context = context;
        this.listGroup = listGroup;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_group_dialog,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final GroupApp groupApp = listGroup.get(position);
        if (position==0){
            holder.imIcon.setImageResource(R.drawable.open_lock);
        }else {
            if (position==1){
                holder.imIcon.setImageResource(R.drawable.guest);
            }
        }
        holder.tvNameGroup.setText(groupApp.getNameGroup());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickedListener!=null){
                    onItemClickedListener.onItemClick(groupApp);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listGroup.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imIcon;
        TextView tvNameGroup;
        public ViewHolder(View itemView) {
            super(itemView);
            imIcon = itemView.findViewById(R.id.imIconGroup);
            tvNameGroup = itemView.findViewById(R.id.tvNameGroup);
        }
    }

    public interface OnItemClickedListener {
        void onItemClick(GroupApp groupApp);
    }

    public GroupDialogAdapter.OnItemClickedListener onItemClickedListener;

    public void setOnItemClickedListener(GroupDialogAdapter.OnItemClickedListener onItemClickedListener) {
        this.onItemClickedListener = onItemClickedListener;
    }
}
