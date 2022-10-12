package com.reginald.briefcaseglobal.Aariyan.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.reginald.briefcaseglobal.Aariyan.Interface.ClickProduct;
import com.reginald.briefcaseglobal.Aariyan.Model.ProductModel;
import com.reginald.briefcaseglobal.Aariyan.ViewModel.ProductViewModel;
import com.reginald.briefcaseglobal.R;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private Context context;
    List<ProductModel> listOfProduct;
    private ClickProduct clickProduct;

    public ItemAdapter(Context context, List<ProductModel> listOfProduct, ClickProduct clickProduct) {
        this.context = context;
        this.listOfProduct = listOfProduct;
        this.clickProduct = clickProduct;
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

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView itemName, itemPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.itemName);
            itemPrice = itemView.findViewById(R.id.itemPrice);
        }
    }
}
