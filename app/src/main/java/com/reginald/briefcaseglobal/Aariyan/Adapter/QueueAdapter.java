package com.reginald.briefcaseglobal.Aariyan.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.reginald.briefcaseglobal.Aariyan.Interface.HeadersInterface;
import com.reginald.briefcaseglobal.Aariyan.Model.HeadersModel;
import com.reginald.briefcaseglobal.LandingActivity;
import com.reginald.briefcaseglobal.R;

import java.util.List;

import retrofit2.http.Header;

public class QueueAdapter extends RecyclerView.Adapter<QueueAdapter.ViewHolder> {

    private Context context;
    private List<HeadersModel> list;
    private HeadersInterface headersInterface;
    public QueueAdapter(Context context, List<HeadersModel> list, HeadersInterface headersInterface) {
        this.context = context;
        this.list = list;
        this.headersInterface = headersInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_single_row, parent,false));
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int index = holder.getAdapterPosition();
        HeadersModel model = list.get(index);
        try {
            holder.itemName.setText(model.getProductName());
            holder.itemPrice.setText(String.valueOf(model.getProductPrice()));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    headersInterface.singleHeaderForPosting(model, position);
                }
            });

        } catch (Exception e) {

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
