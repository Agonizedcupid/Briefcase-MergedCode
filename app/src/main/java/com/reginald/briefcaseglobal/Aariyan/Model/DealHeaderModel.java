package com.reginald.briefcaseglobal.Aariyan.Model;

public class DealHeaderModel {
    private String transactionID,customerCode,dateFrom,dateTo,
            userId;

    private int isCompleted,isUploaded;

    public DealHeaderModel(){}

    public DealHeaderModel(String transactionID, String customerCode, String dateFrom, String dateTo, String userId, int isCompleted, int isUploaded) {
        this.transactionID = transactionID;
        this.customerCode = customerCode;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.userId = userId;
        this.isCompleted = isCompleted;
        this.isUploaded = isUploaded;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
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
