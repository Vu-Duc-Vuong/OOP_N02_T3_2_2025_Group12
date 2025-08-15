package com.example.servingwebcontent.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "Ban")
public class BanEntity {
    @Id
    private String maPhieu;
    private String maHang;
    private String tenHang;
    @Column(name = "soLuong") // Đảm bảo đúng tên cột trong SQL là soLuong
    private int soLuongBan;
    private String tenKhach;
    private double donGia;
    private LocalDate ngayBan;
    private LocalDateTime thoiGianBan;

    public BanEntity() {}

    public BanEntity(String maPhieu, String maHang, String tenHang, int soLuongBan, String tenKhach, double donGia, LocalDate ngayBan, LocalDateTime thoiGianBan) {
        this.maPhieu = maPhieu;
        this.maHang = maHang;
        this.tenHang = tenHang;
        this.soLuongBan = soLuongBan;
        this.tenKhach = tenKhach;
        this.donGia = donGia;
        this.ngayBan = ngayBan;
        this.thoiGianBan = thoiGianBan;
    }

    // Getters and setters
    public String getMaPhieu() { return maPhieu; }
    public void setMaPhieu(String maPhieu) { this.maPhieu = maPhieu; }
    public String getMaHang() { return maHang; }
    public void setMaHang(String maHang) { this.maHang = maHang; }
    public String getTenHang() { return tenHang; }
    public void setTenHang(String tenHang) { this.tenHang = tenHang; }
    public int getSoLuongBan() { return soLuongBan; }
    public void setSoLuongBan(int soLuongBan) { this.soLuongBan = soLuongBan; }
    public String getTenKhach() { return tenKhach; }
    public void setTenKhach(String tenKhach) { this.tenKhach = tenKhach; }
    public double getDonGia() { return donGia; }
    public void setDonGia(double donGia) { this.donGia = donGia; }
    public LocalDate getNgayBan() { return ngayBan; }
    public void setNgayBan(LocalDate ngayBan) { this.ngayBan = ngayBan; }
    public LocalDateTime getThoiGianBan() { return thoiGianBan; }
    public void setThoiGianBan(LocalDateTime thoiGianBan) { this.thoiGianBan = thoiGianBan; }
}
