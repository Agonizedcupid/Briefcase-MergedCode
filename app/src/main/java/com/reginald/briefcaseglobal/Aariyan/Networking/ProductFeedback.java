package com.reginald.briefcaseglobal.Aariyan.Networking;

import androidx.lifecycle.MutableLiveData;

import com.reginald.briefcaseglobal.Aariyan.Interface.ProductListInterface;
import com.reginald.briefcaseglobal.Aariyan.Interface.RestApis;
import com.reginald.briefcaseglobal.Aariyan.Model.ProductModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class ProductFeedback {

    private CompositeDisposable productDisposable = new CompositeDisposable();

    public void getProductFromServer(ProductListInterface productListInterface, RestApis apis, String customerCode) {
        List<ProductModel> listOfProduct = new ArrayList<>();
        productDisposable.add(apis.getProducts(customerCode).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Throwable {
                        JSONArray root = new JSONArray(responseBody.string());
                        if (root.length() > 0) {
                            for (int i = 0; i < root.length(); i++) {
                                JSONObject single = root.getJSONObject(i);
                                String partNumber = single.getString("strPartNumber");
                                String desc = single.getString("strDesc");
                                String category = single.getString("strCategory");
                                String vat = single.getString("Vat");
                                String productId = single.getString("ProductID");
                                String companyName = single.getString("strCompanyName");

                                String cost = ""+single.getInt("Cost");
                                ProductModel model = new ProductModel(partNumber, desc, category, vat, productId, companyName, cost);
                                listOfProduct.add(model);
                            }

                            productListInterface.listOfProduct(listOfProduct);
                        } else {
                            productListInterface.error("No Product Available");
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        productListInterface.error("" + throwable.getMessage());
                    }
                }));
    }
}
