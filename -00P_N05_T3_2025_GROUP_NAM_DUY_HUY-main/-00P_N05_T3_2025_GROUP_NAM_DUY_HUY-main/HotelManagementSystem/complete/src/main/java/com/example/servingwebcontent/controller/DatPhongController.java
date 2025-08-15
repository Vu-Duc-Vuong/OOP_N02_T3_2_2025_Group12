package com.example.servingwebcontent.controller;

import com.example.servingwebcontent.model.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/dat-phong")
public class DatPhongController {

    // ===== TÌM PHÒNG TRỐNG =====
    @PostMapping("/tim-phong-trong")
    public ResponseEntity<?> timPhongTrong(@RequestBody TimPhongRequest req) {
        if (req.ngayNhan() == null || req.ngayTra() == null || !req.ngayTra().isAfter(req.ngayNhan())) {
            return ResponseEntity.badRequest().body("ngayTra phải sau ngayNhan");
        }
        RoomType loai = null;
        if (req.loaiPhong() != null) {
            try {
                loai = RoomType.valueOf(req.loaiPhong());
            } catch (Exception e) {
                return ResponseEntity.badRequest().body("loaiPhong không hợp lệ");
            }
        }
        List<PhongKhachSan> ds = PhongKhachSan.timPhongTrong(req.ngayNhan(), req.ngayTra(), req.soNguoi(), loai);
        return ResponseEntity.ok(ds);
    }

    // ===== LIST / GET =====
    @GetMapping
    public List<DatPhong> all() {
        return DatPhong.findAll();
    }

    @GetMapping("/{maDP}")
    public ResponseEntity<DatPhong> one(@PathVariable("maDP") String maDP) {
        DatPhong dp = DatPhong.findById(maDP);
        return dp != null ? ResponseEntity.ok(dp) : ResponseEntity.notFound().build();
    }

    @GetMapping("/khach/{dinhDanh}")
    public List<DatPhong> byCustomer(@PathVariable String dinhDanh) {
        return DatPhong.findByKhach(dinhDanh);
    }

    // ===== TẠO BOOKING =====
    @PostMapping
    public ResponseEntity<?> create(@RequestBody DatPhong dp) {
        // tối thiểu các trường
        if (dp.getMaDatPhong() == null || dp.getDinhDanhKhach() == null || dp.getMaPhong() == null
                || dp.getNgayNhan() == null || dp.getNgayTra() == null) {
            return ResponseEntity.badRequest().body("Thiếu trường bắt buộc");
        }
        if (!dp.getNgayTra().isAfter(dp.getNgayNhan())) {
            return ResponseEntity.badRequest().body("ngayTra phải sau ngayNhan");
        }
        // kiểm tra phòng trống (DatPhong.insert cũng sẽ kiểm tra lần nữa)
        boolean trong = DatPhong.isPhongTrong(dp.getMaPhong(), dp.getNgayNhan(), dp.getNgayTra());
        if (!trong)
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Phòng không trống trong khoảng ngày");

        if (dp.getTrangThai() == null)
            dp.setTrangThai(BookingStatus.DA_DAT);

        boolean ok = DatPhong.insert(dp);
        return ok ? ResponseEntity.status(HttpStatus.CREATED).body(dp)
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Không thể tạo đặt phòng");
    }

    // ===== SỬA BOOKING =====
    @PutMapping("/{maDP}")
    public ResponseEntity<?> update(@PathVariable("maDP") String maDP, @RequestBody DatPhong dp) {
        DatPhong old = DatPhong.findById(maDP);
        if (old == null)
            return ResponseEntity.notFound().build();

        // khoá chính theo path
        dp.setMaDatPhong(maDP);
        if (dp.getNgayNhan() == null || dp.getNgayTra() == null || !dp.getNgayTra().isAfter(dp.getNgayNhan())) {
            return ResponseEntity.badRequest().body("ngayTra phải sau ngayNhan");
        }

        boolean ok = DatPhong.update(dp); // bên trong có kiểm tra phòng trống khi đổi ngày/phòng
        return ok ? ResponseEntity.ok(dp)
                : ResponseEntity.status(HttpStatus.CONFLICT).body("Phòng không trống hoặc lỗi cập nhật");
    }

    // ===== HỦY / CHECKIN / CHECKOUT =====
    @PostMapping("/{maDP}/huy")
    public ResponseEntity<?> huy(@PathVariable("maDP") String maDP) {
        boolean ok = DatPhong.huy(maDP);
        return ok ? ResponseEntity.ok().build() : ResponseEntity.badRequest().body("Không thể hủy");
    }

    @PostMapping("/{maDP}/checkin")
    public ResponseEntity<?> checkIn(@PathVariable("maDP") String maDP) {
        boolean ok = DatPhong.checkIn(maDP);
        return ok ? ResponseEntity.ok().build() : ResponseEntity.badRequest().body("Check-in thất bại");
    }

    @PostMapping("/{maDP}/checkout")
    public ResponseEntity<?> checkOut(@PathVariable("maDP") String maDP) {
        boolean ok = DatPhong.checkOut(maDP);
        return ok ? ResponseEntity.ok().build() : ResponseEntity.badRequest().body("Check-out thất bại");
    }

    @DeleteMapping("/{maDP}")
    public ResponseEntity<?> delete(@PathVariable String maDP) {
        boolean ok = DatPhong.deleteById(maDP); // cần có hàm này trong Model DatPhong
        return ok ? ResponseEntity.noContent().build()
                : ResponseEntity.badRequest().body("Không thể xoá đặt phòng");
    }

    // ===== DTO yêu cầu tìm phòng =====
    public record TimPhongRequest(LocalDate ngayNhan, LocalDate ngayTra, Integer soNguoi, String loaiPhong) {
    }
}
