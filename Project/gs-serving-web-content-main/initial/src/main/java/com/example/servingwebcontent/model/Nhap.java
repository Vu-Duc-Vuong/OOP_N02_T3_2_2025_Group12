package com.example.servingwebcontent.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.springframework.format.annotation.DateTimeFormat;

public class Nhap {
    private String hanghoaID;
    private String tenHang;
    private String nhaSanXuat;
    private int soLuongNhap;
    private double giaNhap;
    public String getNhaSanXuat() {
        return nhaSanXuat;
    }
    public void setNhaSanXuat(String nhaSanXuat) {
        this.nhaSanXuat = nhaSanXuat;
    }
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate ngayNhap;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime thoiGianNhap;

    public Nhap() {
        this.ngayNhap = LocalDate.now();
        this.thoiGianNhap = LocalDateTime.of(this.ngayNhap, LocalTime.now());
    }

    public Nhap(String hanghoaID, int soLuongNhap, double giaNhap, LocalDate ngayNhap) {
        this.hanghoaID = hanghoaID;
        this.soLuongNhap = soLuongNhap;
        this.giaNhap = giaNhap;
        this.ngayNhap = ngayNhap;
        this.thoiGianNhap = LocalDateTime.of(this.ngayNhap, LocalTime.now());
    }

    public Nhap(String hanghoaID, String tenHang, int soLuongNhap, double giaNhap, LocalDate ngayNhap) {
        this.hanghoaID = hanghoaID;
        this.tenHang = tenHang;
        this.soLuongNhap = soLuongNhap;
        this.giaNhap = giaNhap;
        this.ngayNhap = ngayNhap;
        this.thoiGianNhap = LocalDateTime.of(this.ngayNhap, LocalTime.now());
    }

    public String getHanghoaID() {
        return hanghoaID;
    }

    public void setHanghoaID(String hanghoaID) {
        this.hanghoaID = hanghoaID;
    }

    public String getTenHang() {
        return tenHang;
    }

    public void setTenHang(String tenHang) {
        this.tenHang = tenHang;
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
    public LocalDateTime getThoiGianNhap(){ return thoiGianNhap; }
    public void setThoiGianNhap(LocalDateTime t){ this.thoiGianNhap = t; }

    public double getTongTien() {
        return soLuongNhap * giaNhap;
    }

    @Override
    public String toString() {
        return "Nhap{" +
                "hanghoaID='" + hanghoaID + '\'' +
                ", tenHang='" + tenHang + '\'' +
                ", soLuongNhap=" + soLuongNhap +
                ", giaNhap=" + giaNhap +
                ", ngayNhap=" + ngayNhap +
                ", thoiGianNhap=" + thoiGianNhap +
                '}';
    }
}
