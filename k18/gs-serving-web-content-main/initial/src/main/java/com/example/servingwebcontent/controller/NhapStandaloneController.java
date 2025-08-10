package com.example.servingwebcontent.controller;

import com.example.servingwebcontent.model.Nhap;
import com.example.servingwebcontent.service.NhapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/nhap")
public class NhapStandaloneController {

    @Autowired
    private NhapService nhapService;

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("nhap", new Nhap());
        model.addAttribute("action", "add");
        return "nhap/fullform"; // template full page
    }

    @PostMapping("/add")
    public String addNhap(@ModelAttribute Nhap nhap, RedirectAttributes redirectAttributes) {
        try {
            nhapService.addNhap(nhap);
            redirectAttributes.addFlashAttribute("successMessage", 
                "Thêm phiếu nhập hàng hóa " + nhap.getHanghoaID() + " (" + nhap.getTenHang() + ") thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Có lỗi xảy ra khi thêm phiếu nhập: " + e.getMessage());
        }
        return "redirect:/quanly/nhap"; // quay về danh sách quản lý
    }
}
