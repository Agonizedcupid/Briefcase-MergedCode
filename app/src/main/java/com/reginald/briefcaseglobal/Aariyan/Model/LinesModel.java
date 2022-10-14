package com.reginald.briefcaseglobal.Aariyan.Model;

public class LinesModel {

    private String productCode,price;

    public LinesModel() {}

    public LinesModel(String productCode, String price) {
        this.productCode = productCode;
        this.price = price;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
