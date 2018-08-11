package com.example.pc.app_lock.adapter;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pc.app_lock.mydatabase.AppModify;
import com.example.pc.app_lock.R;
import com.example.pc.app_lock.data.AppOfGroup;
import com.example.pc.app_lock.data.GroupApp;

import java.util.ArrayList;

public class GroupAppAdapter extends RecyclerView.Adapter<GroupAppAdapter.ViewHolder>  {
    private Context context;
    private ArrayList<GroupApp> listGroup;
    private ArrayList<AppOfGroup> listApp;
    private AppModify modify;
    private int id;

    public GroupAppAdapter(Context context, ArrayList<GroupApp> listGroup) {
        this.context = context;
        this.listGroup = listGroup;
        modify = new AppModify(context);
//        listApp = modify.getListApp();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_group, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final GroupApp groupApp = listGroup.get(position);
        holder.tvNameGroup.setText(groupApp.getNameGroup());
        listApp = modify.getListAppByGroup(groupApp.getId());
        holder.lyApp.removeAllViews();
        if (position == 0) {
            final View view = LayoutInflater.from(context).inflate(R.layout.item_text, holder.lyApp, false);
            holder.lyApp.addView(view);
            holder.imIconGroup.setImageResource(R.drawable.open_lock);
        } else {
            if (position==1) {
                holder.imIconGroup.setImageResource(R.drawable.guest);
            }
            if (listApp.size() > 6) {
                for (int i = 0; i < 6; i++) {
                    Drawable icon = null;
                    try {
                        final View view = LayoutInflater.from(context).inflate(R.layout.item_app_group, holder.lyApp, false);
                        ImageView img = view.findViewById(R.id.imApp);
                        icon = context.getPackageManager().getApplicationIcon(listApp.get(i).getPackageName());
                        img.setImageDrawable(icon);
                        holder.lyApp.addView(view);
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }

                }
            } else {
                for (int i = 0; i < listApp.size(); i++) {
                    Drawable icon = null;
                    try {
                        final View view = LayoutInflater.from(context).inflate(R.layout.item_app_group, holder.lyApp, false);
                        ImageView img = view.findViewById(R.id.imApp);
                        icon = context.getPackageManager().getApplicationIcon(listApp.get(i).getPackageName());
                        img.setImageDrawable(icon);
                        holder.lyApp.addView(view);
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }

                }
            }

            holder.lyApp.invalidate();
        }

        if (groupApp.isState()==0){
            holder.tvNameGroup.setTextColor(context.getResources().getColor(R.color.textday));
        }else {
            holder.tvNameGroup.setTextColor(context.getResources().getColor(R.color.blue_light));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickedListener!=null){
                    onItemClickedListener.onItemViewClick(groupApp);
                }
            }
        });

       holder.imSetting.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               PopupMenu pm = new PopupMenu(context, v);
               pm.getMenuInflater().inflate(R.menu.menu_add, pm.getMenu());
               pm.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                   @Override
                   public boolean onMenuItemClick(MenuItem item) {
                       switch (item.getItemId()) {
                           case R.id.imEdit:
                               if (onItemClickedListener!=null){
                                   onItemClickedListener.onEditClick(groupApp.getId());
                               }
                               return true;
                           case R.id.imDel:
                               if (onItemClickedListener!=null){
                                   onItemClickedListener.onDelClick(groupApp.getId());
                               }
                               return true;
                           default:
                               return false;
                       }
                   }
               });
               pm.show();

           }
       });

    }

    @Override
    public int getItemCount() {
        return listGroup.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNameGroup;
        LinearLayout lyApp;
        ImageView imSetting;
        ImageView imIconGroup;
        public ViewHolder(View itemView) {
            super(itemView);
            tvNameGroup = itemView.findViewById(R.id.tvNameGroup);
            lyApp = itemView.findViewById(R.id.lyApp);
            imSetting = itemView.findViewById(R.id.imSetting);
            imIconGroup = itemView.findViewById(R.id.imIconGroup);
        }
    }

    public interface OnItemClickedListener {
        void onItemViewClick(GroupApp groupApp);
        void onEditClick(int id);
        void onDelClick(int id);
    }

    public GroupAppAdapter.OnItemClickedListener onItemClickedListener;

    public void setOnItemClickedListener(GroupAppAdapter.OnItemClickedListener onItemClickedListener) {
        this.onItemClickedListener = onItemClickedListener;
    }

}
