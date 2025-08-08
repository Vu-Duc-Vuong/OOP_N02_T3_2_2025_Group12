package com.example.servingwebcontent;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;
import java.time.LocalDate;
import com.example.servingwebcontent.model.Nhap;

@Controller
public class GreetingController {

	// Danh sách phiếu nhập đơn giản cho UI UINhap (khác module /quanly/nhap)
	private final List<Nhap> uiNhapList = new ArrayList<>();

	public GreetingController(){
		// dữ liệu mẫu ban đầu
		uiNhapList.add(new Nhap("HH010", "Bánh Quy", 10, 12000, LocalDate.now().minusDays(1)));
		uiNhapList.add(new Nhap("HH011", "Sữa Tươi", 5, 20000, LocalDate.now()));
	}

	@GetMapping("/greeting")
	public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
		model.addAttribute("name", name);
		return "greeting";
	}

	@GetMapping("/")
	public String home(Model model) {
		model.addAttribute("appName", "Quản Lý Cửa Hàng Tạp Hóa");
		return "index";
	}


	@GetMapping("/nhap")
	public String nhap(Model model) {
		model.addAttribute("appName", "Quản Lý Cửa Hàng Tạp Hóa");
		model.addAttribute("nhapList", uiNhapList);
		model.addAttribute("tongSoPhieu", uiNhapList.size());
		int tongSoLuong = uiNhapList.stream().mapToInt(Nhap::getSoLuongNhap).sum();
		model.addAttribute("tongSoLuong", tongSoLuong);
		return "UINhap";
	}

	// ĐÃ GỠ /nhap/add để tránh trùng với NhapStandaloneController (/nhap/add)
	// Nếu cần giao diện đơn giản riêng, đổi sang đường dẫn khác (ví dụ: /nhap/simple/add)

	// Báo cáo đơn giản theo ngày (hôm nay) cho UI
	@GetMapping("/nhap/report")
	public String reportNhap(Model model){
		LocalDate today = LocalDate.now();
		double tongTien = uiNhapList.stream()
				.filter(n -> n.getNgayNhap().equals(today))
				.mapToDouble(Nhap::getTongTien)
				.sum();
		model.addAttribute("targetDate", today);
		model.addAttribute("tongTienNhap", tongTien);
		model.addAttribute("nhapTrongNgay", uiNhapList);
		return "nhap/report"; // dùng template report hiện có (layout khác có thể giản lược)
	}

	// /ban route handled in controller.UIBan

}
