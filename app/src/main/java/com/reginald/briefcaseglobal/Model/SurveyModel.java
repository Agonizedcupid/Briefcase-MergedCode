package com.reginald.briefcaseglobal.Model;

public class SurveyModel {
    private String intAuto,strMessage,dteActiveFrom,dteActiveTo,date;
    public SurveyModel(){}

    public SurveyModel(String intAuto, String strMessage, String dteActiveFrom, String dteActiveTo, String date) {
        this.intAuto = intAuto;
        this.strMessage = strMessage;
        this.dteActiveFrom = dteActiveFrom;
        this.dteActiveTo = dteActiveTo;
        this.date = date;
    }

    public String getIntAuto() {
        return intAuto;
    }

    public void setIntAuto(String intAuto) {
        this.intAuto = intAuto;
    }

    public String getStrMessage() {
        return strMessage;
    }

    public void setStrMessage(String strMessage) {
        this.strMessage = strMessage;
    }

    public String getDteActiveFrom() {
        return dteActiveFrom;
    }

    public void setDteActiveFrom(String dteActiveFrom) {
        this.dteActiveFrom = dteActiveFrom;
    }

    public String getDteActiveTo() {
        return dteActiveTo;
    }

    public void setDteActiveTo(String dteActiveTo) {
        this.dteActiveTo = dteActiveTo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
