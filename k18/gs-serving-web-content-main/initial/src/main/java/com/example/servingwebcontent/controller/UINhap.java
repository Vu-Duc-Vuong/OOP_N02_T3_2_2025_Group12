package com.example.servingwebcontent.controller;

import com.example.servingwebcontent.model.Nhap;
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
 
    public UINhap() {
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
        
        return "UINhap";
    }
    
    @GetMapping("/list")
    public String listNhap(Model model) {
        return readList(model);
    }
}
