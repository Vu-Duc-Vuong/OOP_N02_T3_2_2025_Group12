package com.example.servingwebcontent.service;

import com.example.servingwebcontent.model.Nhap;
import com.example.servingwebcontent.model.HangHoa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NhapService {
    
    private List<Nhap> danhSachNhap = new ArrayList<>();

    @Autowired
    private HangHoaService hangHoaService; // đồng bộ kho hàng
    
    public NhapService() {
        // Dữ liệu mẫu
    danhSachNhap.add(new Nhap("HH001", "Gạo ST25", 100, 15000, LocalDate.now().minusDays(1)));
    danhSachNhap.add(new Nhap("HH002", "Nước Mắm", 50, 25000, LocalDate.now()));
    danhSachNhap.add(new Nhap("HH003", "Đường Cát", 200, 8000, LocalDate.now().minusDays(2)));
    }
    
    public List<Nhap> getAllNhap() {
        return new ArrayList<>(danhSachNhap);
    }
    
    public void addNhap(Nhap nhap) {
        // Kiểm tra dữ liệu đầu vào
        if (nhap.getHanghoaID() == null || nhap.getHanghoaID().isEmpty()) {
            throw new IllegalArgumentException("Mã hàng hóa không được để trống!");
        }
        if (nhap.getTenHang() == null || nhap.getTenHang().isEmpty()) {
            throw new IllegalArgumentException("Tên hàng hóa không được để trống!");
        }
        if (nhap.getSoLuongNhap() <= 0) {
            throw new IllegalArgumentException("Số lượng nhập phải lớn hơn 0!");
        }
        if (nhap.getGiaNhap() < 0) {
            throw new IllegalArgumentException("Giá nhập phải >= 0!");
        }
        if (nhap.getNgayNhap() == null) {
            nhap.setNgayNhap(LocalDate.now());
        }
        danhSachNhap.add(nhap);
        // Cập nhật / thêm mới hàng hóa vào kho
        if(hangHoaService != null){
            HangHoa existing = hangHoaService.getHangHoaById(nhap.getHanghoaID());
            if(existing == null){
                // tạo hàng mới với thông tin tối thiểu
                HangHoa hh = new HangHoa(
                    nhap.getHanghoaID(),
                    nhap.getTenHang()!=null? nhap.getTenHang(): nhap.getHanghoaID(),
                    nhap.getSoLuongNhap(),
                    "Nhập Kho", // nhà SX mặc định
                    nhap.getGiaNhap(),
                    LocalDate.now().getYear()
                );
                hangHoaService.addHangHoa(hh);
            } else {
                existing.setSoLuong(existing.getSoLuong() + nhap.getSoLuongNhap());
                if(nhap.getTenHang()!=null) existing.setTenHang(nhap.getTenHang());
                if(nhap.getGiaNhap()>0) existing.setDonGia(nhap.getGiaNhap());
                hangHoaService.updateHangHoa(existing);
            }
        }
    }
    
    public void updateNhap(int index, Nhap nhap) {
        if (index >= 0 && index < danhSachNhap.size()) {
            danhSachNhap.set(index, nhap);
        }
    }
    
    public void deleteNhap(int index) {
        if (index >= 0 && index < danhSachNhap.size()) {
            danhSachNhap.remove(index);
        }
    }
    
    public Nhap getNhapByIndex(int index) {
        if (index >= 0 && index < danhSachNhap.size()) {
            return danhSachNhap.get(index);
        }
        return null;
    }
    
    public List<Nhap> searchNhap(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllNhap();
        }
        
        String keywordLower = keyword.toLowerCase();
        return danhSachNhap.stream()
                .filter(nhap -> nhap.getHanghoaID().toLowerCase().contains(keywordLower))
                .collect(Collectors.toList());
    }
    
    public List<Nhap> getNhapByDate(LocalDate date) {
        return danhSachNhap.stream()
                .filter(nhap -> nhap.getNgayNhap().equals(date))
                .collect(Collectors.toList());
    }
    
    public double getTongTienNhapTheoNgay(LocalDate date) {
        return danhSachNhap.stream()
                .filter(nhap -> nhap.getNgayNhap().equals(date))
                .mapToDouble(Nhap::getTongTien)
                .sum();
    }
    
    public int getTotalCount() {
        return danhSachNhap.size();
    }
}
