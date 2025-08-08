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
        model.addAttribute("danhSachNhap", danhSachNhap);
        model.addAttribute("totalCount", nhapService.getTotalCount());
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
    
    @GetMapping("/edit/{index}")
    public String showEditForm(@PathVariable int index, Model model, RedirectAttributes redirectAttributes) {
        Nhap nhap = nhapService.getNhapByIndex(index);
        if (nhap == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy phiếu nhập!");
            return "redirect:/quanly/nhap";
        }
        
        model.addAttribute("nhap", nhap);
        model.addAttribute("index", index);
        model.addAttribute("title", "Sửa Phiếu Nhập");
        model.addAttribute("content", "nhap/form");
        model.addAttribute("action", "edit");
        return "layout";
    }
    
    @PostMapping("/edit/{index}")
    public String updateNhap(@PathVariable int index, @ModelAttribute Nhap nhap, 
                            RedirectAttributes redirectAttributes) {
        try {
            nhapService.updateNhap(index, nhap);
            redirectAttributes.addFlashAttribute("successMessage", 
                "Cập nhật phiếu nhập hàng hóa " + nhap.getHanghoaID() + " (" + nhap.getTenHang() + ") thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Có lỗi xảy ra khi cập nhật phiếu nhập: " + e.getMessage());
        }
    return "redirect:/quanly/nhap";
    }
    
    @PostMapping("/delete/{index}")
    public String deleteNhap(@PathVariable int index, RedirectAttributes redirectAttributes) {
        try {
            Nhap nhap = nhapService.getNhapByIndex(index);
            if (nhap != null) {
                nhapService.deleteNhap(index);
                redirectAttributes.addFlashAttribute("successMessage", 
                    "Xóa phiếu nhập hàng hóa " + nhap.getHanghoaID() + " (" + nhap.getTenHang() + ") thành công!");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy phiếu nhập!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Có lỗi xảy ra khi xóa phiếu nhập: " + e.getMessage());
        }
    return "redirect:/quanly/nhap";
    }
    
    @GetMapping("/search")
    public String showSearchForm(Model model) {
        model.addAttribute("title", "Tìm Kiếm Phiếu Nhập");
        model.addAttribute("content", "nhap/search");
        return "layout";
    }
    
    @PostMapping("/search")
    public String searchNhap(@RequestParam("keyword") String keyword, Model model) {
        List<Nhap> ketQuaTimKiem = nhapService.searchNhap(keyword);
        model.addAttribute("danhSachNhap", ketQuaTimKiem);
        model.addAttribute("keyword", keyword);
        model.addAttribute("totalCount", ketQuaTimKiem.size());
        model.addAttribute("title", "Kết Quả Tìm Kiếm");
        model.addAttribute("content", "nhap/search");
        return "layout";
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
        model.addAttribute("title", "Báo Cáo Nhập Hàng");
        model.addAttribute("content", "nhap/report");
        return "layout";
    }
}
