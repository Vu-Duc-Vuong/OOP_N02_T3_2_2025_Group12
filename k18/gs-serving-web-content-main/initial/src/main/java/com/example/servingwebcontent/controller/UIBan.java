package com.example.servingwebcontent.controller;

import com.example.servingwebcontent.model.Ban;
import com.example.servingwebcontent.service.HangHoaService;
import com.example.servingwebcontent.model.HangHoa;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.time.LocalDate;
import com.example.servingwebcontent.model.StatItem;

@Controller
@RequestMapping("/ban")
public class UIBan {

    private List<Ban> dsBan = new ArrayList<>();

    private final HangHoaService hangHoaService;

    public UIBan(HangHoaService hangHoaService) {
        this.hangHoaService = hangHoaService;
        Ban b1 = new Ban("P001", "Gạo ST25", "Khach Minh", 5, 50000.0);
        Ban b2 = new Ban("P002", "Nước Mắm", "Khach Dung", 3, 30000.0);
        Ban b3 = new Ban("P003", "Đường Cát", "Khach Duc", 8, 80000.0);

        dsBan.add(b1);
        dsBan.add(b2);
        dsBan.add(b3);
    }

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
        return "ban/form";
    }

    @PostMapping("/add")
    public String addBan(@ModelAttribute Ban ban, Model model) {
        if (ban.getMaPhieu() == null || ban.getMaPhieu().isEmpty()) return "redirect:/ban";
        if (ban.getSoLuong() <=0){
            model.addAttribute("error","Số lượng phải >0");
            return readList(model);
        }
        if(ban.getTenHang()!=null){
            HangHoa target = hangHoaService.getAllHangHoa().stream()
                .filter(h -> h.getTenHang().equalsIgnoreCase(ban.getTenHang()))
                .findFirst().orElse(null);
            if(target != null){
                if(target.getSoLuong() < ban.getSoLuong()){
                    model.addAttribute("error","Kho chỉ còn " + target.getSoLuong() + " - không đủ để bán " + ban.getSoLuong());
                    return readList(model);
                }
                target.setSoLuong(target.getSoLuong() - ban.getSoLuong());
                hangHoaService.updateHangHoa(target);
                if(ban.getDonGia() <= 0 && target.getDonGia()!=null){
                    ban.setDonGia(target.getDonGia());
                }
            }
        }
        dsBan.add(ban);
        return "redirect:/ban";
    }

    @GetMapping("/list")
    public String listBan(Model model) {
        return readList(model);
    }

    @GetMapping("/stats")
    public String stats(Model model){
        Map<String, StatItem> map = new LinkedHashMap<>();
        for(Ban b: dsBan){
            String code = b.getMaPhieu();
            String name = b.getTenHang()!=null? b.getTenHang(): code;
            map.computeIfAbsent(code, c -> new StatItem(c, name))
                .add(b.getSoLuong(), b.tongTien());
        }
        List<StatItem> items = new ArrayList<>(map.values());
        double totalValue = items.stream().mapToDouble(StatItem::getTotalValue).sum();
        int totalQty = items.stream().mapToInt(StatItem::getQuantity).sum();
        model.addAttribute("items", items);
        model.addAttribute("totalValue", totalValue);
        model.addAttribute("totalQty", totalQty);
        return "statsBan";
    }

    public List<Ban> getDsBan(){
        return dsBan;
    }
}
