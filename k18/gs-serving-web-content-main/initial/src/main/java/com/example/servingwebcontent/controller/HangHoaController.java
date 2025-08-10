package com.example.servingwebcontent.controller;

import com.example.servingwebcontent.model.HangHoa;
import com.example.servingwebcontent.service.HangHoaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/hanghoa")
public class HangHoaController {

    @Autowired
    private HangHoaService hangHoaService;

    // Hiển thị danh sách hàng hóa
    @GetMapping
    public String listHangHoa(Model model) {
        model.addAttribute("hangHoaList", hangHoaService.getAllHangHoa());
        model.addAttribute("tongGiaTriKho", hangHoaService.tinhTongGiaTriKho());
        model.addAttribute("topHangHoa", hangHoaService.getTopHangHoaByGiaTri(3));
        return "hanghoa/list";
    }

    // Hiển thị form thêm hàng hóa
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("hangHoa", new HangHoa());
        model.addAttribute("action", "add");
        return "hanghoa/form";
    }

    // Xử lý thêm hàng hóa
    @PostMapping("/add")
    public String addHangHoa(@Valid @ModelAttribute HangHoa hangHoa, 
                            BindingResult bindingResult, 
                            RedirectAttributes redirectAttributes,
                            Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("action", "add");
            return "hanghoa/form";
        }

        if (hangHoaService.addHangHoa(hangHoa)) {
            redirectAttributes.addFlashAttribute("successMessage", 
                "Thêm hàng hóa thành công!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Mã hàng đã tồn tại!");
        }
        
        return "redirect:/hanghoa";
    }

    // Hiển thị form chỉnh sửa
    @GetMapping("/edit/{maHang}")
    public String showEditForm(@PathVariable String maHang, Model model, 
                              RedirectAttributes redirectAttributes) {
        HangHoa hangHoa = hangHoaService.getHangHoaById(maHang);
        if (hangHoa == null) {
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Không tìm thấy hàng hóa!");
            return "redirect:/hanghoa";
        }
        
        model.addAttribute("hangHoa", hangHoa);
        model.addAttribute("action", "edit");
        return "hanghoa/form";
    }

    // Xử lý cập nhật hàng hóa
    @PostMapping("/edit")
    public String editHangHoa(@Valid @ModelAttribute HangHoa hangHoa, 
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes,
                             Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("action", "edit");
            return "hanghoa/form";
        }

        if (hangHoaService.updateHangHoa(hangHoa)) {
            redirectAttributes.addFlashAttribute("successMessage", 
                "Cập nhật hàng hóa thành công!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Cập nhật thất bại!");
        }
        
        return "redirect:/hanghoa";
    }

    // Xóa hàng hóa
    @GetMapping("/delete/{maHang}")
    public String deleteHangHoa(@PathVariable String maHang, 
                               RedirectAttributes redirectAttributes) {
        if (hangHoaService.deleteHangHoa(maHang)) {
            redirectAttributes.addFlashAttribute("successMessage", 
                "Xóa hàng hóa thành công!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Xóa thất bại!");
        }
        
        return "redirect:/hanghoa";
    }

    // Hiển thị chi tiết hàng hóa
    @GetMapping("/detail/{maHang}")
    public String detailHangHoa(@PathVariable String maHang, Model model,
                               RedirectAttributes redirectAttributes) {
        HangHoa hangHoa = hangHoaService.getHangHoaById(maHang);
        if (hangHoa == null) {
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Không tìm thấy hàng hóa!");
            return "redirect:/hanghoa";
        }
        
        model.addAttribute("hangHoa", hangHoa);
        return "hanghoa/detail";
    }

    // Tìm kiếm hàng hóa
    @GetMapping("/search")
    public String searchHangHoa(@RequestParam(required = false) String tenHang,
                               @RequestParam(required = false) String nhaSanXuat,
                               @RequestParam(required = false) Integer soLuongMin,
                               @RequestParam(required = false) Double giaMin,
                               @RequestParam(required = false) Double giaMax,
                               Model model) {
        
        List<HangHoa> results = hangHoaService.getAllHangHoa();
        
        if (tenHang != null && !tenHang.isEmpty()) {
            results = hangHoaService.searchByTenHang(tenHang);
        } else if (nhaSanXuat != null && !nhaSanXuat.isEmpty()) {
            results = hangHoaService.searchByNhaSanXuat(nhaSanXuat);
        } else if (soLuongMin != null) {
            results = hangHoaService.filterBySoLuongMin(soLuongMin);
        } else if (giaMin != null && giaMax != null) {
            results = hangHoaService.filterByGiaRange(giaMin, giaMax);
        }
        
        model.addAttribute("hangHoaList", results);
        model.addAttribute("searchResult", true);
        model.addAttribute("searchCount", results.size());
        model.addAttribute("tenHang", tenHang);
        model.addAttribute("nhaSanXuat", nhaSanXuat);
        model.addAttribute("soLuongMin", soLuongMin);
        model.addAttribute("giaMin", giaMin);
        model.addAttribute("giaMax", giaMax);
        
        return "hanghoa/search";
    }
}
