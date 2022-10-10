package com.reginald.briefcaseglobal.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.reginald.briefcaseglobal.MemoHome;
import com.reginald.briefcaseglobal.Model.CustomerModel;
import com.reginald.briefcaseglobal.R;

import java.util.ArrayList;
import java.util.List;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.ViewHolder> implements Filterable {

    private Context context;
    private List<CustomerModel> list;
    private List<CustomerModel> copiedList;
    public CustomerAdapter( Context context,List<CustomerModel> list) {
        this.context = context;
        this.list = list;
        copiedList = new ArrayList<>(list);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.single_customer_name,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CustomerModel model = list.get(position);
        holder.customerName.setText(model.getStoreName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MemoHome.class);
                intent.putExtra("name", model.getStoreName());
                intent.putExtra("code", model.getCustomerPastelCode());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<CustomerModel> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(copiedList);
            } else {
                String query = constraint.toString().toLowerCase().trim();

                for (CustomerModel model : copiedList) {
                    if (model.getStoreName().toLowerCase().contains(query)) {
                        filteredList.add(model);
                    } else {
                        //Toast.makeText(context, "No product found!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

            list.clear();
            list.addAll((List) filterResults.values);
            notifyDataSetChanged();

        }
    };



    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView customerName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            customerName = itemView.findViewById(R.id.customerName);
        }
    }
}
