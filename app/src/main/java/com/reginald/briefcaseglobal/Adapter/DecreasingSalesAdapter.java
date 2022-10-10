package com.reginald.briefcaseglobal.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.reginald.briefcaseglobal.Model.DecreasingSalesModel;
import com.reginald.briefcaseglobal.ProblematicItemActivity;
import com.reginald.briefcaseglobal.R;

import java.util.List;

public class DecreasingSalesAdapter extends RecyclerView.Adapter<DecreasingSalesAdapter.ViewHolder> {

    private Context context;
    private List<DecreasingSalesModel> list;
    public DecreasingSalesAdapter(Context context, List<DecreasingSalesModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.single_decreasing_sales_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DecreasingSalesModel model = list.get(position);
        holder.storeName.setText(model.getStoreName());
        holder.diff.setText("Diff: "+model.getDiff());
        holder.cym.setText("CYM: "+model.getCym());
        holder.lym.setText("LYM: "+model.getLym());
        holder.code.setText("CODE: "+model.getCode());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                context.startActivity(new Intent(context, ProblematicItemActivity.class)
                        .putExtra("name", model.getStoreName())
                        .putExtra("code", model.getCode())
                );
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView storeName,lym,cym,diff,code;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            storeName = itemView.findViewById(R.id.storeName);
            lym = itemView.findViewById(R.id.lym);
            cym = itemView.findViewById(R.id.cym);
            diff = itemView.findViewById(R.id.diff);
            code = itemView.findViewById(R.id.code);
        }
    }
}
