package com.example.servingwebcontent.model;

import com.example.servingwebcontent.database.aivenConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class KhachHang {
    // ===== Fields =====
    private String dinhDanh;      // CCCD/Passport (PK)
    private String hoTen;
    private String gioiTinh;      // "Nam", "Nu", "Khac" ...
    private LocalDate ngaySinh;
    private String soDienThoai;
    private String email;
    private String diaChi;
    private String quocTich;

    // ===== Constructors =====
    public KhachHang() {}

    public KhachHang(String dinhDanh, String hoTen, String gioiTinh, LocalDate ngaySinh,
                     String soDienThoai, String email, String diaChi, String quocTich) {
        this.dinhDanh = dinhDanh;
        this.hoTen = hoTen;
        this.gioiTinh = gioiTinh;
        this.ngaySinh = ngaySinh;
        this.soDienThoai = soDienThoai;
        this.email = email;
        this.diaChi = diaChi;
        this.quocTich = quocTich;
    }

    // ===== Getters & Setters =====
    public String getDinhDanh() { return dinhDanh; }
    public void setDinhDanh(String dinhDanh) { this.dinhDanh = dinhDanh; }

    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }

    public String getGioiTinh() { return gioiTinh; }
    public void setGioiTinh(String gioiTinh) { this.gioiTinh = gioiTinh; }

    public LocalDate getNgaySinh() { return ngaySinh; }
    public void setNgaySinh(LocalDate ngaySinh) { this.ngaySinh = ngaySinh; }

    public String getSoDienThoai() { return soDienThoai; }
    public void setSoDienThoai(String soDienThoai) { this.soDienThoai = soDienThoai; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDiaChi() { return diaChi; }
    public void setDiaChi(String diaChi) { this.diaChi = diaChi; }

    public String getQuocTich() { return quocTich; }
    public void setQuocTich(String quocTich) { this.quocTich = quocTich; }

    // ===== toString =====
    @Override
    public String toString() {
        return "KhachHang{" +
                "dinhDanh='" + dinhDanh + '\'' +
                ", hoTen='" + hoTen + '\'' +
                ", gioiTinh='" + gioiTinh + '\'' +
                ", ngaySinh=" + ngaySinh +
                ", soDienThoai='" + soDienThoai + '\'' +
                ", email='" + email + '\'' +
                ", diaChi='" + diaChi + '\'' +
                ", quocTich='" + quocTich + '\'' +
                '}';
    }

    // ================= CRUD (JDBC qua aivenConnection) =================

    // CREATE
    public static boolean insert(KhachHang kh) {
        String sql = "INSERT INTO khach_hang " +
                "(dinh_danh, ho_ten, gioi_tinh, ngay_sinh, so_dien_thoai, email, dia_chi, quoc_tich) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection cn = aivenConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, kh.getDinhDanh());
            ps.setString(2, kh.getHoTen());
            ps.setString(3, kh.getGioiTinh());
            if (kh.getNgaySinh() != null) {
                ps.setDate(4, Date.valueOf(kh.getNgaySinh()));
            } else {
                ps.setNull(4, Types.DATE);
            }
            ps.setString(5, kh.getSoDienThoai());
            ps.setString(6, kh.getEmail());
            ps.setString(7, kh.getDiaChi());
            ps.setString(8, kh.getQuocTich());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // READ - by id
    public static KhachHang findById(String dinhDanh) {
        String sql = "SELECT * FROM khach_hang WHERE dinh_danh = ?";
        try (Connection cn = aivenConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, dinhDanh);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // READ - all
    public static List<KhachHang> findAll() {
        List<KhachHang> list = new ArrayList<>();
        String sql = "SELECT * FROM khach_hang ORDER BY ho_ten";
        try (Connection cn = aivenConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // SEARCH - theo tên / sđt / định danh
    public static List<KhachHang> search(String keyword) {
        List<KhachHang> list = new ArrayList<>();
        String k = "%" + (keyword == null ? "" : keyword.trim()) + "%";
        String sql = "SELECT * FROM khach_hang " +
                     "WHERE ho_ten LIKE ? OR so_dien_thoai LIKE ? OR dinh_danh LIKE ? " +
                     "ORDER BY ho_ten";
        try (Connection cn = aivenConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, k);
            ps.setString(2, k);
            ps.setString(3, k);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // UPDATE
    public static boolean update(KhachHang kh) {
        String sql = "UPDATE khach_hang SET " +
                "ho_ten = ?, gioi_tinh = ?, ngay_sinh = ?, so_dien_thoai = ?, email = ?, dia_chi = ?, quoc_tich = ? " +
                "WHERE dinh_danh = ?";
        try (Connection cn = aivenConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, kh.getHoTen());
            ps.setString(2, kh.getGioiTinh());
            if (kh.getNgaySinh() != null) {
                ps.setDate(3, Date.valueOf(kh.getNgaySinh()));
            } else {
                ps.setNull(3, Types.DATE);
            }
            ps.setString(4, kh.getSoDienThoai());
            ps.setString(5, kh.getEmail());
            ps.setString(6, kh.getDiaChi());
            ps.setString(7, kh.getQuocTich());
            ps.setString(8, kh.getDinhDanh());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // DELETE
    public static boolean deleteById(String dinhDanh) {
        String sql = "DELETE FROM khach_hang WHERE dinh_danh = ?";
        try (Connection cn = aivenConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, dinhDanh);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            // Có thể vướng ràng buộc FK nếu khách đã có đặt phòng
            e.printStackTrace();
            return false;
        }
    }

    // ===== Helper =====
    private static KhachHang mapRow(ResultSet rs) throws SQLException {
        KhachHang kh = new KhachHang();
        kh.setDinhDanh(rs.getString("dinh_danh"));
        kh.setHoTen(rs.getString("ho_ten"));
        kh.setGioiTinh(rs.getString("gioi_tinh"));

        Date d = rs.getDate("ngay_sinh");
        kh.setNgaySinh(d != null ? d.toLocalDate() : null);

        kh.setSoDienThoai(rs.getString("so_dien_thoai"));
        kh.setEmail(rs.getString("email"));
        kh.setDiaChi(rs.getString("dia_chi"));
        kh.setQuocTich(rs.getString("quoc_tich"));
        return kh;
    }

    // equals/hashCode theo khóa chính
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof KhachHang)) return false;
        KhachHang that = (KhachHang) o;
        return Objects.equals(dinhDanh, that.dinhDanh);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dinhDanh);
    }
}
