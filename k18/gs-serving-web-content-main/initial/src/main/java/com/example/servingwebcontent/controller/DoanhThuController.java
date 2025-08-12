package com.example.servingwebcontent.controller;

import com.example.servingwebcontent.model.Ban;
import com.example.servingwebcontent.model.Nhap;
import com.example.servingwebcontent.service.NhapService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

@Controller
@RequestMapping("/doanhthu")
public class DoanhThuController {

    private final NhapService nhapService;
    private final UIBan uiBan; // dùng danh sách bán hiện có

    public DoanhThuController(NhapService nhapService, UIBan uiBan){
        this.nhapService = nhapService;
        this.uiBan = uiBan;
    }

    @GetMapping
    public String viewToday(@RequestParam(required = false) String from,
                            @RequestParam(required = false) String to,
                            Model model){
        // Hàm parse an toàn: trả null nếu chuỗi rỗng hoặc lỗi định dạng
        java.util.function.Function<String, LocalDateTime> safeParse = (s) -> {
            if(s == null) return null;
            s = s.trim();
            if(s.isEmpty()) return null;
            try { return LocalDateTime.parse(s); } catch (DateTimeParseException e){ return null; }
        };

        LocalDateTime parsedFrom = safeParse.apply(from);
        LocalDateTime parsedTo   = safeParse.apply(to);

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime calcEnd = parsedTo != null ? parsedTo : now; // nếu không có to dùng hiện tại
        LocalDateTime calcStart = parsedFrom != null ? parsedFrom : calcEnd.minusHours(24); // nếu không có from lùi 24h

        // Nếu start sau end thì hoán đổi hoặc ép về 24h trước end
        if(calcStart.isAfter(calcEnd)){
            if(parsedFrom != null && parsedTo != null){
                LocalDateTime tmp = calcStart; calcStart = calcEnd; calcEnd = tmp;
            } else {
                calcStart = calcEnd.minusHours(24);
            }
        }

        final LocalDateTime start = calcStart; // hiệu lực final cho lambda
        final LocalDateTime end = calcEnd;

        // Tổng nhập trong khoảng (lọc theo ngày + thời gian)
    double tongNhap = nhapService.getAllNhap().stream()
        .filter(n -> n.getThoiGianNhap()!=null && !n.getThoiGianNhap().isBefore(start) && !n.getThoiGianNhap().isAfter(end))
        .mapToDouble(Nhap::getTongTien)
        .sum();

    double tongBan = uiBan.getDsBan().stream()
        .filter(b -> b.getThoiGianBan()!=null && !b.getThoiGianBan().isBefore(start) && !b.getThoiGianBan().isAfter(end))
        .mapToDouble(Ban::tongTien)
        .sum();

        double laiLo = tongBan - tongNhap;
        String trangThai = laiLo > 0 ? "LÃI" : (laiLo < 0 ? "LỖ" : "HÒA VỐN");
        model.addAttribute("tongNhap", tongNhap);
        model.addAttribute("tongBan", tongBan);
        model.addAttribute("laiLo", laiLo);
        model.addAttribute("trangThai", trangThai);
        model.addAttribute("from", start);
        model.addAttribute("to", end);
    model.addAttribute("title", "Doanh Thu 24h");
        model.addAttribute("content", "doanhthu");
        return "layout";
    }
}
