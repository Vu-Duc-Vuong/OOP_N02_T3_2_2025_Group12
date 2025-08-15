package com.example.servingwebcontent.controller;

import com.example.servingwebcontent.core.HotelCoreService;
import com.example.servingwebcontent.core.HotelCoreService.KetQuaDatPhong;
import com.example.servingwebcontent.model.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.sql.*;
import java.util.Map;

// ledger imports
import com.example.servingwebcontent.database.aivenConnection;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/core-flow")
public class CoreFlowController {

    private final HotelCoreService hotelCoreService;

    public CoreFlowController(HotelCoreService hotelCoreService) {
        this.hotelCoreService = hotelCoreService;
    }

    // ===================== 1) Đặt phòng + (DV) + Xuất hoá đơn
    // =====================
    public record FlowRequest(
            String dinhDanhKhach,
            String maPhong,
            String ngayNhan, // yyyy-MM-dd
            String ngayTra, // yyyy-MM-dd
            Integer soKhach,
            Map<String, Integer> dichVuSoLuong, // { "DV_...": 2, ... }
            Double giamGia,
            String phuongThuc // TIEN_MAT | THE | CHUYEN_KHOAN | VI_DIEN_TU
    ) {
    }

    @PostMapping("/dat-phong-thanh-toan")
    public ResponseEntity<?> datPhongThanhToan(@RequestBody FlowRequest r) {
        if (r == null || isBlank(r.dinhDanhKhach()) || isBlank(r.maPhong()) ||
                isBlank(r.ngayNhan()) || isBlank(r.ngayTra())) {
            return ResponseEntity.badRequest().body("Thiếu trường bắt buộc");
        }

        final LocalDate nhan, tra;
        try {
            nhan = LocalDate.parse(r.ngayNhan().trim());
            tra = LocalDate.parse(r.ngayTra().trim());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Định dạng ngày phải là yyyy-MM-dd");
        }

        int soKhach = (r.soKhach() == null || r.soKhach() <= 0) ? 1 : r.soKhach();

        PaymentMethod method = null; // để service tự default TIEN_MAT nếu null
        if (!isBlank(r.phuongThuc())) {
            try {
                method = PaymentMethod.valueOf(r.phuongThuc().trim().toUpperCase());
            } catch (Exception e) {
                return ResponseEntity.badRequest().body("phuongThuc không hợp lệ");
            }
        }

        try {
            KetQuaDatPhong kq = hotelCoreService.datPhongVaThanhToan(
                    r.dinhDanhKhach().trim(),
                    r.maPhong().trim(),
                    nhan,
                    tra,
                    soKhach,
                    r.dichVuSoLuong(), // có thể null/empty
                    r.giamGia() == null ? 0.0 : r.giamGia(),
                    method);

            // >>> GHI LEDGER TRƯỚC KHI TRẢ VỀ <<<
            try {
                HoaDon hd = HoaDon.findById(kq.getMaHoaDon());
                DatPhong dp = DatPhong.findById(kq.getMaDatPhong());
                if (hd != null && dp != null) {
                    upsertRevenueLedger(hd, dp); // hàm helper bạn đã thêm trong controller
                }
            } catch (Exception ignore) {
            }

            return ResponseEntity.ok(kq);

        } catch (IllegalArgumentException iae) {
            return ResponseEntity.badRequest().body(iae.getMessage());
        } catch (IllegalStateException ise) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ise.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi không xác định: " + ex.getMessage());
        }
    }

    // ===================== 2) Thêm phòng (qua HotelCoreService)
    // =====================
    public record NewRoomRequest(
            String maPhong,
            String loaiPhong, // DON | DOI | SUITE | FAMILY | DELUXE
            Double giaMoiDem,
            String tinhTrang, // TRONG | DA_DAT | DANG_O | BAO_TRI
            Integer soNguoiToiDa,
            String tienNghiKemTheo) {
    }

    @PostMapping("/them-phong")
    public ResponseEntity<?> themPhong(@RequestBody NewRoomRequest r) {
        if (r == null || isBlank(r.maPhong()) || isBlank(r.loaiPhong()) || r.soNguoiToiDa() == null) {
            return ResponseEntity.badRequest().body("Thiếu maPhong/loaiPhong/soNguoiToiDa");
        }
        final RoomType loai;
        final RoomStatus status;
        try {
            loai = RoomType.valueOf(r.loaiPhong().trim().toUpperCase());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("loaiPhong không hợp lệ");
        }
        try {
            status = isBlank(r.tinhTrang()) ? RoomStatus.TRONG
                    : RoomStatus.valueOf(r.tinhTrang().trim().toUpperCase());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("tinhTrang không hợp lệ");
        }

        try {
            PhongKhachSan saved = hotelCoreService.themPhong(
                    r.maPhong().trim(),
                    loai,
                    r.giaMoiDem() == null ? 0.0 : r.giaMoiDem(),
                    status,
                    r.soNguoiToiDa(),
                    r.tienNghiKemTheo());
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (IllegalArgumentException iae) {
            return ResponseEntity.badRequest().body(iae.getMessage());
        } catch (IllegalStateException ise) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ise.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi không xác định: " + ex.getMessage());
        }
    }

    // ===================== 3) Thêm dịch vụ (qua HotelCoreService)
    // =====================
    public record NewServiceRequest(
            String maDichVu,
            String tenDichVu,
            Double gia,
            String loai // THEO_LAN | THEO_GIO
    ) {
    }

    @PostMapping("/them-dich-vu")
    public ResponseEntity<?> themDichVu(@RequestBody NewServiceRequest r) {
        if (r == null || isBlank(r.maDichVu()) || isBlank(r.tenDichVu())) {
            return ResponseEntity.badRequest().body("Thiếu maDichVu/tenDichVu");
        }
        final ServiceType type;
        try {
            type = isBlank(r.loai()) ? ServiceType.THEO_LAN
                    : ServiceType.valueOf(r.loai().trim().toUpperCase());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("loai dịch vụ không hợp lệ");
        }

        try {
            DichVu saved = hotelCoreService.themDichVu(
                    r.maDichVu().trim(),
                    r.tenDichVu().trim(),
                    r.gia() == null ? 0.0 : r.gia(),
                    type);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (IllegalArgumentException iae) {
            return ResponseEntity.badRequest().body(iae.getMessage());
        } catch (IllegalStateException ise) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ise.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi không xác định: " + ex.getMessage());
        }
    }

    // ===================== 4) Thanh toán & dọn phụ thuộc & xoá khách
    // =====================
    public record PayAndCleanReq(String maHoaDon, Boolean checkoutFirst, Boolean deleteBooking) {
    }

    public record PayAndCleanRes(String maDatPhong, String dinhDanhKhach,
            int soDongDVXoa, boolean daXoaHoaDon, boolean daXoaBooking, boolean daXoaKhach,
            String message) {
    }

    @PostMapping("/pay-and-clean")
    public ResponseEntity<?> payAndClean(@RequestBody PayAndCleanReq req) {
        if (req == null || isBlank(req.maHoaDon())) {
            return ResponseEntity.badRequest().body("Thiếu maHoaDon");
        }

        // 1) Lấy hoá đơn
        HoaDon hd = HoaDon.findById(req.maHoaDon());
        if (hd == null)
            return ResponseEntity.badRequest().body("Không tìm thấy hoá đơn");

        // 2) Lấy booking & khách
        String maDP = hd.getMaDatPhong();
        DatPhong dp = DatPhong.findById(maDP);
        if (dp == null)
            return ResponseEntity.badRequest().body("Không tìm thấy booking gắn với hoá đơn");
        String khId = dp.getDinhDanhKhach();

        // 3) (tuỳ chọn) checkout trước
        if (Boolean.TRUE.equals(req.checkoutFirst())) {
            try {
                DatPhong.checkOut(maDP);
            } catch (Exception ignored) {
            }
        }

        // 4) Recalc hoá đơn để đảm bảo tổng mới nhất
        try {
            hd.recalcTotalsFromBooking();
            HoaDon.update(hd);
        } catch (Exception ignore) {
        }

        // 4.5) GHI/UPDATE VÀO LEDGER CHO BÁO CÁO (TRƯỚC KHI XOÁ CTDV/HĐ/BOOKING)
        try {
            upsertRevenueLedger(hd, dp); // doanh thu (phòng, DV, thuế…)
            writeServiceLedger(hd, dp); // chi tiết DV theo booking
        } catch (Exception e) {
            System.err.println("Ledger write failed: " + e.getMessage());
        }

        // 4.6) Ghi ledger dịch vụ (để báo cáo vẫn có dữ liệu sau khi xóa CTDV)dich
        try {
            writeServiceLedger(hd, dp);
        } catch (Exception e) {
            System.err.println("writeServiceLedger error: " + e.getMessage());
        }

        // 5) Xoá chi tiết DV của booking
        int deletedCT;
        try {
            deletedCT = ChiTietDichVu.deleteByBooking(maDP);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Xoá chi tiết DV lỗi: " + e.getMessage());
        }

        // 6) Xoá hoá đơn
        boolean delHdOk = HoaDon.deleteById(hd.getMaHoaDon());

        // 7) (tuỳ) Xoá booking
        boolean delDpOk = false;
        if (Boolean.TRUE.equals(req.deleteBooking())) {
            delDpOk = DatPhong.deleteById(maDP);
            if (!delDpOk) {
                return ResponseEntity.badRequest().body("Xoá đặt phòng lỗi (có thể do ràng buộc khác)");
            }
        }

        // 8) Xoá khách (có thể fail nếu còn booking khác)
        boolean delKhOk = KhachHang.deleteById(khId);

        String msg = "Đã thanh toán (recalc), xoá " + deletedCT + " dòng DV, xoá HĐ"
                + (delDpOk ? ", xoá booking" : "")
                + (delKhOk ? ", xoá khách" : " (xoá khách thất bại hoặc còn dữ liệu liên quan)");

        return ResponseEntity.ok(new PayAndCleanRes(
                maDP, khId, deletedCT, delHdOk, delDpOk, delKhOk, msg));
    }

    // ===================== Helpers =====================
    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    private static double nz(Double d) {
        return d == null ? 0.0 : d;
    }

    private static Timestamp toTs(Object v) {
        if (v == null)
            return null;
        if (v instanceof Timestamp t)
            return t;
        if (v instanceof java.util.Date d)
            return new Timestamp(d.getTime());
        if (v instanceof LocalDateTime ldt)
            return Timestamp.valueOf(ldt);
        // fallback: cố parse chuỗi
        try {
            return Timestamp.valueOf(v.toString());
        } catch (Exception ignore) {
        }
        try {
            return Timestamp.valueOf(LocalDateTime.parse(v.toString()));
        } catch (Exception ignore) {
        }
        return null;
    }

    public void upsertRevenueLedger(HoaDon hd, DatPhong dp) {
        if (hd == null || dp == null)
            return;

        Timestamp ts = toTs(hd.getNgayThanhToan());
        if (ts == null)
            ts = new Timestamp(System.currentTimeMillis());

        // Lấy ngày ở & mã phòng từ booking
        java.sql.Date dNhan = java.sql.Date.valueOf(dp.getNgayNhan()); // LocalDate -> Date
        java.sql.Date dTra = java.sql.Date.valueOf(dp.getNgayTra());

        String sql = """
                INSERT INTO ledger_doanh_thu (
                  ma_hoa_don, ma_dat_phong, dinh_danh_khach, ngay_thanh_toan,
                  ma_phong, ngay_nhan, ngay_tra,
                  tong_tien_phong, tong_tien_dich_vu, thue, giam_gia, tong_thanh_toan
                ) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)
                ON DUPLICATE KEY UPDATE
                  ma_dat_phong=VALUES(ma_dat_phong),
                  dinh_danh_khach=VALUES(dinh_danh_khach),
                  ngay_thanh_toan=VALUES(ngay_thanh_toan),
                  ma_phong=VALUES(ma_phong),
                  ngay_nhan=VALUES(ngay_nhan),
                  ngay_tra=VALUES(ngay_tra),
                  tong_tien_phong=VALUES(tong_tien_phong),
                  tong_tien_dich_vu=VALUES(tong_tien_dich_vu),
                  thue=VALUES(thue),
                  giam_gia=VALUES(giam_gia),
                  tong_thanh_toan=VALUES(tong_thanh_toan)
                """;
        try (Connection cn = aivenConnection.getConnection();
                PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, hd.getMaHoaDon());
            ps.setString(2, hd.getMaDatPhong());
            ps.setString(3, dp.getDinhDanhKhach());
            ps.setTimestamp(4, ts);
            ps.setString(5, dp.getMaPhong());
            ps.setDate(6, dNhan);
            ps.setDate(7, dTra);
            ps.setDouble(8, nz(hd.getTongTienPhong()));
            ps.setDouble(9, nz(hd.getTongTienDichVu()));
            ps.setDouble(10, nz(hd.getThue()));
            ps.setDouble(11, nz(hd.getGiamGia()));
            ps.setDouble(12, nz(hd.getTongThanhToan()));
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Ledger upsert failed: " + e.getMessage());
        }
    }

    /** Ghi tổng hợp dịch vụ của booking vào bảng ledger_ctdv để phục vụ báo cáo */
    private void writeServiceLedger(HoaDon hd, DatPhong dp) {
        if (hd == null || dp == null)
            return;

        String sqlAgg = """
                    SELECT c.ma_dich_vu,
                           COALESCE(dv.ten_dich_vu, c.ma_dich_vu) AS ten,
                           SUM(c.so_luong)   AS so_luong,
                           SUM(c.thanh_tien) AS doanh_thu
                    FROM chi_tiet_dich_vu c
                    LEFT JOIN dich_vu dv ON dv.ma_dich_vu = c.ma_dich_vu
                    WHERE c.ma_dat_phong = ?
                    GROUP BY c.ma_dich_vu, ten
                """;
        String sqlIns = """
                    INSERT INTO ledger_dich_vu
                      (ma_hoa_don, ma_dat_phong, dinh_danh_khach,
                       ma_dich_vu, ten_dich_vu, so_luong, doanh_thu, ngay_thanh_toan)
                    VALUES (?,?,?,?,?,?,?,?)
                """;

        Timestamp ts = toTs(hd.getNgayThanhToan());
        if (ts == null)
            ts = new Timestamp(System.currentTimeMillis());

        try (Connection cn = aivenConnection.getConnection();
                PreparedStatement ps = cn.prepareStatement(sqlAgg);
                PreparedStatement ins = cn.prepareStatement(sqlIns)) {

            ps.setString(1, dp.getMaDatPhong());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ins.setString(1, hd.getMaHoaDon());
                    ins.setString(2, dp.getMaDatPhong());
                    ins.setString(3, dp.getDinhDanhKhach());
                    ins.setString(4, rs.getString("ma_dich_vu"));
                    ins.setString(5, rs.getString("ten"));
                    ins.setLong(6, rs.getLong("so_luong"));
                    ins.setDouble(7, rs.getDouble("doanh_thu"));
                    ins.setTimestamp(8, ts);
                    ins.addBatch();
                }
            }
            ins.executeBatch();
        } catch (SQLException e) {
            System.err.println("writeServiceLedger failed: " + e.getMessage());
        }
    }

}
