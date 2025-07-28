package com.example.servingwebcontent.quanly.model;
import java.time.LocalDate;

public class Nhap {
    public String maPhieu;
    public HangHoa hang;
    public int soLuongNhap;
    public LocalDate ngayNhap; // ⬅ Thêm dòng này

    public Nhap(String maPhieu, HangHoa hang, int soLuongNhap, LocalDate ngayNhap) {
        this.maPhieu = maPhieu;
        this.hang = hang;
        this.soLuongNhap = soLuongNhap;
        this.ngayNhap = ngayNhap;
    }

    // Constructor cũ cho tương thích
    public Nhap(String maPhieu, HangHoa hang, int soLuongNhap) {
        this.maPhieu = maPhieu;
        this.hang = hang;
        this.soLuongNhap = soLuongNhap;
        this.ngayNhap = LocalDate.now(); // Mặc định là ngày hiện tại
    }

    public double tongTien() {
        return soLuongNhap * hang.donGia;
    }

    public LocalDate getNgayNhap() {
        return ngayNhap;
    }
}
