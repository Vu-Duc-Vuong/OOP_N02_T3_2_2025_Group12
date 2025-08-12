package com.example.servingwebcontent.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

public class Ban {
    private String maPhieu;
    private String tenHang; // tên hàng (mới)
    private String tenKhach;
    private int soLuong;
    private double donGia;
    private LocalDate ngayBan; // thêm ngày bán
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime thoiGianBan; // thời gian bán chi tiết
    
    // Constructor
    public Ban() { this.ngayBan = LocalDate.now(); this.thoiGianBan = LocalDateTime.now(); }
    
    public Ban(String maPhieu, String tenKhach, int soLuong, double donGia) {
        this.maPhieu = maPhieu;
        this.tenKhach = tenKhach;
        this.soLuong = soLuong;
        this.donGia = donGia;
    this.ngayBan = LocalDate.now();
    this.thoiGianBan = LocalDateTime.now();
    }

    // Constructor đầy đủ bao gồm tên hàng
    public Ban(String maPhieu, String tenHang, String tenKhach, int soLuong, double donGia) {
        this.maPhieu = maPhieu;
        this.tenHang = tenHang;
        this.tenKhach = tenKhach;
        this.soLuong = soLuong;
        this.donGia = donGia;
    this.ngayBan = LocalDate.now();
    this.thoiGianBan = LocalDateTime.now();
    }
    
    // Getter và Setter
    public String getMaPhieu() {
        return maPhieu;
    }
    
    public void setMaPhieu(String maPhieu) {
        this.maPhieu = maPhieu;
    }

    public String getTenHang() {
        return tenHang;
    }

    public void setTenHang(String tenHang) {
        this.tenHang = tenHang;
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

    public LocalDate getNgayBan(){ return ngayBan; }
    public void setNgayBan(LocalDate d){ this.ngayBan = d; }
    public LocalDateTime getThoiGianBan(){ return thoiGianBan; }
    public void setThoiGianBan(LocalDateTime t){ this.thoiGianBan = t; }
    
    @Override
    public String toString() {
        return "Ban{" +
                "maPhieu='" + maPhieu + '\'' +
                ", tenHang='" + tenHang + '\'' +
                ", tenKhach='" + tenKhach + '\'' +
                ", soLuong=" + soLuong +
                ", donGia=" + donGia +
                ", ngayBan=" + ngayBan +
                ", thoiGianBan=" + thoiGianBan +
                ", tongTien=" + tongTien() +
                '}';
    }
}
