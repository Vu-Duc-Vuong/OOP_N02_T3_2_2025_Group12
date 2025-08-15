package com.example.servingwebcontent.controller;

import com.example.servingwebcontent.model.PhongKhachSan;
import com.example.servingwebcontent.model.RoomStatus;
import com.example.servingwebcontent.model.RoomType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/phong")
public class PhongKhachSanController {

    // ===== 1) Danh sách tất cả phòng =====
    @GetMapping
    public List<PhongKhachSan> all() {
        return PhongKhachSan.findAll();
    }

    // ===== 2) Lấy 1 phòng theo mã =====
    @GetMapping("/{maPhong}")
    public ResponseEntity<PhongKhachSan> one(@PathVariable String maPhong) {
        PhongKhachSan p = PhongKhachSan.findById(maPhong);
        return p != null ? ResponseEntity.ok(p) : ResponseEntity.notFound().build();
    }

    // ===== 3) Tạo phòng =====
    @PostMapping
    public ResponseEntity<?> create(@RequestBody PhongKhachSan p) {
        if (p == null || isBlank(p.getMaPhong()))
            return ResponseEntity.badRequest().body("Thiếu maPhong");
        if (p.getLoaiPhong() == null)
            return ResponseEntity.badRequest().body("Thiếu loaiPhong (DON/DOI/SUITE/...)");
        if (p.getGiaMoiDem() < 0)
            return ResponseEntity.badRequest().body("giaMoiDem phải >= 0");
        if (p.getSoNguoiToiDa() <= 0)
            return ResponseEntity.badRequest().body("soNguoiToiDa phải > 0");

        if (PhongKhachSan.findById(p.getMaPhong()) != null)
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Mã phòng đã tồn tại");

        if (p.getTinhTrang() == null) p.setTinhTrang(RoomStatus.TRONG);

        boolean ok = PhongKhachSan.insert(p);
        return ok ? ResponseEntity.status(HttpStatus.CREATED).body(p)
                  : ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Không thể tạo phòng");
    }

    // ===== 4) Cập nhật thông tin phòng =====
    @PutMapping("/{maPhong}")
    public ResponseEntity<?> update(@PathVariable String maPhong, @RequestBody PhongKhachSan p) {
        PhongKhachSan old = PhongKhachSan.findById(maPhong);
        if (old == null) return ResponseEntity.notFound().build();

        // khoá theo path
        p.setMaPhong(maPhong);

        // giữ enum nếu client không gửi
        if (p.getLoaiPhong() == null) p.setLoaiPhong(old.getLoaiPhong());
        if (p.getTinhTrang() == null) p.setTinhTrang(old.getTinhTrang());

        // các ràng buộc cơ bản
        if (p.getGiaMoiDem() < 0)  return ResponseEntity.badRequest().body("giaMoiDem phải >= 0");
        if (p.getSoNguoiToiDa() <= 0) return ResponseEntity.badRequest().body("soNguoiToiDa phải > 0");

        boolean ok = PhongKhachSan.update(p);
        return ok ? ResponseEntity.ok(p)
                  : ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Không thể cập nhật phòng");
    }

    // ===== 5) Đổi trạng thái phòng (TRONG/DA_DAT/DANG_O/BAO_TRI) =====
    @PatchMapping("/{maPhong}/status/{status}")
    public ResponseEntity<?> updateStatus(@PathVariable String maPhong, @PathVariable String status) {
        RoomStatus st;
        try {
            st = RoomStatus.valueOf(status);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Trạng thái không hợp lệ");
        }
        boolean ok = PhongKhachSan.updateStatus(maPhong, st);
        return ok ? ResponseEntity.ok().build()
                  : ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Không thể đổi trạng thái");
    }

    // ===== 6) Xoá phòng =====
    @DeleteMapping("/{maPhong}")
    public ResponseEntity<?> delete(@PathVariable String maPhong) {
        boolean ok = PhongKhachSan.deleteById(maPhong);
        return ok ? ResponseEntity.noContent().build()
                  : ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Không thể xoá (có thể đang được dùng)");
    }

    // ===== 7) Tìm phòng trống theo khoảng ngày / sức chứa / loại phòng =====
    // GET /api/phong/tim-trong?nhan=2025-08-20&tra=2025-08-22&soNguoi=2&loai=DON
    @GetMapping("/tim-trong")
    public ResponseEntity<?> timTrong(
            @RequestParam("nhan") String nhanStr,
            @RequestParam("tra")  String traStr,
            @RequestParam(value = "soNguoi", required = false) Integer soNguoi,
            @RequestParam(value = "loai", required = false) String loaiStr
    ) {
        LocalDate nhan, tra;
        try {
            nhan = LocalDate.parse(nhanStr);
            tra  = LocalDate.parse(traStr);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Định dạng ngày phải là yyyy-MM-dd");
        }
        if (!tra.isAfter(nhan)) {
            return ResponseEntity.badRequest().body("ngayTra phải sau ngayNhan");
        }

        RoomType loai = null;
        if (loaiStr != null && !loaiStr.isBlank()) {
            try { loai = RoomType.valueOf(loaiStr); }
            catch (Exception e) { return ResponseEntity.badRequest().body("loai không hợp lệ"); }
        }

        List<PhongKhachSan> list = PhongKhachSan.timPhongTrong(nhan, tra, soNguoi, loai);
        return ResponseEntity.ok(list);
    }

    private static boolean isBlank(String s) { return s == null || s.trim().isEmpty(); }
}
