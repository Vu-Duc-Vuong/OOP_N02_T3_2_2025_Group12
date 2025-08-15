package com.example.servingwebcontent.controller;

import com.example.servingwebcontent.model.KhachHang;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*") // cho phép gọi từ web client khác domain (tuỳ bạn)
@RestController
@RequestMapping("/api/khach")
public class KhachHangController {

    // Lấy tất cả khách hàng
    @GetMapping
    public List<KhachHang> getAll() {
        return KhachHang.findAll();
    }

    // Lấy 1 khách theo CCCD/Passport
    @GetMapping("/{dinhDanh}")
    public ResponseEntity<KhachHang> getOne(@PathVariable String dinhDanh) {
        KhachHang kh = KhachHang.findById(dinhDanh);
        return kh != null ? ResponseEntity.ok(kh) : ResponseEntity.notFound().build();
    }

    // Tìm kiếm theo tên/sđt/định danh: /api/khach/search?q=...
    @GetMapping("/search")
    public List<KhachHang> search(@RequestParam(name = "q", required = false) String q) {
        return KhachHang.search(q);
    }

    // Tạo mới khách hàng
    @PostMapping
    public ResponseEntity<?> create(@RequestBody KhachHang kh) {
        if (kh == null || isBlank(kh.getDinhDanh()) || isBlank(kh.getHoTen())) {
            return ResponseEntity.badRequest().body("Thiếu dinhDanh hoặc hoTen");
        }
        if (KhachHang.findById(kh.getDinhDanh()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Định danh đã tồn tại");
        }
        boolean ok = KhachHang.insert(kh);
        return ok ? ResponseEntity.status(HttpStatus.CREATED).body(kh)
                  : ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Không thể tạo khách hàng");
    }

    // Cập nhật thông tin khách hàng
    @PutMapping("/{dinhDanh}")
    public ResponseEntity<?> update(@PathVariable String dinhDanh, @RequestBody KhachHang kh) {
        KhachHang old = KhachHang.findById(dinhDanh);
        if (old == null) return ResponseEntity.notFound().build();

        // giữ khóa chính theo path
        kh.setDinhDanh(dinhDanh);

        boolean ok = KhachHang.update(kh);
        return ok ? ResponseEntity.ok(kh)
                  : ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Không thể cập nhật khách hàng");
    }

    // Xoá khách hàng
    @DeleteMapping("/{dinhDanh}")
    public ResponseEntity<?> delete(@PathVariable String dinhDanh) {
        // có thể thất bại nếu đang bị ràng buộc FK bởi dat_phong
        boolean ok = KhachHang.deleteById(dinhDanh);
        return ok ? ResponseEntity.noContent().build()
                  : ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Không thể xoá (có thể đang có đặt phòng)");
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}
