package com.reginald.briefcaseglobal.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.reginald.briefcaseglobal.Model.OrderModel;
import com.reginald.briefcaseglobal.R;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    private Context context;
    private List<OrderModel> list;
    private onClickListener onClickListener;
    public OrderAdapter(Context context,List<OrderModel> list,onClickListener onClickListener) {
        this.context = context;
        this.list = list;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.order_list_recycler_view,parent,false),onClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderModel model = list.get(position);
        holder.deliveryDate.setText("D.D: "+model.getDeliveryDate());
        holder.invoiceNumber.setText(model.getInvoiceNo());
        holder.orderId.setText(model.getOrderId());
        holder.orderNumber.setText(model.getOrderNo());
        holder.orderDate.setText("O.D: "+model.getOrderDate());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {
        private TextView invoiceNumber,orderId,orderNumber,orderDate,deliveryDate;
        onClickListener onClickListener;
        public ViewHolder(@NonNull View itemView, onClickListener onClickListener) {
            super(itemView);
            invoiceNumber = itemView.findViewById(R.id.invoiceNumber);
            orderId = itemView.findViewById(R.id.orderId);
            orderNumber = itemView.findViewById(R.id.orderNumber);
            orderDate = itemView.findViewById(R.id.orderDate);
            deliveryDate = itemView.findViewById(R.id.deliveryDate);
            this.onClickListener = onClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onClickListener.onItemClick(getAdapterPosition());
        }
    }

    public interface onClickListener {
        void onItemClick(int position);
    }
}
