package com.example.servingwebcontent.controller;

import com.example.servingwebcontent.model.Ban;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/ban")
public class UIBan {
    
    private List<Ban> dsBan = new ArrayList<>();
 
    public UIBan() {
        Ban b1 = new Ban("P001", "Gạo ST25", "Khach Minh", 5, 50000.0);
        Ban b2 = new Ban("P002", "Nước Mắm", "Khach Dung", 3, 30000.0);
        Ban b3 = new Ban("P003", "Đường Cát", "Khach Duc", 8, 80000.0);
        
        dsBan.add(b1);
        dsBan.add(b2);
        dsBan.add(b3);
    }
    
    // Method readList() như yêu cầu của cô
    @GetMapping
    public String readList(Model model) {
        model.addAttribute("banList", dsBan);
        model.addAttribute("tongSoPhieu", dsBan.size());
        
        double tongDoanhThu = dsBan.stream()
                                   .mapToDouble(Ban::tongTien)
                                   .sum();
        model.addAttribute("tongDoanhThu", tongDoanhThu);
        
        return "UIBan";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("ban", new Ban());
        model.addAttribute("action", "add");
        return "ban/form"; // new template
    }

    @PostMapping("/add")
    public String addBan(@ModelAttribute Ban ban) {
        if (ban.getMaPhieu() != null && !ban.getMaPhieu().isEmpty()) {
            dsBan.add(ban);
        }
        return "redirect:/ban";
    }
    
    @GetMapping("/list")
    public String listBan(Model model) {
        return readList(model);
    }
}
