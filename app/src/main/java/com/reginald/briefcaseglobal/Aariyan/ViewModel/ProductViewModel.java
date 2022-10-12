package com.reginald.briefcaseglobal.Aariyan.ViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.reginald.briefcaseglobal.Aariyan.Interface.ProductListInterface;
import com.reginald.briefcaseglobal.Aariyan.Interface.RestApis;
import com.reginald.briefcaseglobal.Aariyan.Model.ProductModel;
import com.reginald.briefcaseglobal.Aariyan.Repository.ProductRepository;

import java.util.List;

public class ProductViewModel extends ViewModel {

    private MutableLiveData<List<ProductModel>> listOfProducts;
    public void init (RestApis restApis, String customerCode) {
        if (listOfProducts != null) {
            return;
        }
        listOfProducts = ProductRepository.getInstance().getListOfProduct(restApis,customerCode);
    }

    public MutableLiveData<List<ProductModel>> getListOfProducts() {
        return listOfProducts;
    }
}
