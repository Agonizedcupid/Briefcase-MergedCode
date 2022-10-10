package com.reginald.briefcaseglobal.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.reginald.briefcaseglobal.AddItemToSpecialActivity;
import com.reginald.briefcaseglobal.Model.ProblematicModel;
import com.reginald.briefcaseglobal.ProblematicItemActivity;
import com.reginald.briefcaseglobal.R;

import java.util.List;

public class ProblematicAdapter extends RecyclerView.Adapter<ProblematicAdapter.ViewHolder> {

    private Context context;
    private List<ProblematicModel> list;

    public ProblematicAdapter(Context context, List<ProblematicModel>list) {
        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.single_problematic_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProblematicModel model = list.get(position);
        holder.prM.setText("PrM: "+model.getDecPriorMonth());
        holder.pastelDescription.setText(model.getPastelDescription());
        holder.diff.setText("Diff: "+model.getDecDifference());
        holder.lm.setText("LM: "+model.getDecLastMonth());
        holder.ly.setText("LY: "+model.getDecLastYear());
        holder.pastelCode.setText("Code: "+model.getPastelCode());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                context.startActivity(new Intent(context, AddItemToSpecialActivity.class)
                        .putExtra("customercode", model.getCustomerPastelCode())
                        .putExtra("itemcode", model.getPastelCode())
                        .putExtra("storename", model.getStoreName())
                        .putExtra("getPastelDescription", model.getPastelDescription())
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

        private TextView pastelDescription,pastelCode,ly,lm,diff,prM;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            pastelDescription = itemView.findViewById(R.id.pastelDescription);
            pastelCode = itemView.findViewById(R.id.pastelCode);
            ly = itemView.findViewById(R.id.ly);
            lm = itemView.findViewById(R.id.lm);
            diff = itemView.findViewById(R.id.diffs);
            prM = itemView.findViewById(R.id.prM);
        }
    }
}
