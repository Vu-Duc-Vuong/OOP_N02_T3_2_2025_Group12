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
import com.example.servingwebcontent.model.StatItem;

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
	public String nhapRedirect(){
		// Hợp nhất về module /quanly/nhap
		return "redirect:/quanly/nhap";
	}

	@GetMapping("/nhap/stats")
	public String nhapStats(Model model){
		Map<String, StatItem> map = new LinkedHashMap<>();
		for(Nhap n: uiNhapList){
			String code = n.getHanghoaID();
			map.computeIfAbsent(code, c -> new StatItem(c, n.getTenHang()!=null? n.getTenHang():c))
				.add(n.getSoLuongNhap(), n.getTongTien());
		}
		List<StatItem> items = new ArrayList<>(map.values());
		double totalValue = items.stream().mapToDouble(StatItem::getTotalValue).sum();
		int totalQty = items.stream().mapToInt(StatItem::getQuantity).sum();
		model.addAttribute("items", items);
		model.addAttribute("totalValue", totalValue);
		model.addAttribute("totalQty", totalQty);
		return "statsNhap";
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
