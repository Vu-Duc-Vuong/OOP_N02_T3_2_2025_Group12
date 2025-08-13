package com.example.servingwebcontent.service;

import com.example.servingwebcontent.model.HangHoa;
import com.example.servingwebcontent.repository.HangHoaRepository;
import org.springframework.stereotype.Service;
import com.example.servingwebcontent.util.CodeGenerator;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class HangHoaService {

    private final HangHoaRepository repository;

    public HangHoaService(HangHoaRepository repository) {
        this.repository = repository;
        if (repository.count() == 0) {
            repository.saveAll(Arrays.asList(
                new HangHoa("HH001", "Sữa Vinamilk", 50, "Vinamilk", 25000.0, 2024),
                new HangHoa("HH002", "Bánh mì Kinh Đô", 30, "Kinh Đô", 15000.0, 2024),
                new HangHoa("HH003", "Nước suối Lavie", 100, "Lavie", 8000.0, 2024),
                new HangHoa("HH004", "Mì tôm Hảo Hảo", 80, "Acecook", 6000.0, 2024)
            ));
        }
    }

    public List<HangHoa> getAllHangHoa() { return repository.findAll(); }

    public HangHoa findByTenExact(String ten) {
        if (ten == null) return null;
        return repository.findAll().stream()
            .filter(h -> h.getTenHang() != null && h.getTenHang().equalsIgnoreCase(ten.trim()))
            .findFirst().orElse(null);
    }

    public boolean adjustSoLuong(String maHang, int delta) {
    HangHoa h = repository.findById(maHang).orElse(null);
        if (h == null) return false;
        int newQty = h.getSoLuong() + delta;
        if (newQty < 0) return false;
        if (newQty == 0) {
            // Bán hết / xuất hết: xóa hàng khỏi kho để tránh vi phạm @Positive
            repository.delete(h);
            return true;
        }
        h.setSoLuong(newQty);
        repository.save(h);
        return true;
    }

    public void addOrIncrease(HangHoa hangHoa) {
    HangHoa existing = repository.findById(hangHoa.getMaHang()).orElse(null);
        if (existing == null) {
            repository.save(hangHoa);
        } else {
            existing.setSoLuong(existing.getSoLuong() + hangHoa.getSoLuong());
            if (hangHoa.getDonGia() != null) existing.setDonGia(hangHoa.getDonGia());
            if (hangHoa.getNamSanXuat() != null) existing.setNamSanXuat(hangHoa.getNamSanXuat());
            repository.save(existing);
        }
    }

    public HangHoa getHangHoaById(String maHang) { return repository.findById(maHang).orElse(null); }

    public boolean addHangHoa(HangHoa hangHoa) {
        if (hangHoa.getMaHang()==null || hangHoa.getMaHang().isBlank()) {
            // Sinh mã mới dạng HHX
            String code;
            do { code = CodeGenerator.nextHangHoa(); } while (repository.existsById(code));
            hangHoa.setMaHang(code);
        }
        if (repository.existsById(hangHoa.getMaHang())) {
            return false;
        }
        repository.save(hangHoa);
        return true;
    }

    public boolean updateHangHoa(HangHoa hangHoa) {
    if (!repository.existsById(hangHoa.getMaHang())) {
            return false;
        }
    repository.save(hangHoa);
        return true;
    }

    public boolean deleteHangHoa(String maHang) {
    if (!repository.existsById(maHang)) {
            return false;
        }
    repository.deleteById(maHang);
        return true;
    }

    public List<HangHoa> searchByTenHang(String tenHang) {
    return repository.findAll().stream()
                .filter(hh -> hh.getTenHang().toLowerCase().contains(tenHang.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<HangHoa> searchByNhaSanXuat(String nhaSanXuat) {
    return repository.findAll().stream()
                .filter(hh -> hh.getNhaSanXuat().toLowerCase().contains(nhaSanXuat.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<HangHoa> filterBySoLuongMin(int soLuongMin) {
    return repository.findAll().stream()
                .filter(hh -> hh.getSoLuong() >= soLuongMin)
                .collect(Collectors.toList());
    }

    public List<HangHoa> filterByGiaRange(double giaMin, double giaMax) {
    return repository.findAll().stream()
                .filter(hh -> hh.getDonGia() >= giaMin && hh.getDonGia() <= giaMax)
                .collect(Collectors.toList());
    }

    public double tinhTongGiaTriKho() {
    return repository.findAll().stream()
                .mapToDouble(HangHoa::thanhTien)
                .sum();
    }

    public List<HangHoa> getTopHangHoaByGiaTri(int n) {
    return repository.findAll().stream()
                .sorted((h1, h2) -> Double.compare(h2.thanhTien(), h1.thanhTien()))
                .limit(n)
                .collect(Collectors.toList());
    }
}
