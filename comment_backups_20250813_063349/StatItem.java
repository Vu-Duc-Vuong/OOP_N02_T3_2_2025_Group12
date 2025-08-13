package com.example.servingwebcontent.model;

public class StatItem {
    private String code; // Mã hàng hoặc ký hiệu
    private String name; // Tên hàng
    private int quantity;
    private double totalValue;

    public StatItem(String code, String name){
        this.code = code;
        this.name = name;
    }

    public void add(int qty, double value){
        this.quantity += qty;
        this.totalValue += value;
    }

    public String getCode() {return code;}
    public String getName() {return name;}
    public int getQuantity() {return quantity;}
    public double getTotalValue() {return totalValue;}
}
