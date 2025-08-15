package com.example.servingwebcontent.model;

import com.example.servingwebcontent.database.aivenConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PhongKhachSan {
    // ====== Fields ======
    private String maPhong;             // PK
    private RoomType loaiPhong;         // DON, DOI, SUITE, ...
    private double giaMoiDem;           // VND
    private RoomStatus tinhTrang;       // TRONG, DA_DAT, DANG_O, BAO_TRI
    private int soNguoiToiDa;
    private String tienNghiKemTheo;     // CSV hoặc JSON ngắn

    // ====== Constructors ======
    public PhongKhachSan() {}

    public PhongKhachSan(String maPhong, RoomType loaiPhong, double giaMoiDem,
                         RoomStatus tinhTrang, int soNguoiToiDa, String tienNghiKemTheo) {
        this.maPhong = maPhong;
        this.loaiPhong = loaiPhong;
        this.giaMoiDem = giaMoiDem;
        this.tinhTrang = tinhTrang;
        this.soNguoiToiDa = soNguoiToiDa;
        this.tienNghiKemTheo = tienNghiKemTheo;
    }

    // ====== Getters & Setters ======
    public String getMaPhong() { return maPhong; }
    public void setMaPhong(String maPhong) { this.maPhong = maPhong; }

    public RoomType getLoaiPhong() { return loaiPhong; }
    public void setLoaiPhong(RoomType loaiPhong) { this.loaiPhong = loaiPhong; }

    public double getGiaMoiDem() { return giaMoiDem; }
    public void setGiaMoiDem(double giaMoiDem) { this.giaMoiDem = giaMoiDem; }

    public RoomStatus getTinhTrang() { return tinhTrang; }
    public void setTinhTrang(RoomStatus tinhTrang) { this.tinhTrang = tinhTrang; }

    public int getSoNguoiToiDa() { return soNguoiToiDa; }
    public void setSoNguoiToiDa(int soNguoiToiDa) { this.soNguoiToiDa = soNguoiToiDa; }

    public String getTienNghiKemTheo() { return tienNghiKemTheo; }
    public void setTienNghiKemTheo(String tienNghiKemTheo) { this.tienNghiKemTheo = tienNghiKemTheo; }

    // ====== toString ======
    @Override
    public String toString() {
        return "PhongKhachSan{" +
                "maPhong='" + maPhong + '\'' +
                ", loaiPhong=" + loaiPhong +
                ", giaMoiDem=" + giaMoiDem +
                ", tinhTrang=" + tinhTrang +
                ", soNguoiToiDa=" + soNguoiToiDa +
                ", tienNghiKemTheo='" + tienNghiKemTheo + '\'' +
                '}';
    }

    // ====== CRUD (JDBC qua aivenConnection) ======

    // CREATE
    public static boolean insert(PhongKhachSan p) {
        final String sql = "INSERT INTO phong_khach_san " +
                "(ma_phong, loai_phong, gia_moi_dem, tinh_trang, so_nguoi_toi_da, tien_nghi_kem_theo) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection cn = aivenConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, p.getMaPhong());
            ps.setString(2, p.getLoaiPhong().name());
            ps.setDouble(3, p.getGiaMoiDem());
            ps.setString(4, p.getTinhTrang().name());
            ps.setInt(5, p.getSoNguoiToiDa());
            ps.setString(6, p.getTienNghiKemTheo());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // READ - findById
    public static PhongKhachSan findById(String maPhong) {
        final String sql = "SELECT * FROM phong_khach_san WHERE ma_phong = ?";
        try (Connection cn = aivenConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, maPhong);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // READ - findAll
    public static List<PhongKhachSan> findAll() {
        List<PhongKhachSan> list = new ArrayList<>();
        final String sql = "SELECT * FROM phong_khach_san ORDER BY ma_phong";
        try (Connection cn = aivenConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // UPDATE
    public static boolean update(PhongKhachSan p) {
        final String sql = "UPDATE phong_khach_san SET " +
                "loai_phong = ?, gia_moi_dem = ?, tinh_trang = ?, so_nguoi_toi_da = ?, tien_nghi_kem_theo = ? " +
                "WHERE ma_phong = ?";
        try (Connection cn = aivenConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, p.getLoaiPhong().name());
            ps.setDouble(2, p.getGiaMoiDem());
            ps.setString(3, p.getTinhTrang().name());
            ps.setInt(4, p.getSoNguoiToiDa());
            ps.setString(5, p.getTienNghiKemTheo());
            ps.setString(6, p.getMaPhong());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // DELETE
    public static boolean deleteById(String maPhong) {
        final String sql = "DELETE FROM phong_khach_san WHERE ma_phong = ?";
        try (Connection cn = aivenConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, maPhong);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            // Lưu ý: có thể vi phạm FK nếu phòng đang có booking
            e.printStackTrace();
            return false;
        }
    }

    // UPDATE STATUS nhanh
    public static boolean updateStatus(String maPhong, RoomStatus status) {
        final String sql = "UPDATE phong_khach_san SET tinh_trang = ? WHERE ma_phong = ?";
        try (Connection cn = aivenConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, status.name());
            ps.setString(2, maPhong);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ====== Tìm phòng trống theo khoảng ngày / sức chứa / loại phòng ======
    // Lưu ý: ngày ở dạng java.sql.Date (yyyy-MM-dd)
    public static List<PhongKhachSan> timPhongTrong(java.time.LocalDate ngayNhan,
                                                    java.time.LocalDate ngayTra,
                                                    Integer soNguoiYeuCau,
                                                    RoomType loaiPhongYeuCau) {
        /*
         * Phòng được coi là TRỐNG cho khoảng [ngayNhan, ngayTra) nếu:
         *  - Phòng không ở trạng thái BAO_TRI
         *  - Và KHÔNG tồn tại đặt phòng đang hiệu lực (trang_thai IN ('DA_DAT','DANG_O'))
         *    mà khoản thời gian giao nhau với [ngayNhan, ngayTra)
         *    Điều kiện giao nhau (overlap):
         *      NOT (ngayTra <= old.ngay_nhan OR ngayNhan >= old.ngay_tra)
         */
        List<PhongKhachSan> list = new ArrayList<>();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT p.* FROM phong_khach_san p ")
          .append("WHERE p.tinh_trang <> 'BAO_TRI' ");

        if (soNguoiYeuCau != null) {
            sb.append("AND p.so_nguoi_toi_da >= ? ");
        }
        if (loaiPhongYeuCau != null) {
            sb.append("AND p.loai_phong = ? ");
        }

        sb.append("AND NOT EXISTS ( ")
          .append("  SELECT 1 FROM dat_phong d ")
          .append("  WHERE d.ma_phong = p.ma_phong ")
          .append("    AND d.trang_thai IN ('DA_DAT','DANG_O') ")
          .append("    AND NOT (? <= d.ngay_nhan OR ? >= d.ngay_tra) ")
          .append(") ")
          .append("ORDER BY p.ma_phong");

        final String sql = sb.toString();

        try (Connection cn = aivenConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            int idx = 1;
            if (soNguoiYeuCau != null) ps.setInt(idx++, soNguoiYeuCau);
            if (loaiPhongYeuCau != null) ps.setString(idx++, loaiPhongYeuCau.name());
            ps.setDate(idx++, Date.valueOf(ngayTra));   // ? <= d.ngay_nhan
            ps.setDate(idx,   Date.valueOf(ngayNhan));  // ? >= d.ngay_tra

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Overload: chỉ theo ngày
    public static List<PhongKhachSan> timPhongTrong(java.time.LocalDate ngayNhan,
                                                    java.time.LocalDate ngayTra) {
        return timPhongTrong(ngayNhan, ngayTra, null, null);
    }

    // ====== Helper mapRow ======
    private static PhongKhachSan mapRow(ResultSet rs) throws SQLException {
        PhongKhachSan p = new PhongKhachSan();
        p.setMaPhong(rs.getString("ma_phong"));

        String loai = rs.getString("loai_phong");
        p.setLoaiPhong(loai != null ? RoomType.valueOf(loai) : null);

        p.setGiaMoiDem(rs.getDouble("gia_moi_dem"));

        String st = rs.getString("tinh_trang");
        p.setTinhTrang(st != null ? RoomStatus.valueOf(st) : null);

        p.setSoNguoiToiDa(rs.getInt("so_nguoi_toi_da"));
        p.setTienNghiKemTheo(rs.getString("tien_nghi_kem_theo"));
        return p;
    }

    // ====== equals & hashCode theo khóa chính ======
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PhongKhachSan)) return false;
        PhongKhachSan that = (PhongKhachSan) o;
        return Objects.equals(maPhong, that.maPhong);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maPhong);
    }
}
