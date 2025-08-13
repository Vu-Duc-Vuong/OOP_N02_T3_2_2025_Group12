package com.example.servingwebcontent.model;

public class StatItem {
    private String code;
    private String name;
    private int quantity;
    private double totalValue;
    private java.time.LocalDateTime thoiGian; // thời gian đại diện (lần đầu thêm)

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
    public java.time.LocalDateTime getThoiGian(){ return thoiGian; }
    public void setThoiGian(java.time.LocalDateTime t){ if(this.thoiGian==null) this.thoiGian = t; }
}
