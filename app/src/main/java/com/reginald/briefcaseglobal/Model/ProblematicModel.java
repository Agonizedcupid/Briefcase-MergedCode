package com.reginald.briefcaseglobal.Model;

public class ProblematicModel {
    private String CustomerPastelCode,StoreName,PastelDescription,PastelCode;
    private String strSalesName,decLastYear,decLastMonth,decPriorMonth,decDifference,wasNotActive;

    public ProblematicModel(){}

    public ProblematicModel(String customerPastelCode, String storeName, String pastelDescription, String pastelCode, String strSalesName, String decLastYear, String decLastMonth, String decPriorMonth, String decDifference, String wasNotActive) {
        CustomerPastelCode = customerPastelCode;
        StoreName = storeName;
        PastelDescription = pastelDescription;
        PastelCode = pastelCode;
        this.strSalesName = strSalesName;
        this.decLastYear = decLastYear;
        this.decLastMonth = decLastMonth;
        this.decPriorMonth = decPriorMonth;
        this.decDifference = decDifference;
        this.wasNotActive = wasNotActive;
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

    public String getPastelDescription() {
        return PastelDescription;
    }

    public void setPastelDescription(String pastelDescription) {
        PastelDescription = pastelDescription;
    }

    public String getPastelCode() {
        return PastelCode;
    }

    public void setPastelCode(String pastelCode) {
        PastelCode = pastelCode;
    }

    public String getStrSalesName() {
        return strSalesName;
    }

    public void setStrSalesName(String strSalesName) {
        this.strSalesName = strSalesName;
    }

    public String getDecLastYear() {
        return decLastYear;
    }

    public void setDecLastYear(String decLastYear) {
        this.decLastYear = decLastYear;
    }

    public String getDecLastMonth() {
        return decLastMonth;
    }

    public void setDecLastMonth(String decLastMonth) {
        this.decLastMonth = decLastMonth;
    }

    public String getDecPriorMonth() {
        return decPriorMonth;
    }

    public void setDecPriorMonth(String decPriorMonth) {
        this.decPriorMonth = decPriorMonth;
    }

    public String getDecDifference() {
        return decDifference;
    }

    public void setDecDifference(String decDifference) {
        this.decDifference = decDifference;
    }

    public String getWasNotActive() {
        return wasNotActive;
    }

    public void setWasNotActive(String wasNotActive) {
        this.wasNotActive = wasNotActive;
    }
}
