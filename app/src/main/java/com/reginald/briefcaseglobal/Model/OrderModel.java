package com.reginald.briefcaseglobal.Model;

public class OrderModel {
    private String StoreName,CustomerPastelCode,orderDate;
    private int orderTimeZoneType;
    private String orderTimeZone;

    private String deliveryDate;
    private int deliveryTimeZoneType;
    private String deliveryTimeZone;

    private String invoiceNo,OrderNo,OrderId;

    public OrderModel(){}

    public OrderModel(String storeName, String customerPastelCode, String orderDate, int orderTimeZoneType, String orderTimeZone, String deliveryDate, int deliveryTimeZoneType, String deliveryTimeZone, String invoiceNo, String orderNo, String orderId) {
        StoreName = storeName;
        CustomerPastelCode = customerPastelCode;
        this.orderDate = orderDate;
        this.orderTimeZoneType = orderTimeZoneType;
        this.orderTimeZone = orderTimeZone;
        this.deliveryDate = deliveryDate;
        this.deliveryTimeZoneType = deliveryTimeZoneType;
        this.deliveryTimeZone = deliveryTimeZone;
        this.invoiceNo = invoiceNo;
        OrderNo = orderNo;
        OrderId = orderId;
    }

    public String getStoreName() {
        return StoreName;
    }

    public void setStoreName(String storeName) {
        StoreName = storeName;
    }

    public String getCustomerPastelCode() {
        return CustomerPastelCode;
    }

    public void setCustomerPastelCode(String customerPastelCode) {
        CustomerPastelCode = customerPastelCode;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public int getOrderTimeZoneType() {
        return orderTimeZoneType;
    }

    public void setOrderTimeZoneType(int orderTimeZoneType) {
        this.orderTimeZoneType = orderTimeZoneType;
    }

    public String getOrderTimeZone() {
        return orderTimeZone;
    }

    public void setOrderTimeZone(String orderTimeZone) {
        this.orderTimeZone = orderTimeZone;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public int getDeliveryTimeZoneType() {
        return deliveryTimeZoneType;
    }

    public void setDeliveryTimeZoneType(int deliveryTimeZoneType) {
        this.deliveryTimeZoneType = deliveryTimeZoneType;
    }

    public String getDeliveryTimeZone() {
        return deliveryTimeZone;
    }

    public void setDeliveryTimeZone(String deliveryTimeZone) {
        this.deliveryTimeZone = deliveryTimeZone;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getOrderNo() {
        return OrderNo;
    }

    public void setOrderNo(String orderNo) {
        OrderNo = orderNo;
    }

    public String getOrderId() {
        return OrderId;
    }

    public void setOrderId(String orderId) {
        OrderId = orderId;
    }
}
