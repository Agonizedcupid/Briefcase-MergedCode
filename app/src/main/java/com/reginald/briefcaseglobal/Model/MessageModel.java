package com.reginald.briefcaseglobal.Model;

public class MessageModel {
    private String intAutoId;
    private int intDepId;
    private String Department,messagestofollow;
    private int intCreatedBy,intAssignedTo;
    private String date;
    private int intStatusId;
    private String strSubject,FromUser;

    public MessageModel(){}

    public MessageModel(String intAutoId, int intDepId, String department, String messagestofollow, int intCreatedBy, int intAssignedTo, String date, int intStatusId, String strSubject, String fromUser) {
        this.intAutoId = intAutoId;
        this.intDepId = intDepId;
        Department = department;
        this.messagestofollow = messagestofollow;
        this.intCreatedBy = intCreatedBy;
        this.intAssignedTo = intAssignedTo;
        this.date = date;
        this.intStatusId = intStatusId;
        this.strSubject = strSubject;
        FromUser = fromUser;
    }

    public String getIntAutoId() {
        return intAutoId;
    }

    public void setIntAutoId(String intAutoId) {
        this.intAutoId = intAutoId;
    }

    public int getIntDepId() {
        return intDepId;
    }

    public void setIntDepId(int intDepId) {
        this.intDepId = intDepId;
    }

    public String getDepartment() {
        return Department;
    }

    public void setDepartment(String department) {
        Department = department;
    }

    public String getMessagestofollow() {
        return messagestofollow;
    }

    public void setMessagestofollow(String messagestofollow) {
        this.messagestofollow = messagestofollow;
    }

    public int getIntCreatedBy() {
        return intCreatedBy;
    }

    public void setIntCreatedBy(int intCreatedBy) {
        this.intCreatedBy = intCreatedBy;
    }

    public int getIntAssignedTo() {
        return intAssignedTo;
    }

    public void setIntAssignedTo(int intAssignedTo) {
        this.intAssignedTo = intAssignedTo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getIntStatusId() {
        return intStatusId;
    }

    public void setIntStatusId(int intStatusId) {
        this.intStatusId = intStatusId;
    }

    public String getStrSubject() {
        return strSubject;
    }

    public void setStrSubject(String strSubject) {
        this.strSubject = strSubject;
    }

    public String getFromUser() {
        return FromUser;
    }

    public void setFromUser(String fromUser) {
        FromUser = fromUser;
    }
}
