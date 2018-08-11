package com.example.pc.app_lock.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.pc.app_lock.R;
import com.example.pc.app_lock.data.AppInfo;
import com.example.pc.app_lock.data.TimeApp;
import com.example.pc.app_lock.mydatabase.GroupAppModify;
import com.example.pc.app_lock.mydatabase.TimeModify;

import java.util.ArrayList;

public class TimeAdapter extends RecyclerView.Adapter<TimeAdapter.ViewHolder> {

    private ArrayList<TimeApp> listTimeApp;
    private Context context;
    private TimeModify modify;
    private GroupAppModify groupAppModify;

    public TimeAdapter(Context context, ArrayList<TimeApp> listTimeApp) {
        this.context = context;
        this.listTimeApp = listTimeApp;
        modify = new TimeModify(context);
        groupAppModify = new GroupAppModify(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_time, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final TimeApp timeApp = listTimeApp.get(position);
        final int state = timeApp.getState();
        holder.tvTime.setText(timeApp.getHour() + ":" + subTime(timeApp.getMin()));
        if (state == 0) {
            holder.imState.setImageResource(R.drawable.off);
        } else holder.imState.setImageResource(R.drawable.on);

        if (timeApp.getId_group()!=-1){
            holder.tvNameGroup.setText(groupAppModify.getGroupById(timeApp.getId_group()).getNameGroup());
        }


        String day = timeApp.getDay();
        if (day.length() > 0) {
            String[] listDay = splitString(day);
            for (String mDay : listDay) {
                switch (mDay) {
                    case "2":
                        holder.lyMon.setVisibility(View.VISIBLE);
                        holder.tvMon.setTextColor(context.getResources().getColor(R.color.blue_light));
                        break;
                    case "3":
                        holder.lyTue.setVisibility(View.VISIBLE);
                        holder.tvTue.setTextColor(context.getResources().getColor(R.color.blue_light));
                        break;
                    case "4":
                        holder.lyWed.setVisibility(View.VISIBLE);
                        holder.tvWed.setTextColor(context.getResources().getColor(R.color.blue_light));
                        break;
                    case "5":
                        holder.lyThur.setVisibility(View.VISIBLE);
                        holder.tvThur.setTextColor(context.getResources().getColor(R.color.blue_light));
                        break;
                    case "6":
                        holder.lyFri.setVisibility(View.VISIBLE);
                        holder.tvFri.setTextColor(context.getResources().getColor(R.color.blue_light));
                        break;
                    case "7":
                        holder.lySat.setVisibility(View.VISIBLE);
                        holder.tvSat.setTextColor(context.getResources().getColor(R.color.blue_light));
                        break;
                    case "8":
                        holder.lySun.setVisibility(View.VISIBLE);
                        holder.tvSun.setTextColor(context.getResources().getColor(R.color.blue_light));
                        break;
                }
            }
        }



        holder.imShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.lyGroup.setVisibility(View.VISIBLE);
                holder.lyDel.setVisibility(View.VISIBLE);
                holder.lySun.setVisibility(View.VISIBLE);
                holder.lySat.setVisibility(View.VISIBLE);
                holder.lyFri.setVisibility(View.VISIBLE);
                holder.lyThur.setVisibility(View.VISIBLE);
                holder.lyWed.setVisibility(View.VISIBLE);
                holder.lyTue.setVisibility(View.VISIBLE);
                holder.lyMon.setVisibility(View.VISIBLE);
                holder.imShow.setVisibility(View.GONE);
                holder.view.setVisibility(View.VISIBLE);

            }
        });

        holder.tvTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickedListener != null) {
                    onItemClickedListener.onTvTimeClick(timeApp);
                }
            }
        });
        holder.imState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (state == 0) {
                    timeApp.setState(1);
                    holder.imState.setImageResource(R.drawable.on);
                } else {
                    timeApp.setState(0);
                    holder.imState.setImageResource(R.drawable.off);
                }
              if (onItemClickedListener!=null){
                    onItemClickedListener.onImStateClick(timeApp);
              }

            }
        });
        holder.lyDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.lyGroup.setVisibility(View.GONE);
                holder.lyDel.setVisibility(View.GONE);
                holder.imShow.setVisibility(View.VISIBLE);
                holder.view.setVisibility(View.GONE);
                String full = " 2 3 4 5 6 7 8";
                if (timeApp.getDay().length() > 0) {
                    String[] listDay = splitString(timeApp.getDay());
                    for (String mDay : listDay) {
                        full = full.replace(mDay, "");
                    }
                }

                String[] listOut = splitString(full);
                for (String mDay : listOut) {
                    switch (mDay) {
                        case "2":
                            holder.lyMon.setVisibility(View.GONE);
                            break;
                        case "3":
                            holder.lyTue.setVisibility(View.GONE);
                            break;
                        case "4":
                            holder.lyWed.setVisibility(View.GONE);
                            break;
                        case "5":
                            holder.lyThur.setVisibility(View.GONE);
                            break;
                        case "6":
                            holder.lyFri.setVisibility(View.GONE);
                            break;
                        case "7":
                            holder.lySat.setVisibility(View.GONE);
                            break;
                        case "8":
                            holder.lySun.setVisibility(View.GONE);
                            break;
                    }
                }
            }
        });
        holder.imDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickedListener != null) {
                    onItemClickedListener.onImDelClick(timeApp);
                }
            }
        });
        holder.lyGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickedListener != null) {
                    onItemClickedListener.onLyGroupClick(timeApp);
                }
            }
        });

        holder.lyMon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String day;
                if (timeApp.getDay().contains("2")) {
                    holder.tvMon.setTextColor(context.getResources().getColor(R.color.textday));
                    day = timeApp.getDay().replace(" 2", "");
                } else {
                    holder.tvMon.setTextColor(context.getResources().getColor(R.color.blue_light));
                    day = timeApp.getDay().concat(" 2");
                }
                timeApp.setDay(day);
                modify.updateTime(timeApp);
                if (onItemClickedListener!=null){
                    onItemClickedListener.onlyDay(timeApp);
                }
            }
        });
        holder.lyTue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String day;
                if (timeApp.getDay().contains("3")) {
                    holder.tvTue.setTextColor(context.getResources().getColor(R.color.textday));
                    day = timeApp.getDay().replace(" 3", "");
                } else {
                    holder.tvTue.setTextColor(context.getResources().getColor(R.color.blue_light));
                    day = timeApp.getDay().concat(" 3");
                }
                timeApp.setDay(day);
                modify.updateTime(timeApp);
                if (onItemClickedListener!=null){
                    onItemClickedListener.onlyDay(timeApp);
                }
            }
        });
        holder.lyWed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String day;
                if (timeApp.getDay().contains("4")) {
                    holder.tvWed.setTextColor(context.getResources().getColor(R.color.textday));
                    day = timeApp.getDay().replace(" 4", "");
                } else {
                    holder.tvWed.setTextColor(context.getResources().getColor(R.color.blue_light));
                    day = timeApp.getDay().concat(" 4");
                }
                timeApp.setDay(day);
                modify.updateTime(timeApp);
                if (onItemClickedListener!=null){
                    onItemClickedListener.onlyDay(timeApp);
                }
            }
        });
        holder.lyThur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String day;
                if (timeApp.getDay().contains("5")) {
                    holder.tvThur.setTextColor(context.getResources().getColor(R.color.textday));
                    day = timeApp.getDay().replace(" 5", "");
                } else {
                    holder.tvThur.setTextColor(context.getResources().getColor(R.color.blue_light));
                    day = timeApp.getDay().concat(" 5");
                }
                timeApp.setDay(day);
                modify.updateTime(timeApp);
                if (onItemClickedListener!=null){
                    onItemClickedListener.onlyDay(timeApp);
                }
            }
        });
        holder.lyFri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String day;
                if (timeApp.getDay().contains("6")) {
                    holder.tvFri.setTextColor(context.getResources().getColor(R.color.textday));
                    day = timeApp.getDay().replace(" 6", "");
                } else {
                    holder.tvFri.setTextColor(context.getResources().getColor(R.color.blue_light));
                    day = timeApp.getDay().concat(" 6");
                }
                timeApp.setDay(day);
                modify.updateTime(timeApp);
                if (onItemClickedListener!=null){
                    onItemClickedListener.onlyDay(timeApp);
                }
            }
        });

        holder.lySat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String day;
                if (timeApp.getDay().contains("7")) {
                    holder.tvSat.setTextColor(context.getResources().getColor(R.color.textday));
                    day = timeApp.getDay().replace(" 7", "");
                } else {
                    holder.tvSat.setTextColor(context.getResources().getColor(R.color.blue_light));
                    day = timeApp.getDay().concat(" 7");
                }
                timeApp.setDay(day);
                modify.updateTime(timeApp);
                if (onItemClickedListener!=null){
                    onItemClickedListener.onlyDay(timeApp);
                }
            }
        });
        holder.lySun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String day;
                if (timeApp.getDay().contains("8")) {
                    holder.tvSun.setTextColor(context.getResources().getColor(R.color.textday));
                    day = timeApp.getDay().replace(" 8", "");
                } else {
                    holder.tvSun.setTextColor(context.getResources().getColor(R.color.blue_light));
                    day = timeApp.getDay().concat(" 8");
                }
                timeApp.setDay(day);
                modify.updateTime(timeApp);
                if (onItemClickedListener!=null){
                    onItemClickedListener.onlyDay(timeApp);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listTimeApp.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View view;
        TextView tvTime, tvSun, tvMon, tvTue, tvWed, tvThur, tvFri, tvSat,tvNameGroup;
        ImageView imState, imDel, imShow;
        RelativeLayout lySun, lyMon, lyTue, lyWed, lyThur, lyFri, lySat, lyGroup, lyDel;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView.findViewById(R.id.view);
            tvNameGroup = itemView.findViewById(R.id.tvNameGroup);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvSun = itemView.findViewById(R.id.tvSun);
            tvMon = itemView.findViewById(R.id.tvMon);
            tvTue = itemView.findViewById(R.id.tvTue);
            tvWed = itemView.findViewById(R.id.tvWed);
            tvThur = itemView.findViewById(R.id.tvThur);
            tvFri = itemView.findViewById(R.id.tvFri);
            tvSat = itemView.findViewById(R.id.tvSat);

            imState = itemView.findViewById(R.id.imState);
            imDel = itemView.findViewById(R.id.imDel);
            imShow = itemView.findViewById(R.id.imShow);

            lyGroup = itemView.findViewById(R.id.lyGroup);
            lyDel = itemView.findViewById(R.id.lyDel);
            lySun = itemView.findViewById(R.id.lySun);
            lyMon = itemView.findViewById(R.id.lyMon);
            lyTue = itemView.findViewById(R.id.lyTue);
            lyWed = itemView.findViewById(R.id.lyWed);
            lyThur = itemView.findViewById(R.id.lyThur);
            lyFri = itemView.findViewById(R.id.lyFri);
            lySat = itemView.findViewById(R.id.lySat);

        }
    }

    public interface OnItemClickedListener {
        void onTvTimeClick(TimeApp timeApp);

        void onImDelClick(TimeApp timeApp);

        void onLyGroupClick(TimeApp timeApp);

        void onImStateClick(TimeApp timeApp);

        void onlyDay(TimeApp timeApp);
    }

    public TimeAdapter.OnItemClickedListener onItemClickedListener;

    public void setOnItemClickedListener(TimeAdapter.OnItemClickedListener
                                                 onItemClickedListener) {
        this.onItemClickedListener = onItemClickedListener;
    }

    private String[] splitString(String day) {
        String[] values = day.split("");
        return values;
    }

    private String subTime(int x) {
        String rs = "";
        if (x < 10)
            rs = "0" + x;
        else rs = "" + x;
        return rs;
    }
}
