package com.example.servingwebcontent.model;

import com.example.servingwebcontent.database.aivenConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DatPhong {
    // ===== Fields =====
    private String maDatPhong;       // PK
    private String dinhDanhKhach;    // FK -> khach_hang.dinh_danh
    private String maPhong;          // FK -> phong_khach_san.ma_phong
    private LocalDate ngayNhan;
    private LocalDate ngayTra;
    private int soKhach;
    private BookingStatus trangThai; // DA_DAT, DANG_O, DA_TRA_PHONG, HUY

    // ===== Constructors =====
    public DatPhong() {}

    public DatPhong(String maDatPhong, String dinhDanhKhach, String maPhong,
                    LocalDate ngayNhan, LocalDate ngayTra, int soKhach, BookingStatus trangThai) {
        this.maDatPhong = maDatPhong;
        this.dinhDanhKhach = dinhDanhKhach;
        this.maPhong = maPhong;
        this.ngayNhan = ngayNhan;
        this.ngayTra = ngayTra;
        this.soKhach = soKhach;
        this.trangThai = trangThai;
    }

    // ===== Getters/Setters =====
    public String getMaDatPhong() { return maDatPhong; }
    public void setMaDatPhong(String maDatPhong) { this.maDatPhong = maDatPhong; }

    public String getDinhDanhKhach() { return dinhDanhKhach; }
    public void setDinhDanhKhach(String dinhDanhKhach) { this.dinhDanhKhach = dinhDanhKhach; }

    public String getMaPhong() { return maPhong; }
    public void setMaPhong(String maPhong) { this.maPhong = maPhong; }

    public LocalDate getNgayNhan() { return ngayNhan; }
    public void setNgayNhan(LocalDate ngayNhan) { this.ngayNhan = ngayNhan; }

    public LocalDate getNgayTra() { return ngayTra; }
    public void setNgayTra(LocalDate ngayTra) { this.ngayTra = ngayTra; }

    public int getSoKhach() { return soKhach; }
    public void setSoKhach(int soKhach) { this.soKhach = soKhach; }

    public BookingStatus getTrangThai() { return trangThai; }
    public void setTrangThai(BookingStatus trangThai) { this.trangThai = trangThai; }

    // ===== toString =====
    @Override
    public String toString() {
        return "DatPhong{" +
                "maDatPhong='" + maDatPhong + '\'' +
                ", dinhDanhKhach='" + dinhDanhKhach + '\'' +
                ", maPhong='" + maPhong + '\'' +
                ", ngayNhan=" + ngayNhan +
                ", ngayTra=" + ngayTra +
                ", soKhach=" + soKhach +
                ", trangThai=" + trangThai +
                '}';
    }

    // ================= CRUD (JDBC) =================

    // CREATE (mặc định trạng thái DA_DAT). Có kiểm tra phòng trống trước khi chèn.
    public static boolean insert(DatPhong dp) {
        if (dp == null) return false;
        if (dp.getNgayNhan() == null || dp.getNgayTra() == null) return false;
        if (!isPhongTrong(dp.getMaPhong(), dp.getNgayNhan(), dp.getNgayTra())) return false;

        final String sql = "INSERT INTO dat_phong " +
                "(ma_dat_phong, dinh_danh_khach, ma_phong, ngay_nhan, ngay_tra, so_khach, trang_thai) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection cn = aivenConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, dp.getMaDatPhong());
            ps.setString(2, dp.getDinhDanhKhach());
            ps.setString(3, dp.getMaPhong());
            ps.setDate(4, Date.valueOf(dp.getNgayNhan()));
            ps.setDate(5, Date.valueOf(dp.getNgayTra()));
            ps.setInt(6, dp.getSoKhach());
            ps.setString(7, (dp.getTrangThai() != null ? dp.getTrangThai() : BookingStatus.DA_DAT).name());

            boolean ok = ps.executeUpdate() > 0;
            if (ok) {
                // Cập nhật trạng thái phòng thành DA_DAT (đang có booking chờ)
                PhongKhachSan.updateStatus(dp.getMaPhong(), RoomStatus.DA_DAT);
            }
            return ok;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // READ - by id
    public static DatPhong findById(String maDatPhong) {
        final String sql = "SELECT * FROM dat_phong WHERE ma_dat_phong = ?";
        try (Connection cn = aivenConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, maDatPhong);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // READ - all
    public static List<DatPhong> findAll() {
        List<DatPhong> list = new ArrayList<>();
        final String sql = "SELECT * FROM dat_phong ORDER BY ngay_nhan DESC";
        try (Connection cn = aivenConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // READ - by customer
    public static List<DatPhong> findByKhach(String dinhDanhKhach) {
        List<DatPhong> list = new ArrayList<>();
        final String sql = "SELECT * FROM dat_phong WHERE dinh_danh_khach = ? ORDER BY ngay_nhan DESC";
        try (Connection cn = aivenConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, dinhDanhKhach);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // UPDATE (có kiểm tra phòng trống khi thay đổi ngày/phòng)
    public static boolean update(DatPhong dp) {
        if (dp == null) return false;
        // Nếu đổi ngày/phòng, cần đảm bảo không trùng
        DatPhong old = findById(dp.getMaDatPhong());
        if (old == null) return false;

        boolean thayDoiKhoangNgay = !Objects.equals(dp.getNgayNhan(), old.getNgayNhan())
                || !Objects.equals(dp.getNgayTra(), old.getNgayTra());
        boolean thayDoiPhong = !Objects.equals(dp.getMaPhong(), old.getMaPhong());

        if (thayDoiKhoangNgay || thayDoiPhong) {
            if (!isPhongTrong(dp.getMaPhong(), dp.getNgayNhan(), dp.getNgayTra(), dp.getMaDatPhong()))
                return false;
        }

        final String sql = "UPDATE dat_phong SET " +
                "dinh_danh_khach=?, ma_phong=?, ngay_nhan=?, ngay_tra=?, so_khach=?, trang_thai=? " +
                "WHERE ma_dat_phong=?";
        try (Connection cn = aivenConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, dp.getDinhDanhKhach());
            ps.setString(2, dp.getMaPhong());
            ps.setDate(3, Date.valueOf(dp.getNgayNhan()));
            ps.setDate(4, Date.valueOf(dp.getNgayTra()));
            ps.setInt(5, dp.getSoKhach());
            ps.setString(6, dp.getTrangThai().name());
            ps.setString(7, dp.getMaDatPhong());

            boolean ok = ps.executeUpdate() > 0;

            // Nếu đổi phòng, có thể cân nhắc cập nhật lại trạng thái của phòng cũ/mới (đơn giản hoá ở Controller/Service)
            return ok;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // DELETE
    public static boolean deleteById(String maDatPhong) {
        final String sql = "DELETE FROM dat_phong WHERE ma_dat_phong = ?";
        try (Connection cn = aivenConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, maDatPhong);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            // Lưu ý: nếu đã phát sinh hóa đơn, cần kiểm soát ở tầng nghiệp vụ
            e.printStackTrace();
            return false;
        }
    }

    // ================= Nghiệp vụ: phòng trống, checkin, checkout, hủy =================

    // Kiểm tra phòng trống cho khoảng ngày (không bỏ qua chính mình)
    public static boolean isPhongTrong(String maPhong, LocalDate ngayNhan, LocalDate ngayTra) {
        return isPhongTrong(maPhong, ngayNhan, ngayTra, null);
    }

    // Overload: bỏ qua một booking cụ thể (khi update)
    public static boolean isPhongTrong(String maPhong, LocalDate ngayNhan, LocalDate ngayTra, String ignoreMaDatPhong) {
        /*
         * Phòng là KHÔNG trống nếu tồn tại booking d.trang_thai in ('DA_DAT','DANG_O')
         * với khoảng ngày giao với [ngayNhan, ngayTra)
         * Điều kiện overlap:
         *   NOT (ngayTra <= d.ngay_nhan OR ngayNhan >= d.ngay_tra)
         */
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT COUNT(1) FROM dat_phong d ")
          .append("WHERE d.ma_phong = ? ")
          .append("AND d.trang_thai IN ('DA_DAT','DANG_O') ")
          .append("AND NOT (? <= d.ngay_nhan OR ? >= d.ngay_tra) ");
        if (ignoreMaDatPhong != null) {
            sb.append("AND d.ma_dat_phong <> ? ");
        }
        final String sql = sb.toString();

        try (Connection cn = aivenConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            int idx = 1;
            ps.setString(idx++, maPhong);
            ps.setDate(idx++, Date.valueOf(ngayTra));
            ps.setDate(idx++, Date.valueOf(ngayNhan));
            if (ignoreMaDatPhong != null) ps.setString(idx, ignoreMaDatPhong);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int cnt = rs.getInt(1);
                    return cnt == 0; // không có trùng => trống
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // lỗi coi như không trống để an toàn
    }

    // CHECK-IN: đổi booking -> DANG_O, phòng -> DANG_O (transaction)
    public static boolean checkIn(String maDatPhong) {
        final String sqlUpdateBooking = "UPDATE dat_phong SET trang_thai='DANG_O' WHERE ma_dat_phong=?";
        final String sqlFindPhong = "SELECT ma_phong FROM dat_phong WHERE ma_dat_phong=?";
        try (Connection cn = aivenConnection.getConnection()) {
            cn.setAutoCommit(false);
            String maPhong = null;

            try (PreparedStatement psFind = cn.prepareStatement(sqlFindPhong)) {
                psFind.setString(1, maDatPhong);
                try (ResultSet rs = psFind.executeQuery()) {
                    if (rs.next()) maPhong = rs.getString(1);
                }
            }

            if (maPhong == null) { cn.rollback(); return false; }

            try (PreparedStatement ps = cn.prepareStatement(sqlUpdateBooking)) {
                ps.setString(1, maDatPhong);
                if (ps.executeUpdate() <= 0) { cn.rollback(); return false; }
            }

            // cập nhật phòng
            try (PreparedStatement ps2 = cn.prepareStatement(
                    "UPDATE phong_khach_san SET tinh_trang='DANG_O' WHERE ma_phong=?")) {
                ps2.setString(1, maPhong);
                if (ps2.executeUpdate() <= 0) { cn.rollback(); return false; }
            }

            cn.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // CHECK-OUT: đổi booking -> DA_TRA_PHONG, phòng -> TRONG (transaction)
    public static boolean checkOut(String maDatPhong) {
        final String sqlFindPhong = "SELECT ma_phong FROM dat_phong WHERE ma_dat_phong=?";
        try (Connection cn = aivenConnection.getConnection()) {
            cn.setAutoCommit(false);
            String maPhong = null;

            try (PreparedStatement psFind = cn.prepareStatement(sqlFindPhong)) {
                psFind.setString(1, maDatPhong);
                try (ResultSet rs = psFind.executeQuery()) {
                    if (rs.next()) maPhong = rs.getString(1);
                }
            }
            if (maPhong == null) { cn.rollback(); return false; }

            try (PreparedStatement ps = cn.prepareStatement(
                    "UPDATE dat_phong SET trang_thai='DA_TRA_PHONG' WHERE ma_dat_phong=?")) {
                ps.setString(1, maDatPhong);
                if (ps.executeUpdate() <= 0) { cn.rollback(); return false; }
            }

            // Nếu còn các booking khác trong tương lai của phòng này, không set TRONG ở đây.
            // Đơn giản hoá: set TRONG, Controller có thể điều chỉnh theo lịch.
            try (PreparedStatement ps2 = cn.prepareStatement(
                    "UPDATE phong_khach_san SET tinh_trang='TRONG' WHERE ma_phong=?")) {
                ps2.setString(1, maPhong);
                if (ps2.executeUpdate() <= 0) { cn.rollback(); return false; }
            }

            cn.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // HỦY: đổi booking -> HUY, phòng -> TRONG (nếu chưa check-in)
    public static boolean huy(String maDatPhong) {
        final String sqlFind = "SELECT ma_phong, trang_thai FROM dat_phong WHERE ma_dat_phong=?";
        try (Connection cn = aivenConnection.getConnection()) {
            cn.setAutoCommit(false);

            String maPhong = null;
            String trangThai = null;

            try (PreparedStatement psFind = cn.prepareStatement(sqlFind)) {
                psFind.setString(1, maDatPhong);
                try (ResultSet rs = psFind.executeQuery()) {
                    if (rs.next()) {
                        maPhong = rs.getString("ma_phong");
                        trangThai = rs.getString("trang_thai");
                    }
                }
            }
            if (maPhong == null) { cn.rollback(); return false; }

            try (PreparedStatement ps = cn.prepareStatement(
                    "UPDATE dat_phong SET trang_thai='HUY' WHERE ma_dat_phong=?")) {
                ps.setString(1, maDatPhong);
                if (ps.executeUpdate() <= 0) { cn.rollback(); return false; }
            }

            // Nếu chưa check-in (DA_DAT), trả phòng về TRONG.
            if ("DA_DAT".equals(trangThai)) {
                try (PreparedStatement ps2 = cn.prepareStatement(
                        "UPDATE phong_khach_san SET tinh_trang='TRONG' WHERE ma_phong=?")) {
                    ps2.setString(1, maPhong);
                    if (ps2.executeUpdate() <= 0) { cn.rollback(); return false; }
                }
            }

            cn.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ===== Helper mapRow =====
    private static DatPhong mapRow(ResultSet rs) throws SQLException {
        DatPhong dp = new DatPhong();
        dp.setMaDatPhong(rs.getString("ma_dat_phong"));
        dp.setDinhDanhKhach(rs.getString("dinh_danh_khach"));
        dp.setMaPhong(rs.getString("ma_phong"));

        Date n1 = rs.getDate("ngay_nhan");
        dp.setNgayNhan(n1 != null ? n1.toLocalDate() : null);

        Date n2 = rs.getDate("ngay_tra");
        dp.setNgayTra(n2 != null ? n2.toLocalDate() : null);

        dp.setSoKhach(rs.getInt("so_khach"));

        String st = rs.getString("trang_thai");
        dp.setTrangThai(st != null ? BookingStatus.valueOf(st) : null);
        return dp;
        }

    // equals/hashCode theo PK
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DatPhong)) return false;
        DatPhong datPhong = (DatPhong) o;
        return Objects.equals(maDatPhong, datPhong.maDatPhong);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maDatPhong);
    }
}
