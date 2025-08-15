package com.example.servingwebcontent.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "nhap")
public class NhapEntity {
    @Column(name = "nha_san_xuat", length = 100)
    private String nhaSanXuat;
    public String getNhaSanXuat() { return nhaSanXuat; }
    public void setNhaSanXuat(String n) { this.nhaSanXuat = n; }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "hanghoa_id", length = 10, nullable = false)
    private String hanghoaID;

    @Column(name = "ten_hang", length = 100, nullable = false)
    private String tenHang;

    @Column(name = "so_luong", nullable = false)
    private int soLuongNhap;

    @Column(name = "gia_nhap", nullable = false)
    private double giaNhap;

    @Column(name = "ngay_nhap", nullable = false)
    private LocalDate ngayNhap;

    @Column(name = "thoi_gian_nhap")
    private LocalDateTime thoiGianNhap;

    public NhapEntity() {}

    public NhapEntity(String hanghoaID, String tenHang, int soLuongNhap, double giaNhap, LocalDate ngayNhap, LocalDateTime thoiGianNhap) {
        this.hanghoaID = hanghoaID;
        this.tenHang = tenHang;
        this.soLuongNhap = soLuongNhap;
        this.giaNhap = giaNhap;
        this.ngayNhap = ngayNhap;
        this.thoiGianNhap = thoiGianNhap;
    }

    public Long getId() {return id;}
    public String getHanghoaID(){return hanghoaID;}
    public void setHanghoaID(String id){this.hanghoaID=id;}
    public String getTenHang(){return tenHang;}
    public void setTenHang(String t){this.tenHang=t;}
    public int getSoLuongNhap(){return soLuongNhap;}
    public void setSoLuongNhap(int v){this.soLuongNhap=v;}
    public double getGiaNhap(){return giaNhap;}
    public void setGiaNhap(double g){this.giaNhap=g;}
    public LocalDate getNgayNhap(){return ngayNhap;}
    public void setNgayNhap(LocalDate d){this.ngayNhap=d;}
    public LocalDateTime getThoiGianNhap(){return thoiGianNhap;}
    public void setThoiGianNhap(LocalDateTime t){this.thoiGianNhap=t;}
    public double getTongTien(){return soLuongNhap * giaNhap;}
}
