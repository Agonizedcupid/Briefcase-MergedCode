package com.reginald.briefcaseglobal.Model;

public class DailyScheduleModel {

    private String StoreName,code,date,timezone_type,timezone;
    public DailyScheduleModel(){}

    public DailyScheduleModel(String storeName, String code, String date, String timezone_type, String timezone) {
        StoreName = storeName;
        this.code = code;
        this.date = date;
        this.timezone_type = timezone_type;
        this.timezone = timezone;
    }

    public String getStoreName() {
        return StoreName;
    }

    public void setStoreName(String storeName) {
        StoreName = storeName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTimezone_type() {
        return timezone_type;
    }

    public void setTimezone_type(String timezone_type) {
        this.timezone_type = timezone_type;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }
}
