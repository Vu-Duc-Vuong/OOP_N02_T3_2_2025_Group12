package com.example.servingwebcontent.model;

import com.example.servingwebcontent.database.aivenConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChiTietDichVu {
    // ===== Fields =====
    private long id;                // PK AUTO_INCREMENT
    private String maDatPhong;      // FK -> dat_phong.ma_dat_phong
    private String maDichVu;        // FK -> dich_vu.ma_dich_vu
    private int soLuong;            // THEO_LAN: số lần | THEO_GIO: số giờ
    private double thanhTien;       // donGia * soLuong (lưu để audit)

    // ===== Constructors =====
    public ChiTietDichVu() {}

    public ChiTietDichVu(long id, String maDatPhong, String maDichVu, int soLuong, double thanhTien) {
        this.id = id;
        this.maDatPhong = maDatPhong;
        this.maDichVu = maDichVu;
        this.soLuong = soLuong;
        this.thanhTien = thanhTien;
    }

    public ChiTietDichVu(String maDatPhong, String maDichVu, int soLuong, double thanhTien) {
        this(0L, maDatPhong, maDichVu, soLuong, thanhTien);
    }

    // ===== Getters/Setters =====
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getMaDatPhong() { return maDatPhong; }
    public void setMaDatPhong(String maDatPhong) { this.maDatPhong = maDatPhong; }

    public String getMaDichVu() { return maDichVu; }
    public void setMaDichVu(String maDichVu) { this.maDichVu = maDichVu; }

    public int getSoLuong() { return soLuong; }
    public void setSoLuong(int soLuong) { this.soLuong = soLuong; }

    public double getThanhTien() { return thanhTien; }
    public void setThanhTien(double thanhTien) { this.thanhTien = thanhTien; }

    // ===== toString =====
    @Override
    public String toString() {
        return "ChiTietDichVu{" +
                "id=" + id +
                ", maDatPhong='" + maDatPhong + '\'' +
                ", maDichVu='" + maDichVu + '\'' +
                ", soLuong=" + soLuong +
                ", thanhTien=" + thanhTien +
                '}';
    }

    // ================= CRUD (JDBC) =================

    // CREATE (trả về id được sinh)
    public static Long insert(ChiTietDichVu ctdv) {
        String sql = "INSERT INTO chi_tiet_dich_vu (ma_dat_phong, ma_dich_vu, so_luong, thanh_tien) " +
                     "VALUES (?, ?, ?, ?)";
        try (Connection cn = aivenConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, ctdv.getMaDatPhong());
            ps.setString(2, ctdv.getMaDichVu());
            ps.setInt(3, ctdv.getSoLuong());
            ps.setDouble(4, ctdv.getThanhTien());

            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        long id = rs.getLong(1);
                        ctdv.setId(id);
                        return id;
                    }
                }
            }
            return null;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // READ - by id
    public static ChiTietDichVu findById(long id) {
        String sql = "SELECT * FROM chi_tiet_dich_vu WHERE id = ?";
        try (Connection cn = aivenConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // READ - tất cả DV theo booking
    public static List<ChiTietDichVu> findByBooking(String maDatPhong) {
        List<ChiTietDichVu> list = new ArrayList<>();
        String sql = "SELECT * FROM chi_tiet_dich_vu WHERE ma_dat_phong = ? ORDER BY id DESC";
        try (Connection cn = aivenConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, maDatPhong);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // UPDATE
    public static boolean update(ChiTietDichVu ctdv) {
        String sql = "UPDATE chi_tiet_dich_vu SET ma_dat_phong=?, ma_dich_vu=?, so_luong=?, thanh_tien=? WHERE id=?";
        try (Connection cn = aivenConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, ctdv.getMaDatPhong());
            ps.setString(2, ctdv.getMaDichVu());
            ps.setInt(3, ctdv.getSoLuong());
            ps.setDouble(4, ctdv.getThanhTien());
            ps.setLong(5, ctdv.getId());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // DELETE
    public static boolean deleteById(long id) {
        String sql = "DELETE FROM chi_tiet_dich_vu WHERE id = ?";
        try (Connection cn = aivenConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setLong(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // DELETE - tất cả DV của một booking (tiện khi huỷ booking)
    public static int deleteByBooking(String maDatPhong) {
        String sql = "DELETE FROM chi_tiet_dich_vu WHERE ma_dat_phong = ?";
        try (Connection cn = aivenConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, maDatPhong);
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    // ================= Tính tổng tiền dịch vụ theo booking =================
    public static double sumThanhTienByBooking(String maDatPhong) {
        String sql = "SELECT COALESCE(SUM(thanh_tien), 0) FROM chi_tiet_dich_vu WHERE ma_dat_phong = ?";
        try (Connection cn = aivenConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, maDatPhong);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getDouble(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    // ===== Helper =====
    private static ChiTietDichVu mapRow(ResultSet rs) throws SQLException {
        ChiTietDichVu c = new ChiTietDichVu();
        c.setId(rs.getLong("id"));
        c.setMaDatPhong(rs.getString("ma_dat_phong"));
        c.setMaDichVu(rs.getString("ma_dich_vu"));
        c.setSoLuong(rs.getInt("so_luong"));
        c.setThanhTien(rs.getDouble("thanh_tien"));
        return c;
    }

    // equals/hashCode theo PK
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChiTietDichVu)) return false;
        ChiTietDichVu that = (ChiTietDichVu) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
