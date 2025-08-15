package com.example.servingwebcontent.controller;

import com.example.servingwebcontent.model.DichVu;
import com.example.servingwebcontent.model.ServiceType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/dich-vu")
public class DichVuController {

    // Danh sách tất cả dịch vụ
    @GetMapping
    public List<DichVu> all() {
        return DichVu.findAll();
    }

    // Lấy 1 dịch vụ theo mã
    @GetMapping("/{ma}")
    public ResponseEntity<DichVu> one(@PathVariable String ma) {
        DichVu dv = DichVu.findById(ma);
        return dv != null ? ResponseEntity.ok(dv) : ResponseEntity.notFound().build();
    }

    // Tạo mới dịch vụ
    @PostMapping
    public ResponseEntity<?> create(@RequestBody DichVu dv) {
        if (dv == null || isBlank(dv.getMaDichVu()) || isBlank(dv.getTenDichVu()))
            return ResponseEntity.badRequest().body("Thiếu maDichVu/tenDichVu");
        if (DichVu.findById(dv.getMaDichVu()) != null)
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Mã dịch vụ đã tồn tại");

        if (dv.getLoai() == null) dv.setLoai(ServiceType.THEO_LAN);
        boolean ok = DichVu.insert(dv);
        return ok ? ResponseEntity.status(HttpStatus.CREATED).body(dv)
                  : ResponseEntity.badRequest().body("Không thể tạo dịch vụ");
    }

    // Cập nhật dịch vụ
    @PutMapping("/{ma}")
    public ResponseEntity<?> update(@PathVariable String ma, @RequestBody DichVu dv) {
        DichVu old = DichVu.findById(ma);
        if (old == null) return ResponseEntity.notFound().build();

        // khoá theo path
        dv.setMaDichVu(ma);
        // nếu thiếu loai, giữ nguyên loại cũ
        if (dv.getLoai() == null) dv.setLoai(old.getLoai());

        boolean ok = DichVu.update(dv);
        return ok ? ResponseEntity.ok(dv)
                  : ResponseEntity.badRequest().body("Không thể cập nhật dịch vụ");
    }

    // Xoá dịch vụ
    @DeleteMapping("/{ma}")
    public ResponseEntity<?> delete(@PathVariable String ma) {
        boolean ok = DichVu.deleteById(ma);
        return ok ? ResponseEntity.noContent().build()
                  : ResponseEntity.badRequest().body("Không thể xoá dịch vụ (có thể đang được dùng)");
    }

    private static boolean isBlank(String s) { return s == null || s.trim().isEmpty(); }
}
