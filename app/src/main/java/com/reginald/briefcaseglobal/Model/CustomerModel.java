package com.reginald.briefcaseglobal.Model;

public class CustomerModel {
    private String CustomerPastelCode,StoreName,lastNotes,lastDate;
    private CustomerModel(){}

    public CustomerModel(String customerPastelCode, String storeName, String lastNotes, String lastDate) {
        CustomerPastelCode = customerPastelCode;
        StoreName = storeName;
        this.lastNotes = lastNotes;
        this.lastDate = lastDate;
    }

    public String getCustomerPastelCode() {
        return CustomerPastelCode;
    }

    public void setCustomerPastelCode(String customerPastelCode) {
        CustomerPastelCode = customerPastelCode;
    }

    public String getStoreName() {
        return StoreName;
    }

    public void setStoreName(String storeName) {
        StoreName = storeName;
    }

    public String getLastNotes() {
        return lastNotes;
    }

    public void setLastNotes(String lastNotes) {
        this.lastNotes = lastNotes;
    }

    public String getLastDate() {
        return lastDate;
    }

    public void setLastDate(String lastDate) {
        this.lastDate = lastDate;
    }
}
