package com.example.servingwebcontent.controller;

import com.example.servingwebcontent.model.HangHoa;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/uihanghoa")
public class UIHangHoa {
    
    private List<HangHoa> dsHangHoa = new ArrayList<>();
    
    // Khởi tạo dữ liệu mẫu như trong bảng
    public UIHangHoa() {
        HangHoa h1 = new HangHoa("H001", "Sữa Vinamilk", 50, "Vinamilk", 25000.0, 2024);
        HangHoa h2 = new HangHoa("H002", "Bánh mì Kinh Đô", 30, "Kinh Đô", 15000.0, 2024);
        HangHoa h3 = new HangHoa("H003", "Nước suối Lavie", 100, "Lavie", 8000.0, 2024);
        
        dsHangHoa.add(h1);
        dsHangHoa.add(h2);
        dsHangHoa.add(h3);
    }
    
    // Method readList() như yêu cầu của cô
    @GetMapping
    public String readList(Model model) {
        model.addAttribute("hangHoaList", dsHangHoa);
        model.addAttribute("tongSoHang", dsHangHoa.size());
        
        double tongGiaTri = dsHangHoa.stream()
                                    .mapToDouble(h -> h.getSoLuong() * h.getDonGia())
                                    .sum();
        model.addAttribute("tongGiaTri", tongGiaTri);
        
        return "UIHangHoa";
    }
    
    @GetMapping("/list")
    public String listHangHoa(Model model) {
        return readList(model);
    }
}
