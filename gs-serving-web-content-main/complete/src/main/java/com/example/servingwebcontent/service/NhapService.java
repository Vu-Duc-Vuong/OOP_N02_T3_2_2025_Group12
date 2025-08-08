package com.example.servingwebcontent.service;

import com.example.servingwebcontent.model.Nhap;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NhapService {
    
    private List<Nhap> danhSachNhap = new ArrayList<>();
    
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
        if (nhap.getNgayNhap() == null) {
            nhap.setNgayNhap(LocalDate.now());
        }
        danhSachNhap.add(nhap);
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
