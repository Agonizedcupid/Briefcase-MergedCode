package com.reginald.briefcaseglobal.Aariyan.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.reginald.briefcaseglobal.Aariyan.Interface.ClickProduct;
import com.reginald.briefcaseglobal.Aariyan.Model.LinesModel;
import com.reginald.briefcaseglobal.Aariyan.Model.ProductModel;
import com.reginald.briefcaseglobal.Aariyan.ViewModel.ProductViewModel;
import com.reginald.briefcaseglobal.R;

import java.util.ArrayList;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> implements Filterable {

    private Context context;
    List<ProductModel> listOfProduct;
    private ClickProduct clickProduct;
    private List<ProductModel> copiedList;

    public ItemAdapter(Context context, List<ProductModel> listOfProduct, ClickProduct clickProduct) {
        this.context = context;
        this.listOfProduct = listOfProduct;
        this.clickProduct = clickProduct;
        copiedList = new ArrayList<>(listOfProduct);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_single_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductModel model = listOfProduct.get(position);
        try {
            holder.itemName.setText("" + model.getStrDesc());
            holder.itemPrice.setText(model.getCost());
        } catch (Exception e) {
            Log.d("ERROR_HAPPENS", "onBindViewHolder: " + model.getCost());
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickProduct.carryModel(model);
            }
        });

    }

    @Override
    public int getItemCount() {
        return listOfProduct.size();
    }
    private final Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<ProductModel> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(copiedList);
            } else {
                String query = constraint.toString().toLowerCase().trim();

                for (ProductModel model : copiedList) {
                    if (model.getStrDesc().toLowerCase().contains(query)) {
                        filteredList.add(model);
                    }
//                    else {
//                        Toast.makeText(context, "No Apps found!", Toast.LENGTH_SHORT).show();
//                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

            listOfProduct.clear();
            listOfProduct.addAll((List) filterResults.values);
            notifyDataSetChanged();

        }
    };

    @Override
    public Filter getFilter() {
        return filter;
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
