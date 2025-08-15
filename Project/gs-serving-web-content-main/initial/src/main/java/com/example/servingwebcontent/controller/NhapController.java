package com.example.servingwebcontent.controller;

import com.example.servingwebcontent.model.Nhap;
import com.example.servingwebcontent.service.NhapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/quanly/nhap")
public class NhapController {

    @Autowired
    private NhapService nhapService;

    @GetMapping
    public String listNhap(Model model) {
        List<Nhap> danhSachNhap = nhapService.getAllNhap();
        LocalDate today = LocalDate.now();
        List<Nhap> nhapToday = nhapService.getNhapByDate(today);
        int totalCountToday = nhapToday.size();
        double totalMoneyToday = nhapService.getTongTienNhapTheoNgay(today);
    double totalAllMoney = danhSachNhap.stream().mapToDouble(Nhap::getTongTien).sum();
        model.addAttribute("danhSachNhap", danhSachNhap);
        model.addAttribute("totalCountToday", totalCountToday);
        model.addAttribute("totalMoneyToday", String.format("%,.0f VNĐ", totalMoneyToday));
    model.addAttribute("totalAllMoney", totalAllMoney);
        model.addAttribute("title", "Danh Sách Nhập Hàng");
        model.addAttribute("content", "nhap/list");
        return "layout";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("nhap", new Nhap());
        model.addAttribute("title", "Thêm Phiếu Nhập");
        model.addAttribute("content", "nhap/form");
        model.addAttribute("action", "add");
        return "layout";
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
    return "redirect:/quanly/nhap";
    }

    @GetMapping("/report")
    public String showReport(@RequestParam(required = false) String date, Model model) {
        LocalDate targetDate = (date != null && !date.isEmpty()) ?
            LocalDate.parse(date) : LocalDate.now();

        List<Nhap> nhapTrongNgay = nhapService.getNhapByDate(targetDate);
        double tongTienNhap = nhapService.getTongTienNhapTheoNgay(targetDate);

        model.addAttribute("targetDate", targetDate);
        model.addAttribute("nhapTrongNgay", nhapTrongNgay);
        model.addAttribute("tongTienNhap", tongTienNhap);
    model.addAttribute("title", "Thống Kê Nhập Hàng");
        model.addAttribute("content", "nhap/report");
        return "layout";
    }

    // --- Sửa phiếu nhập ---
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Nhap nhap = nhapService.getNhapById(id);
        if (nhap == null) {
            model.addAttribute("errorMessage", "Không tìm thấy phiếu nhập!");
            return "redirect:/quanly/nhap";
        }
        model.addAttribute("nhap", nhap);
        model.addAttribute("title", "Sửa Phiếu Nhập");
        model.addAttribute("content", "nhap/form");
        model.addAttribute("action", "edit");
        return "layout";
    }

    @PostMapping("/edit/{id}")
    public String editNhap(@PathVariable Long id, @ModelAttribute Nhap nhap, RedirectAttributes redirectAttributes) {
        try {
            nhapService.updateNhapById(id, nhap);
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật phiếu nhập thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Có lỗi khi cập nhật: " + e.getMessage());
        }
        return "redirect:/quanly/nhap";
    }

    // --- Xóa phiếu nhập ---
    @GetMapping("/delete/{id}")
    public String deleteNhap(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            nhapService.deleteNhapById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Đã xóa phiếu nhập thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không xóa được: " + e.getMessage());
        }
        return "redirect:/quanly/nhap";
    }
}
