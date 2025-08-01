package com.example.servingwebcontent.model;

import java.time.LocalDate;

public class Nhap {
    private String hanghoaID;
    private int soLuongNhap;
    private double giaNhap;
    private LocalDate ngayNhap;
    
    // Constructor mặc định
    public Nhap() {
        this.ngayNhap = LocalDate.now();
    }
    
    // Constructor đầy đủ
    public Nhap(String hanghoaID, int soLuongNhap, double giaNhap, LocalDate ngayNhap) {
        this.hanghoaID = hanghoaID;
        this.soLuongNhap = soLuongNhap;
        this.giaNhap = giaNhap;
        this.ngayNhap = ngayNhap;
    }
    
    // Getters và Setters
    public String getHanghoaID() {
        return hanghoaID;
    }
    
    public void setHanghoaID(String hanghoaID) {
        this.hanghoaID = hanghoaID;
    }
    
    public int getSoLuongNhap() {
        return soLuongNhap;
    }
    
    public void setSoLuongNhap(int soLuongNhap) {
        this.soLuongNhap = soLuongNhap;
    }
    
    public double getGiaNhap() {
        return giaNhap;
    }
    
    public void setGiaNhap(double giaNhap) {
        this.giaNhap = giaNhap;
    }
    
    public LocalDate getNgayNhap() {
        return ngayNhap;
    }
    
    public void setNgayNhap(LocalDate ngayNhap) {
        this.ngayNhap = ngayNhap;
    }
    
    // Phương thức tính tổng tiền
    public double getTongTien() {
        return soLuongNhap * giaNhap;
    }
    
    @Override
    public String toString() {
        return "Nhap{" +
                "hanghoaID='" + hanghoaID + '\'' +
                ", soLuongNhap=" + soLuongNhap +
                ", giaNhap=" + giaNhap +
                ", ngayNhap=" + ngayNhap +
                '}';
    }
}
