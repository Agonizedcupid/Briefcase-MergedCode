package com.reginald.briefcaseglobal.Aariyan.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.reginald.briefcaseglobal.Aariyan.Interface.ItemSelectionInterface;
import com.reginald.briefcaseglobal.Aariyan.Model.HeadersModel;
import com.reginald.briefcaseglobal.Aariyan.Model.LinesModel;
import com.reginald.briefcaseglobal.R;

import java.util.List;

public class AlreadySelectedItemAdapter extends RecyclerView.Adapter<AlreadySelectedItemAdapter.ViewHolder> {

    private Context context;
    private List<HeadersModel> list;
    public AlreadySelectedItemAdapter(Context context, List<HeadersModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_single_row,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HeadersModel model = list.get(position);

        try {
            holder.itemName.setText(model.getProductName());
            holder.itemPrice.setText(model.getProductPrice());
            Log.d("ADAPTER_ERROR", ""+model.getProductPrice());
        }catch (Exception e) {
            Log.d("ADAPTER_ERROR", "onBindViewHolder: "+e.getMessage());
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView itemName, itemPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.itemName);
            itemPrice = itemView.findViewById(R.id.itemPrice);
        }
    }
}
