package com.reginald.briefcaseglobal.Model;

public class ItemModel {
    private String ItemName,ItemCode;
    private int Qty;
    private double Price,Total,Tax;
    private String MESSAGESINV;

    public ItemModel(){}

    public ItemModel(String itemName, String itemCode, int qty, double price, double total, double tax, String MESSAGESINV) {
        ItemName = itemName;
        ItemCode = itemCode;
        Qty = qty;
        Price = price;
        Total = total;
        Tax = tax;
        this.MESSAGESINV = MESSAGESINV;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public String getItemCode() {
        return ItemCode;
    }

    public void setItemCode(String itemCode) {
        ItemCode = itemCode;
    }

    public int getQty() {
        return Qty;
    }

    public void setQty(int qty) {
        Qty = qty;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }

    public double getTotal() {
        return Total;
    }

    public void setTotal(double total) {
        Total = total;
    }

    public double getTax() {
        return Tax;
    }

    public void setTax(double tax) {
        Tax = tax;
    }

    public String getMESSAGESINV() {
        return MESSAGESINV;
    }

    public void setMESSAGESINV(String MESSAGESINV) {
        this.MESSAGESINV = MESSAGESINV;
    }
}
