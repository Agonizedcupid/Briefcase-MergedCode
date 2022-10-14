package com.reginald.briefcaseglobal.Aariyan.Model;

public class LinesModel {
    private String productCode,price, transactionId;

    public LinesModel() {}

    public LinesModel(String productCode, String price, String transactionId) {
        this.productCode = productCode;
        this.price = price;
        this.transactionId = transactionId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
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
