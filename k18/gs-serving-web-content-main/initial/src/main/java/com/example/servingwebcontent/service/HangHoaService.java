package com.example.servingwebcontent.service;

import com.example.servingwebcontent.model.HangHoa;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class HangHoaService {
    
    private Map<String, HangHoa> hangHoaMap = new HashMap<>();
    
    // Khởi tạo dữ liệu mẫu
    public HangHoaService() {
        initSampleData();
    }
    
    private void initSampleData() {
        HangHoa hh1 = new HangHoa("HH001", "Sữa Vinamilk", 50, "Vinamilk", 25000.0, 2024);
        HangHoa hh2 = new HangHoa("HH002", "Bánh mì Kinh Đô", 30, "Kinh Đô", 15000.0, 2024);
        HangHoa hh3 = new HangHoa("HH003", "Nước suối Lavie", 100, "Lavie", 8000.0, 2024);
        HangHoa hh4 = new HangHoa("HH004", "Mì tôm Hảo Hảo", 80, "Acecook", 6000.0, 2024);
        
        hangHoaMap.put(hh1.getMaHang(), hh1);
        hangHoaMap.put(hh2.getMaHang(), hh2);
        hangHoaMap.put(hh3.getMaHang(), hh3);
        hangHoaMap.put(hh4.getMaHang(), hh4);
    }
    
    // Lấy tất cả hàng hóa
    public List<HangHoa> getAllHangHoa() {
        return new ArrayList<>(hangHoaMap.values());
    }

    // Tìm theo tên (exact, bỏ qua hoa thường)
    public HangHoa findByTenExact(String ten) {
        if (ten == null) return null;
        return hangHoaMap.values().stream()
                .filter(h -> h.getTenHang() != null && h.getTenHang().equalsIgnoreCase(ten.trim()))
                .findFirst().orElse(null);
    }

    // Tăng / giảm số lượng (delta có thể âm). Trả về true nếu thành công.
    public boolean adjustSoLuong(String maHang, int delta) {
        HangHoa h = hangHoaMap.get(maHang);
        if (h == null) return false;
        int newQty = h.getSoLuong() + delta;
        if (newQty < 0) return false; // không cho âm
        h.setSoLuong(newQty);
        // nếu về 0 có thể giữ lại để tham chiếu, không xóa tự động
        hangHoaMap.put(maHang, h);
        return true;
    }

    // Thêm mới hoặc cộng dồn số lượng nếu mã đã tồn tại
    public void addOrIncrease(HangHoa hangHoa) {
        HangHoa existing = hangHoaMap.get(hangHoa.getMaHang());
        if (existing == null) {
            hangHoaMap.put(hangHoa.getMaHang(), hangHoa);
        } else {
            existing.setSoLuong(existing.getSoLuong() + hangHoa.getSoLuong());
            // cập nhật giá / năm SX nếu cần (giữ đơn giản: cập nhật nếu khác null)
            if (hangHoa.getDonGia() != null) existing.setDonGia(hangHoa.getDonGia());
            if (hangHoa.getNamSanXuat() != null) existing.setNamSanXuat(hangHoa.getNamSanXuat());
            hangHoaMap.put(existing.getMaHang(), existing);
        }
    }
    
    // Lấy hàng hóa theo mã
    public HangHoa getHangHoaById(String maHang) {
        return hangHoaMap.get(maHang);
    }
    
    // Thêm hàng hóa mới
    public boolean addHangHoa(HangHoa hangHoa) {
        if (hangHoaMap.containsKey(hangHoa.getMaHang())) {
            return false; // Mã hàng đã tồn tại
        }
        hangHoaMap.put(hangHoa.getMaHang(), hangHoa);
        return true;
    }
    
    // Cập nhật hàng hóa
    public boolean updateHangHoa(HangHoa hangHoa) {
        if (!hangHoaMap.containsKey(hangHoa.getMaHang())) {
            return false; // Hàng hóa không tồn tại
        }
        hangHoaMap.put(hangHoa.getMaHang(), hangHoa);
        return true;
    }
    
    // Xóa hàng hóa
    public boolean deleteHangHoa(String maHang) {
        if (!hangHoaMap.containsKey(maHang)) {
            return false; // Hàng hóa không tồn tại
        }
        hangHoaMap.remove(maHang);
        return true;
    }
    
    // Tìm kiếm hàng hóa theo tên
    public List<HangHoa> searchByTenHang(String tenHang) {
        return hangHoaMap.values().stream()
                .filter(hh -> hh.getTenHang().toLowerCase().contains(tenHang.toLowerCase()))
                .collect(Collectors.toList());
    }
    
    // Tìm kiếm hàng hóa theo nhà sản xuất
    public List<HangHoa> searchByNhaSanXuat(String nhaSanXuat) {
        return hangHoaMap.values().stream()
                .filter(hh -> hh.getNhaSanXuat().toLowerCase().contains(nhaSanXuat.toLowerCase()))
                .collect(Collectors.toList());
    }
    
    // Lọc hàng hóa theo số lượng tối thiểu
    public List<HangHoa> filterBySoLuongMin(int soLuongMin) {
        return hangHoaMap.values().stream()
                .filter(hh -> hh.getSoLuong() >= soLuongMin)
                .collect(Collectors.toList());
    }
    
    // Lọc hàng hóa theo khoảng giá
    public List<HangHoa> filterByGiaRange(double giaMin, double giaMax) {
        return hangHoaMap.values().stream()
                .filter(hh -> hh.getDonGia() >= giaMin && hh.getDonGia() <= giaMax)
                .collect(Collectors.toList());
    }
    
    // Tính tổng giá trị kho hàng
    public double tinhTongGiaTriKho() {
        return hangHoaMap.values().stream()
                .mapToDouble(HangHoa::thanhTien)
                .sum();
    }
    
    // Lấy top N hàng hóa có giá trị cao nhất
    public List<HangHoa> getTopHangHoaByGiaTri(int n) {
        return hangHoaMap.values().stream()
                .sorted((h1, h2) -> Double.compare(h2.thanhTien(), h1.thanhTien()))
                .limit(n)
                .collect(Collectors.toList());
    }
}
