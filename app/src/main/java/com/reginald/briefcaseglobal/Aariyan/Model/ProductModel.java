package com.reginald.briefcaseglobal.Aariyan.Model;

public class ProductModel {
    private String strPartNumber,strDesc,strCategory,Vat,ProductID,strCompanyName;
    private String Cost;

    public ProductModel() {}

    public ProductModel(String strPartNumber, String strDesc, String strCategory, String vat, String productID, String strCompanyName, String cost) {
        this.strPartNumber = strPartNumber;
        this.strDesc = strDesc;
        this.strCategory = strCategory;
        Vat = vat;
        ProductID = productID;
        this.strCompanyName = strCompanyName;
        Cost = cost;
    }

    public String getStrPartNumber() {
        return strPartNumber;
    }

    public void setStrPartNumber(String strPartNumber) {
        this.strPartNumber = strPartNumber;
    }

    public String getStrDesc() {
        return strDesc;
    }

    public void setStrDesc(String strDesc) {
        this.strDesc = strDesc;
    }

    public String getStrCategory() {
        return strCategory;
    }

    public void setStrCategory(String strCategory) {
        this.strCategory = strCategory;
    }

    public String getVat() {
        return Vat;
    }

    public void setVat(String vat) {
        Vat = vat;
    }

    public String getProductID() {
        return ProductID;
    }

    public void setProductID(String productID) {
        ProductID = productID;
    }

    public String getStrCompanyName() {
        return strCompanyName;
    }

    public void setStrCompanyName(String strCompanyName) {
        this.strCompanyName = strCompanyName;
    }

    public String getCost() {
        return Cost;
    }

    public void setCost(String cost) {
        Cost = cost;
    }
}
