package com.example.servingwebcontent.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Entity
@Table(name = "hang_hoa")
public class HangHoa {

    @NotBlank(message = "Mã hàng không được để trống")
    @Size(max = 10, message = "Mã hàng không được quá 10 ký tự")
    @Id
    @Column(name = "ma_hang", length = 10)
    private String maHang;

    @NotBlank(message = "Tên hàng không được để trống")
    @Size(max = 100, message = "Tên hàng không được quá 100 ký tự")
    @Column(name = "ten_hang", length = 100, nullable = false)
    private String tenHang;

    @NotNull(message = "Số lượng không được để trống")
    @Positive(message = "Số lượng phải lớn hơn 0")
    @Column(name = "so_luong", nullable = false)
    private Integer soLuong;

    @NotBlank(message = "Nhà sản xuất không được để trống")
    @Size(max = 100, message = "Nhà sản xuất không được quá 100 ký tự")
    @Column(name = "nha_san_xuat", length = 100, nullable = false)
    private String nhaSanXuat;

    @NotNull(message = "Đơn giá không được để trống")
    @Positive(message = "Đơn giá phải lớn hơn 0")
    @Column(name = "don_gia", nullable = false)
    private Double donGia;

    @Column(name = "nam_sx")
    private Integer namSanXuat;

    public HangHoa() {}

    public HangHoa(String maHang, String tenHang, Integer soLuong, String nhaSanXuat, Double donGia, Integer namSanXuat) {
        this.maHang = maHang;
        this.tenHang = tenHang;
        this.soLuong = soLuong;
        this.nhaSanXuat = nhaSanXuat;
        this.donGia = donGia;
        this.namSanXuat = namSanXuat;
    }

    public String getMaHang() {
        return maHang;
    }

    public void setMaHang(String maHang) {
        this.maHang = maHang;
    }

    public String getTenHang() {
        return tenHang;
    }

    public void setTenHang(String tenHang) {
        this.tenHang = tenHang;
    }

    public Integer getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(Integer soLuong) {
        this.soLuong = soLuong;
    }

    public String getNhaSanXuat() {
        return nhaSanXuat;
    }

    public void setNhaSanXuat(String nhaSanXuat) {
        this.nhaSanXuat = nhaSanXuat;
    }

    public Double getDonGia() {
        return donGia;
    }

    public void setDonGia(Double donGia) {
        this.donGia = donGia;
    }

    public Integer getNamSanXuat() {
        return namSanXuat;
    }

    public void setNamSanXuat(Integer namSanXuat) {
        this.namSanXuat = namSanXuat;
    }

    public double thanhTien() {
        return soLuong * donGia;
    }

    @Override
    public String toString() {
        return "HangHoa{" +
                "maHang='" + maHang + '\'' +
                ", tenHang='" + tenHang + '\'' +
                ", soLuong=" + soLuong +
                ", nhaSanXuat='" + nhaSanXuat + '\'' +
                ", donGia=" + donGia +
                ", namSanXuat=" + namSanXuat +
                '}';
    }
}
