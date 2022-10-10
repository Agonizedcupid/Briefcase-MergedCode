package com.reginald.briefcaseglobal.Model;

public class DecreasingSalesModel {
    private String Code,StoreName,LastYear,lym,cym,diff;
    public DecreasingSalesModel(){}

    public DecreasingSalesModel(String code, String storeName, String lastYear, String lym, String cym, String diff) {
        Code = code;
        StoreName = storeName;
        LastYear = lastYear;
        this.lym = lym;
        this.cym = cym;
        this.diff = diff;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getStoreName() {
        return StoreName;
    }

    public void setStoreName(String storeName) {
        StoreName = storeName;
    }

    public String getLastYear() {
        return LastYear;
    }

    public void setLastYear(String lastYear) {
        LastYear = lastYear;
    }

    public String getLym() {
        return lym;
    }

    public void setLym(String lym) {
        this.lym = lym;
    }

    public String getCym() {
        return cym;
    }

    public void setCym(String cym) {
        this.cym = cym;
    }

    public String getDiff() {
        return diff;
    }

    public void setDiff(String diff) {
        this.diff = diff;
    }
}
