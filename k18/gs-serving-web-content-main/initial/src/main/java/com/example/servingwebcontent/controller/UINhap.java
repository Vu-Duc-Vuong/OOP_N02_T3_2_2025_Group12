package com.example.servingwebcontent.controller;

import com.example.servingwebcontent.model.Nhap;
import com.example.servingwebcontent.model.HangHoa;
import com.example.servingwebcontent.service.HangHoaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/uinhap")
public class UINhap {
    private List<Nhap> dsNhap = new ArrayList<>();
    private final HangHoaService hangHoaService;

    public UINhap(HangHoaService hangHoaService) {
        this.hangHoaService = hangHoaService;
        Nhap n1 = new Nhap("HH001", 100, 25000.0, LocalDate.now().minusDays(10));
        Nhap n2 = new Nhap("HH002", 80, 15000.0, LocalDate.now().minusDays(5));
        Nhap n3 = new Nhap("HH003", 120, 8000.0, LocalDate.now());

        dsNhap.add(n1);
        dsNhap.add(n2);
        dsNhap.add(n3);
    }

    @GetMapping
    public String readList(Model model) {
        model.addAttribute("nhapList", dsNhap);
        model.addAttribute("tongSoPhieu", dsNhap.size());

        int tongSoLuong = dsNhap.stream()
                                .mapToInt(Nhap::getSoLuongNhap)
                                .sum();
        model.addAttribute("tongSoLuong", tongSoLuong);

    double tongTienNhap = dsNhap.stream()
        .mapToDouble(Nhap::getTongTien)
        .sum();
    model.addAttribute("tongTienNhap", tongTienNhap);

        return "UINhap";
    }

    @GetMapping("/list")
    public String listNhap(Model model) {
        return readList(model);
    }

    @GetMapping("/add")
    public String addNhap(@org.springframework.web.bind.annotation.RequestParam String ma,
                          @org.springframework.web.bind.annotation.RequestParam String ten,
                          @org.springframework.web.bind.annotation.RequestParam(name="sl") int soLuong,
                          @org.springframework.web.bind.annotation.RequestParam(name="gia", required=false) Double gia,
                          Model model){
        if(soLuong <=0){
            model.addAttribute("error","Số lượng phải > 0");
            return readList(model);
        }
        Nhap phieu = new Nhap(ma, ten, soLuong, gia!=null? gia:0.0, LocalDate.now());
        dsNhap.add(phieu);
        HangHoa existing = hangHoaService.getHangHoaById(ma);
        if(existing == null){
            HangHoa newItem = new HangHoa(ma, ten, soLuong, "Chua ro", gia!=null? gia:0.0, LocalDate.now().getYear());
            hangHoaService.addOrIncrease(newItem);
            model.addAttribute("message","Đã thêm mới hàng hóa " + ten + " ("+ma+")");
        } else {
            existing.setSoLuong(existing.getSoLuong() + soLuong);
            if(gia!=null && gia>0) existing.setDonGia(gia);
            hangHoaService.updateHangHoa(existing);
            model.addAttribute("message","Đã cộng dồn số lượng cho " + ten + ": +"+soLuong);
        }
        return readList(model);
    }
}
