package com.example.servingwebcontent.quanly.model;

public class Ban {
    public String maPhieu;
    public HangHoa hang;
    public int soLuongBan;

    public Ban(String maPhieu, HangHoa hang, int soLuongBan) {
        this.maPhieu = maPhieu;
        this.hang = hang;
        this.soLuongBan = soLuongBan;
    }

    public double tongTien() {
        return soLuongBan * hang.donGia;
    }
}
