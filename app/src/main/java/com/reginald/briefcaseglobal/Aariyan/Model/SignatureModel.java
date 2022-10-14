package com.reginald.briefcaseglobal.Aariyan.Model;

public class SignatureModel {
    private String transactionId;
    private String signature;

    public SignatureModel(){}

    public SignatureModel(String transactionId, String signature) {
        this.transactionId = transactionId;
        this.signature = signature;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
