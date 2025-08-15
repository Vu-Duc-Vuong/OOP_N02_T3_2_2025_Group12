package com.example.servingwebcontent.model;

import com.example.servingwebcontent.database.aivenConnection;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HoaDon {
    // ===== Cấu hình thuế =====
    private static final double VAT_RATE = 0.10; // 10%

    // ===== Fields =====
    private String maHoaDon;            // PK
    private String maDatPhong;          // FK -> dat_phong
    private double tongTienPhong;       // tính từ số đêm * giá/đêm
    private double tongTienDichVu;      // sum(chi_tiet_dich_vu.thanh_tien)
    private double thue;                // (phong + dv) * VAT_RATE
    private double giamGia;             // nhập tay hoặc chính sách
    private double tongThanhToan;       // phong + dv + thue - giamGia
    private LocalDateTime ngayThanhToan;
    private PaymentMethod phuongThuc;   // TIEN_MAT, THE, CHUYEN_KHOAN, VI_DIEN_TU

    // ===== Constructors =====
    public HoaDon() {}

    public HoaDon(String maHoaDon, String maDatPhong, double tongTienPhong, double tongTienDichVu,
                  double thue, double giamGia, double tongThanhToan,
                  LocalDateTime ngayThanhToan, PaymentMethod phuongThuc) {
        this.maHoaDon = maHoaDon;
        this.maDatPhong = maDatPhong;
        this.tongTienPhong = tongTienPhong;
        this.tongTienDichVu = tongTienDichVu;
        this.thue = thue;
        this.giamGia = giamGia;
        this.tongThanhToan = tongThanhToan;
        this.ngayThanhToan = ngayThanhToan;
        this.phuongThuc = phuongThuc;
    }

    // ===== Getters/Setters =====
    public String getMaHoaDon() { return maHoaDon; }
    public void setMaHoaDon(String maHoaDon) { this.maHoaDon = maHoaDon; }

    public String getMaDatPhong() { return maDatPhong; }
    public void setMaDatPhong(String maDatPhong) { this.maDatPhong = maDatPhong; }

    public double getTongTienPhong() { return tongTienPhong; }
    public void setTongTienPhong(double tongTienPhong) { this.tongTienPhong = tongTienPhong; }

    public double getTongTienDichVu() { return tongTienDichVu; }
    public void setTongTienDichVu(double tongTienDichVu) { this.tongTienDichVu = tongTienDichVu; }

    public double getThue() { return thue; }
    public void setThue(double thue) { this.thue = thue; }

    public double getGiamGia() { return giamGia; }
    public void setGiamGia(double giamGia) { this.giamGia = giamGia; }

    public double getTongThanhToan() { return tongThanhToan; }
    public void setTongThanhToan(double tongThanhToan) { this.tongThanhToan = tongThanhToan; }

    public LocalDateTime getNgayThanhToan() { return ngayThanhToan; }
    public void setNgayThanhToan(LocalDateTime ngayThanhToan) { this.ngayThanhToan = ngayThanhToan; }

    public PaymentMethod getPhuongThuc() { return phuongThuc; }
    public void setPhuongThuc(PaymentMethod phuongThuc) { this.phuongThuc = phuongThuc; }

    // ===== toString =====
    @Override
    public String toString() {
        return "HoaDon{" +
                "maHoaDon='" + maHoaDon + '\'' +
                ", maDatPhong='" + maDatPhong + '\'' +
                ", tongTienPhong=" + tongTienPhong +
                ", tongTienDichVu=" + tongTienDichVu +
                ", thue=" + thue +
                ", giamGia=" + giamGia +
                ", tongThanhToan=" + tongThanhToan +
                ", ngayThanhToan=" + ngayThanhToan +
                ", phuongThuc=" + phuongThuc +
                '}';
    }

    // =================== Helper tính toán ===================

    // Làm tròn 2 chữ số thập phân theo chuẩn ngân hàng
    private static double round2(double v) {
        return BigDecimal.valueOf(v).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    // Tiền phòng = số đêm * giá/đêm (tối thiểu 1 đêm)
    public static double tinhTongTienPhongByBooking(String maDatPhong) {
        final String sql = "SELECT d.ngay_nhan, d.ngay_tra, p.gia_moi_dem " +
                           "FROM dat_phong d JOIN phong_khach_san p ON d.ma_phong = p.ma_phong " +
                           "WHERE d.ma_dat_phong = ?";
        try (Connection cn = aivenConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, maDatPhong);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Date n1 = rs.getDate("ngay_nhan");
                    Date n2 = rs.getDate("ngay_tra");
                    double gia = rs.getDouble("gia_moi_dem");

                    LocalDate nhan = n1.toLocalDate();
                    LocalDate tra  = n2.toLocalDate();
                    long dem = ChronoUnit.DAYS.between(nhan, tra);
                    if (dem < 1) dem = 1;

                    return round2(dem * gia);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    // Tiền dịch vụ = SUM(thanh_tien) theo booking
    public static double tinhTongTienDichVuByBooking(String maDatPhong) {
        return round2(ChiTietDichVu.sumThanhTienByBooking(maDatPhong));
    }

    // Xây dựng hóa đơn từ booking (tự tính các khoản theo VAT 10%)
    public static HoaDon buildFromBooking(String maHoaDon, String maDatPhong, double giamGia,
                                          PaymentMethod phuongThuc) {
        double tp = tinhTongTienPhongByBooking(maDatPhong);
        double tdv = tinhTongTienDichVuByBooking(maDatPhong);
        double thue = round2((tp + tdv) * VAT_RATE);
        double tong = round2(tp + tdv + thue - (giamGia < 0 ? 0 : giamGia));

        return new HoaDon(
                maHoaDon,
                maDatPhong,
                tp,
                tdv,
                thue,
                giamGia < 0 ? 0 : round2(giamGia),
                tong,
                LocalDateTime.now(),
                phuongThuc
        );
    }

    // Tính lại các khoản cho đối tượng hiện tại (nếu muốn refresh)
    public void recalcTotalsFromBooking() {
        double tp = tinhTongTienPhongByBooking(this.maDatPhong);
        double tdv = tinhTongTienDichVuByBooking(this.maDatPhong);
        double thue = round2((tp + tdv) * VAT_RATE);
        double tong = round2(tp + tdv + thue - (this.giamGia < 0 ? 0 : this.giamGia));

        this.tongTienPhong = tp;
        this.tongTienDichVu = tdv;
        this.thue = thue;
        this.tongThanhToan = tong;
    }

    // =================== CRUD (JDBC) ===================

    // CREATE
    public static boolean insert(HoaDon hd) {
        String sql = "INSERT INTO hoa_don " +
                "(ma_hoa_don, ma_dat_phong, tong_tien_phong, tong_tien_dich_vu, thue, giam_gia, tong_thanh_toan, ngay_thanh_toan, phuong_thuc) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection cn = aivenConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, hd.getMaHoaDon());
            ps.setString(2, hd.getMaDatPhong());
            ps.setDouble(3, round2(hd.getTongTienPhong()));
            ps.setDouble(4, round2(hd.getTongTienDichVu()));
            ps.setDouble(5, round2(hd.getThue()));
            ps.setDouble(6, round2(hd.getGiamGia()));
            ps.setDouble(7, round2(hd.getTongThanhToan()));
            ps.setTimestamp(8, Timestamp.valueOf(hd.getNgayThanhToan() != null ? hd.getNgayThanhToan() : LocalDateTime.now()));
            ps.setString(9, hd.getPhuongThuc().name());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Tiện: tạo & lưu hóa đơn từ booking
    public static boolean taoVaLuuHoaDonTuBooking(String maHoaDon, String maDatPhong, double giamGia,
                                                  PaymentMethod phuongThuc) {
        HoaDon hd = buildFromBooking(maHoaDon, maDatPhong, giamGia, phuongThuc);
        return insert(hd);
    }

    // READ - by id
    public static HoaDon findById(String maHoaDon) {
        String sql = "SELECT * FROM hoa_don WHERE ma_hoa_don = ?";
        try (Connection cn = aivenConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, maHoaDon);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // READ - by booking
    public static HoaDon findByBooking(String maDatPhong) {
        String sql = "SELECT * FROM hoa_don WHERE ma_dat_phong = ? ORDER BY ngay_thanh_toan DESC LIMIT 1";
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
    public static List<HoaDon> findAll() {
        List<HoaDon> list = new ArrayList<>();
        String sql = "SELECT * FROM hoa_don ORDER BY ngay_thanh_toan DESC";
        try (Connection cn = aivenConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // UPDATE (gợi ý: thường không chỉnh tổng sau khi chốt; nhưng để đủ CRUD)
    public static boolean update(HoaDon hd) {
        String sql = "UPDATE hoa_don SET " +
                "ma_dat_phong=?, tong_tien_phong=?, tong_tien_dich_vu=?, thue=?, giam_gia=?, tong_thanh_toan=?, ngay_thanh_toan=?, phuong_thuc=? " +
                "WHERE ma_hoa_don=?";
        try (Connection cn = aivenConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, hd.getMaDatPhong());
            ps.setDouble(2, round2(hd.getTongTienPhong()));
            ps.setDouble(3, round2(hd.getTongTienDichVu()));
            ps.setDouble(4, round2(hd.getThue()));
            ps.setDouble(5, round2(hd.getGiamGia()));
            ps.setDouble(6, round2(hd.getTongThanhToan()));
            ps.setTimestamp(7, Timestamp.valueOf(hd.getNgayThanhToan()));
            ps.setString(8, hd.getPhuongThuc().name());
            ps.setString(9, hd.getMaHoaDon());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // DELETE
    public static boolean deleteById(String maHoaDon) {
        String sql = "DELETE FROM hoa_don WHERE ma_hoa_don = ?";
        try (Connection cn = aivenConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, maHoaDon);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            // Lưu ý: cân nhắc không xoá hoá đơn đã phát hành (tuỳ chính sách)
            e.printStackTrace();
            return false;
        }
    }

    // ===== Mapping =====
    private static HoaDon mapRow(ResultSet rs) throws SQLException {
        HoaDon h = new HoaDon();
        h.setMaHoaDon(rs.getString("ma_hoa_don"));
        h.setMaDatPhong(rs.getString("ma_dat_phong"));
        h.setTongTienPhong(rs.getDouble("tong_tien_phong"));
        h.setTongTienDichVu(rs.getDouble("tong_tien_dich_vu"));
        h.setThue(rs.getDouble("thue"));
        h.setGiamGia(rs.getDouble("giam_gia"));
        h.setTongThanhToan(rs.getDouble("tong_thanh_toan"));
        Timestamp ts = rs.getTimestamp("ngay_thanh_toan");
        h.setNgayThanhToan(ts != null ? ts.toLocalDateTime() : null);
        String pt = rs.getString("phuong_thuc");
        h.setPhuongThuc(pt != null ? PaymentMethod.valueOf(pt) : null);
        return h;
    }

    // equals/hashCode theo PK
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HoaDon)) return false;
        HoaDon hoaDon = (HoaDon) o;
        return Objects.equals(maHoaDon, hoaDon.maHoaDon);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maHoaDon);
    }
}
