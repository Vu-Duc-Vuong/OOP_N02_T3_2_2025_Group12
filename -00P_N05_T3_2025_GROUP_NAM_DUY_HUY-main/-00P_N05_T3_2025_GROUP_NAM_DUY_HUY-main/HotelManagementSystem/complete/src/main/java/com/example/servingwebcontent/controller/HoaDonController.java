package com.example.servingwebcontent.controller;

import com.example.servingwebcontent.model.DatPhong;
import com.example.servingwebcontent.model.HoaDon;
import com.example.servingwebcontent.model.PaymentMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/hoa-don")
public class HoaDonController {

    // ===== Danh sách tất cả hoá đơn =====
    @GetMapping
    public List<HoaDon> all() {
        return HoaDon.findAll();
    }

    // ===== Lấy 1 hoá đơn theo mã =====
    @GetMapping("/{maHD}")
    public ResponseEntity<HoaDon> one(@PathVariable("maHD") String maHD) {
        HoaDon hd = HoaDon.findById(maHD);
        return hd != null ? ResponseEntity.ok(hd) : ResponseEntity.notFound().build();
    }

    // ===== Lấy hoá đơn theo mã đặt phòng (mới nhất) =====
    @GetMapping("/booking/{maDP}")
    public ResponseEntity<HoaDon> byBooking(@PathVariable("maDP") String maDP) {
        HoaDon hd = HoaDon.findByBooking(maDP);
        return hd != null ? ResponseEntity.ok(hd) : ResponseEntity.notFound().build();
    }

    // ===== Tạo hoá đơn từ đặt phòng (tự tính tiền phòng, DV, thuế) =====
    @PostMapping("/from-booking")
    public ResponseEntity<?> createFromBooking(@RequestBody CreateInvoiceRequest req) {
        if (req == null || isBlank(req.maDatPhong())) {
            return ResponseEntity.badRequest().body("Thiếu maDatPhong");
        }
        if (DatPhong.findById(req.maDatPhong()) == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Đặt phòng không tồn tại");
        }

        PaymentMethod pt;
        try {
            pt = req.phuongThuc() != null ? PaymentMethod.valueOf(req.phuongThuc()) : PaymentMethod.TIEN_MAT;
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("phuongThuc không hợp lệ");
        }

        String maHD = (req.maHoaDon() == null || req.maHoaDon().isBlank())
                ? "HD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase()
                : req.maHoaDon();

        // Tạo đối tượng từ booking (VAT 10% đã nằm trong HoaDon)
        HoaDon hd = HoaDon.buildFromBooking(maHD, req.maDatPhong(),
                req.giamGia() != null ? req.giamGia() : 0.0, pt);

        boolean ok = HoaDon.insert(hd);
        return ok ? ResponseEntity.status(HttpStatus.CREATED).body(hd)
                  : ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Không thể tạo hoá đơn");
    }

    // ===== Cập nhật hoá đơn (đổi giảm giá / phương thức + tính lại tổng) =====
    @PutMapping("/{maHD}")
    public ResponseEntity<?> update(@PathVariable("maHD") String maHD, @RequestBody UpdateInvoiceRequest req) {
        HoaDon hd = HoaDon.findById(maHD);
        if (hd == null) return ResponseEntity.notFound().build();

        // cập nhật các trường cho phép
        if (req.giamGia() != null && req.giamGia() >= 0) {
            hd.setGiamGia(req.giamGia());
        }
        if (req.phuongThuc() != null) {
            try {
                hd.setPhuongThuc(PaymentMethod.valueOf(req.phuongThuc()));
            } catch (Exception e) {
                return ResponseEntity.badRequest().body("phuongThuc không hợp lệ");
            }
        }

        // tính lại tổng dựa trên booking
        hd.recalcTotalsFromBooking();

        boolean ok = HoaDon.update(hd);
        return ok ? ResponseEntity.ok(hd)
                  : ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Không thể cập nhật hoá đơn");
    }

    // ===== Tính lại hoá đơn từ booking (khi DV thay đổi) =====
    @PostMapping("/{maHD}/recalc")
    public ResponseEntity<?> recalc(@PathVariable("maHD") String maHD) {
        HoaDon hd = HoaDon.findById(maHD);
        if (hd == null) return ResponseEntity.notFound().build();

        hd.recalcTotalsFromBooking();
        boolean ok = HoaDon.update(hd);
        return ok ? ResponseEntity.ok(hd) : ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Không thể tính lại");
    }

    // ===== Xoá hoá đơn =====
    @DeleteMapping("/{maHD}")
    public ResponseEntity<?> delete(@PathVariable("maHD") String maHD) {
        boolean ok = HoaDon.deleteById(maHD);
        return ok ? ResponseEntity.noContent().build()
                  : ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Không thể xoá hoá đơn");
    }

    // ===== DTO =====
    public record CreateInvoiceRequest(String maHoaDon, String maDatPhong, Double giamGia, String phuongThuc) {}
    public record UpdateInvoiceRequest(Double giamGia, String phuongThuc) {}

    private static boolean isBlank(String s) { return s == null || s.trim().isEmpty(); }
}
