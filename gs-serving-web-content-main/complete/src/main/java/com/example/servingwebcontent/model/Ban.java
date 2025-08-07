package com.example.servingwebcontent.model;

public class Ban {
    private String maPhieu;
    private String tenKhach;
    private int soLuong;
    private double donGia;
    
    // Constructor
    public Ban() {}
    
    public Ban(String maPhieu, String tenKhach, int soLuong, double donGia) {
        this.maPhieu = maPhieu;
        this.tenKhach = tenKhach;
        this.soLuong = soLuong;
        this.donGia = donGia;
    }
    
    // Getter và Setter
    public String getMaPhieu() {
        return maPhieu;
    }
    
    public void setMaPhieu(String maPhieu) {
        this.maPhieu = maPhieu;
    }
    
    public String getTenKhach() {
        return tenKhach;
    }
    
    public void setTenKhach(String tenKhach) {
        this.tenKhach = tenKhach;
    }
    
    public int getSoLuong() {
        return soLuong;
    }
    
    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }
    
    public double getDonGia() {
        return donGia;
    }
    
    public void setDonGia(double donGia) {
        this.donGia = donGia;
    }
    
    // Method tính tổng tiền
    public double tongTien() {
        return soLuong * donGia;
    }
    
    @Override
    public String toString() {
        return "Ban{" +
                "maPhieu='" + maPhieu + '\'' +
                ", tenKhach='" + tenKhach + '\'' +
                ", soLuong=" + soLuong +
                ", donGia=" + donGia +
                ", tongTien=" + tongTien() +
                '}';
    }
}
