package com.reginald.briefcaseglobal.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.reginald.briefcaseglobal.CreateMemo;
import com.reginald.briefcaseglobal.LogVisit;
import com.reginald.briefcaseglobal.Model.DailyScheduleModel;
import com.reginald.briefcaseglobal.R;

import java.util.List;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {

    private Context context;
    private List<DailyScheduleModel> list;

    public ScheduleAdapter(Context context, List<DailyScheduleModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.single_schedule_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DailyScheduleModel model = list.get(position);
        holder.date.setText(model.getDate());
        holder.name.setText(model.getStoreName());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("I want to ...")
                        .setCancelable(false)
                        .setPositiveButton("LOG VISIT", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                context.startActivity(new Intent(context, LogVisit.class)
                                        .putExtra("name",model.getStoreName())
                                        .putExtra("code",model.getCode())
                                );
                                dialog.dismiss();

                            }
                        })
                        .setNegativeButton("PLAN VISIT", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        context.startActivity(new Intent(context, CreateMemo.class)
                                .putExtra("name",model.getStoreName())
                                .putExtra("code",model.getCode())
                        );
                        dialog.dismiss();

                    }
                }).setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();

                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();



                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView name,date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.cNames);
            date = itemView.findViewById(R.id.dates);
        }
    }
}
