package com.example.servingwebcontent.service;

import com.example.servingwebcontent.model.HangHoa;
import com.example.servingwebcontent.repository.HangHoaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class HangHoaService {

    private final HangHoaRepository repository;

    public HangHoaService(HangHoaRepository repository) {
        this.repository = repository;
        // Khởi tạo dữ liệu mẫu nếu bảng rỗng
        if (repository.count() == 0) {
            repository.saveAll(Arrays.asList(
                new HangHoa("HH001", "Sữa Vinamilk", 50, "Vinamilk", 25000.0, 2024),
                new HangHoa("HH002", "Bánh mì Kinh Đô", 30, "Kinh Đô", 15000.0, 2024),
                new HangHoa("HH003", "Nước suối Lavie", 100, "Lavie", 8000.0, 2024),
                new HangHoa("HH004", "Mì tôm Hảo Hảo", 80, "Acecook", 6000.0, 2024)
            ));
        }
    }
    
    // Lấy tất cả hàng hóa
    public List<HangHoa> getAllHangHoa() { return repository.findAll(); }

    // Tìm theo tên (exact, bỏ qua hoa thường)
    public HangHoa findByTenExact(String ten) {
        if (ten == null) return null;
        return repository.findAll().stream()
            .filter(h -> h.getTenHang() != null && h.getTenHang().equalsIgnoreCase(ten.trim()))
            .findFirst().orElse(null);
    }

    // Tăng / giảm số lượng (delta có thể âm). Trả về true nếu thành công.
    public boolean adjustSoLuong(String maHang, int delta) {
    HangHoa h = repository.findById(maHang).orElse(null);
        if (h == null) return false;
        int newQty = h.getSoLuong() + delta;
        if (newQty < 0) return false; // không cho âm
    h.setSoLuong(newQty);
    repository.save(h);
        return true;
    }

    // Thêm mới hoặc cộng dồn số lượng nếu mã đã tồn tại
    public void addOrIncrease(HangHoa hangHoa) {
    HangHoa existing = repository.findById(hangHoa.getMaHang()).orElse(null);
        if (existing == null) {
            repository.save(hangHoa);
        } else {
            existing.setSoLuong(existing.getSoLuong() + hangHoa.getSoLuong());
            // cập nhật giá / năm SX nếu cần (giữ đơn giản: cập nhật nếu khác null)
            if (hangHoa.getDonGia() != null) existing.setDonGia(hangHoa.getDonGia());
            if (hangHoa.getNamSanXuat() != null) existing.setNamSanXuat(hangHoa.getNamSanXuat());
            repository.save(existing);
        }
    }
    
    // Lấy hàng hóa theo mã
    public HangHoa getHangHoaById(String maHang) { return repository.findById(maHang).orElse(null); }
    
    // Thêm hàng hóa mới
    public boolean addHangHoa(HangHoa hangHoa) {
    if (repository.existsById(hangHoa.getMaHang())) {
            return false; // Mã hàng đã tồn tại
        }
    repository.save(hangHoa);
        return true;
    }
    
    // Cập nhật hàng hóa
    public boolean updateHangHoa(HangHoa hangHoa) {
    if (!repository.existsById(hangHoa.getMaHang())) {
            return false; // Hàng hóa không tồn tại
        }
    repository.save(hangHoa);
        return true;
    }
    
    // Xóa hàng hóa
    public boolean deleteHangHoa(String maHang) {
    if (!repository.existsById(maHang)) {
            return false; // Hàng hóa không tồn tại
        }
    repository.deleteById(maHang);
        return true;
    }
    
    // Tìm kiếm hàng hóa theo tên
    public List<HangHoa> searchByTenHang(String tenHang) {
    return repository.findAll().stream()
                .filter(hh -> hh.getTenHang().toLowerCase().contains(tenHang.toLowerCase()))
                .collect(Collectors.toList());
    }
    
    // Tìm kiếm hàng hóa theo nhà sản xuất
    public List<HangHoa> searchByNhaSanXuat(String nhaSanXuat) {
    return repository.findAll().stream()
                .filter(hh -> hh.getNhaSanXuat().toLowerCase().contains(nhaSanXuat.toLowerCase()))
                .collect(Collectors.toList());
    }
    
    // Lọc hàng hóa theo số lượng tối thiểu
    public List<HangHoa> filterBySoLuongMin(int soLuongMin) {
    return repository.findAll().stream()
                .filter(hh -> hh.getSoLuong() >= soLuongMin)
                .collect(Collectors.toList());
    }
    
    // Lọc hàng hóa theo khoảng giá
    public List<HangHoa> filterByGiaRange(double giaMin, double giaMax) {
    return repository.findAll().stream()
                .filter(hh -> hh.getDonGia() >= giaMin && hh.getDonGia() <= giaMax)
                .collect(Collectors.toList());
    }
    
    // Tính tổng giá trị kho hàng
    public double tinhTongGiaTriKho() {
    return repository.findAll().stream()
                .mapToDouble(HangHoa::thanhTien)
                .sum();
    }
    
    // Lấy top N hàng hóa có giá trị cao nhất
    public List<HangHoa> getTopHangHoaByGiaTri(int n) {
    return repository.findAll().stream()
                .sorted((h1, h2) -> Double.compare(h2.thanhTien(), h1.thanhTien()))
                .limit(n)
                .collect(Collectors.toList());
    }
}
