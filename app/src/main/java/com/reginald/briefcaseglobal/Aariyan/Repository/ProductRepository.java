package com.reginald.briefcaseglobal.Aariyan.Repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.reginald.briefcaseglobal.Aariyan.Database.DatabaseAdapter;
import com.reginald.briefcaseglobal.Aariyan.Interface.ProductListInterface;
import com.reginald.briefcaseglobal.Aariyan.Interface.RestApis;
import com.reginald.briefcaseglobal.Aariyan.Model.ProductModel;
import com.reginald.briefcaseglobal.Aariyan.Networking.ProductFeedback;

import java.util.List;

public class ProductRepository {

    private static ProductRepository productRepository;

    public static ProductRepository getInstance() {
        if (productRepository == null) {
            productRepository = new ProductRepository();
        }
        return productRepository;
    }

    private MutableLiveData<List<ProductModel>> listOfProduct = new MutableLiveData<>();

    public MutableLiveData<List<ProductModel>> getListOfProduct(RestApis apis, String customerCode, DatabaseAdapter databaseAdapter) {
        new ProductFeedback(databaseAdapter).getProductFromServer(new ProductListInterface() {
            @Override
            public void listOfProduct(List<ProductModel> listOfProducts) {
                listOfProduct.setValue(listOfProducts);
            }

            @Override
            public void error(String errorMessage) {
                Log.d("ERROR_HAPPENS", "error: " + errorMessage);
            }
        }, apis, customerCode);
        return listOfProduct;
    }

}
