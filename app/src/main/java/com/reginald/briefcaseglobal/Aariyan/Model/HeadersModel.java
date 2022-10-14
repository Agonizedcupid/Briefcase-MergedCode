package com.reginald.briefcaseglobal.Aariyan.Model;

public class HeadersModel {
    private String transactionId, customerCode, dateFrom, dateTo, userId;
    private int isCompleted, isUploaded;
    private String productName;
    private String productPrice;
    private String productCode;

    public HeadersModel() {}

    public HeadersModel(String transactionId, String customerCode, String dateFrom, String dateTo, String userId, int isCompleted, int isUploaded,String productName,String productPrice, String productCode) {
        this.transactionId = transactionId;
        this.customerCode = customerCode;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.userId = userId;
        this.isCompleted = isCompleted;
        this.isUploaded = isUploaded;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productCode = productCode;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    public String getDateTo() {
        return dateTo;
    }

    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(int isCompleted) {
        this.isCompleted = isCompleted;
    }

    public int getIsUploaded() {
        return isUploaded;
    }

    public void setIsUploaded(int isUploaded) {
        this.isUploaded = isUploaded;
    }
}
