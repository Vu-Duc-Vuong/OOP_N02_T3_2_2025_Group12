package com.example.servingwebcontent.model;

import com.example.servingwebcontent.database.aivenConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DichVu {
    // ===== Fields =====
    private String maDichVu;       // PK
    private String tenDichVu;
    private double gia;
    private ServiceType loai;      // THEO_LAN, THEO_GIO

    // ===== Constructors =====
    public DichVu() {}

    public DichVu(String maDichVu, String tenDichVu, double gia, ServiceType loai) {
        this.maDichVu = maDichVu;
        this.tenDichVu = tenDichVu;
        this.gia = gia;
        this.loai = loai;
    }

    // ===== Getters/Setters =====
    public String getMaDichVu() { return maDichVu; }
    public void setMaDichVu(String maDichVu) { this.maDichVu = maDichVu; }

    public String getTenDichVu() { return tenDichVu; }
    public void setTenDichVu(String tenDichVu) { this.tenDichVu = tenDichVu; }

    public double getGia() { return gia; }
    public void setGia(double gia) { this.gia = gia; }

    public ServiceType getLoai() { return loai; }
    public void setLoai(ServiceType loai) { this.loai = loai; }

    // ===== toString =====
    @Override
    public String toString() {
        return "DichVu{" +
                "maDichVu='" + maDichVu + '\'' +
                ", tenDichVu='" + tenDichVu + '\'' +
                ", gia=" + gia +
                ", loai=" + loai +
                '}';
    }

    // ================= CRUD (JDBC) =================

    // CREATE
    public static boolean insert(DichVu dv) {
        String sql = "INSERT INTO dich_vu (ma_dich_vu, ten_dich_vu, gia, loai) VALUES (?, ?, ?, ?)";
        try (Connection cn = aivenConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, dv.getMaDichVu());
            ps.setString(2, dv.getTenDichVu());
            ps.setDouble(3, dv.getGia());
            ps.setString(4, dv.getLoai().name());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // READ - by id
    public static DichVu findById(String ma) {
        String sql = "SELECT * FROM dich_vu WHERE ma_dich_vu = ?";
        try (Connection cn = aivenConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, ma);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // READ - all
    public static List<DichVu> findAll() {
        List<DichVu> list = new ArrayList<>();
        String sql = "SELECT * FROM dich_vu ORDER BY ten_dich_vu";
        try (Connection cn = aivenConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // SEARCH
    public static List<DichVu> search(String keyword) {
        List<DichVu> list = new ArrayList<>();
        String k = "%" + (keyword == null ? "" : keyword.trim()) + "%";
        String sql = "SELECT * FROM dich_vu WHERE ten_dich_vu LIKE ? ORDER BY ten_dich_vu";
        try (Connection cn = aivenConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, k);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // UPDATE
    public static boolean update(DichVu dv) {
        String sql = "UPDATE dich_vu SET ten_dich_vu=?, gia=?, loai=? WHERE ma_dich_vu=?";
        try (Connection cn = aivenConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, dv.getTenDichVu());
            ps.setDouble(2, dv.getGia());
            ps.setString(3, dv.getLoai().name());
            ps.setString(4, dv.getMaDichVu());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // DELETE
    public static boolean deleteById(String ma) {
        String sql = "DELETE FROM dich_vu WHERE ma_dich_vu = ?";
        try (Connection cn = aivenConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, ma);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            // Có thể vướng FK nếu dịch vụ đã được dùng trong chi_tiet_dich_vu
            e.printStackTrace();
            return false;
        }
    }

    // ===== Helper =====
    private static DichVu mapRow(ResultSet rs) throws SQLException {
        DichVu dv = new DichVu();
        dv.setMaDichVu(rs.getString("ma_dich_vu"));
        dv.setTenDichVu(rs.getString("ten_dich_vu"));
        dv.setGia(rs.getDouble("gia"));
        String loaiStr = rs.getString("loai");
        dv.setLoai(loaiStr != null ? ServiceType.valueOf(loaiStr) : null);
        return dv;
    }

    // equals/hashCode theo PK
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DichVu)) return false;
        DichVu dichVu = (DichVu) o;
        return Objects.equals(maDichVu, dichVu.maDichVu);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maDichVu);
    }
}
