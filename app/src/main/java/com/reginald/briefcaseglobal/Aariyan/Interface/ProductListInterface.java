package com.reginald.briefcaseglobal.Aariyan.Interface;

import androidx.lifecycle.MutableLiveData;

import com.reginald.briefcaseglobal.Aariyan.Model.ProductModel;

import java.util.List;

public interface ProductListInterface {
    void listOfProduct(List<ProductModel> listOfProducts);
    void error(String errorMessage);
}
