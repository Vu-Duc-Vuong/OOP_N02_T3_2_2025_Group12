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

	@GetMapping
	public String listHangHoa(Model model) {
		model.addAttribute("hangHoaList", hangHoaService.getAllHangHoa());
		model.addAttribute("tongGiaTriKho", hangHoaService.tinhTongGiaTriKho());
		model.addAttribute("topHangHoa", hangHoaService.getTopHangHoaByGiaTri(3));
		return "hanghoa/list";
	}

	@GetMapping("/add")
	public String showAddForm(Model model){
		model.addAttribute("hangHoa", new HangHoa());
		model.addAttribute("action","add");
		return "hanghoa/form";
	}

	@PostMapping("/add")
	public String addHangHoa(@Valid @ModelAttribute HangHoa hangHoa,
							 BindingResult bindingResult,
							 RedirectAttributes redirectAttributes,
							 Model model){
		if(bindingResult.hasErrors()){
			model.addAttribute("action","add");
			return "hanghoa/form";
		}
		hangHoaService.addHangHoa(hangHoa);
		redirectAttributes.addFlashAttribute("successMessage","Thêm hàng hóa thành công!");
		return "redirect:/hanghoa";
	}

	@GetMapping("/edit/{maHang}")
	public String showEditForm(@PathVariable String maHang, Model model, RedirectAttributes redirectAttributes){
		HangHoa hh = hangHoaService.getHangHoaById(maHang);
		if(hh==null){
			redirectAttributes.addFlashAttribute("errorMessage","Không tìm thấy hàng hóa!");
			return "redirect:/hanghoa";
		}
		model.addAttribute("hangHoa", hh);
		model.addAttribute("action","edit");
		return "hanghoa/form";
	}

	@PostMapping("/edit")
	public String editHangHoa(@Valid @ModelAttribute HangHoa hangHoa,
							  BindingResult bindingResult,
							  RedirectAttributes redirectAttributes,
							  Model model){
		if(bindingResult.hasErrors()){
			model.addAttribute("action","edit");
			return "hanghoa/form";
		}
		if(hangHoaService.updateHangHoa(hangHoa)){
			redirectAttributes.addFlashAttribute("successMessage","Cập nhật hàng hóa thành công!");
		} else {
			redirectAttributes.addFlashAttribute("errorMessage","Cập nhật thất bại!");
		}
		return "redirect:/hanghoa";
	}

	@PostMapping("/delete/{maHang}")
	public String deleteHangHoa(@PathVariable String maHang, RedirectAttributes redirectAttributes){
		HangHoa hh = hangHoaService.getHangHoaById(maHang);
		if(hh==null){
			redirectAttributes.addFlashAttribute("errorMessage","Không tìm thấy hàng hóa!");
		} else {
			hangHoaService.deleteHangHoa(maHang);
			redirectAttributes.addFlashAttribute("successMessage","Đã xóa hàng hóa " + maHang + "!");
		}
		return "redirect:/hanghoa";
	}

	// Thêm GET mapping để hỗ trợ link <a> hiện tại trong giao diện (tránh lỗi 405).
	// Khuyến nghị lâu dài: đổi sang form POST/DELETE để đúng chuẩn REST.
	@GetMapping("/delete/{maHang}")
	public String deleteHangHoaGet(@PathVariable String maHang, RedirectAttributes redirectAttributes){
		return deleteHangHoa(maHang, redirectAttributes);
	}
}
