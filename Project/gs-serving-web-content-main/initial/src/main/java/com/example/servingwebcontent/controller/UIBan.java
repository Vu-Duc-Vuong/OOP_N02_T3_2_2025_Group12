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
import com.example.servingwebcontent.util.CodeGenerator;
import com.example.servingwebcontent.model.StatItem;

@Controller
@RequestMapping("/ban")
public class UIBan {
    @GetMapping("/edit/{index}")
    public String showEditForm(@PathVariable int index, Model model) {
        if (index < 0 || index >= dsBan.size()) {
            model.addAttribute("error", "Không tìm thấy phiếu bán!");
            return readList(model);
        }
        model.addAttribute("ban", dsBan.get(index));
        model.addAttribute("action", "edit");
        model.addAttribute("index", index);
        return "ban/form";
    }

    @PostMapping("/edit/{index}")
    public String editBan(@PathVariable int index, @ModelAttribute Ban ban, Model model) {
        if (index < 0 || index >= dsBan.size()) {
            model.addAttribute("error", "Không tìm thấy phiếu bán!");
            return readList(model);
        }
        Ban old = dsBan.get(index);
        // Không cho đổi mã phiếu
        ban.setMaPhieu(old.getMaPhieu());
        dsBan.set(index, ban);
        return "redirect:/ban";
    }

    @GetMapping("/delete/{index}")
    public String deleteBan(@PathVariable int index, Model model) {
        if (index >= 0 && index < dsBan.size()) {
            dsBan.remove(index);
        }
        return "redirect:/ban";
    }

    private List<Ban> dsBan = new ArrayList<>();

    private final HangHoaService hangHoaService;

    public UIBan(HangHoaService hangHoaService) {
    this.hangHoaService = hangHoaService;
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
        if (ban.getMaPhieu() == null || ban.getMaPhieu().isBlank()) {
            ban.setMaPhieu(CodeGenerator.nextBan());
        }
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
                // Giảm số lượng; nếu về 0 sẽ bị xóa bởi service
                hangHoaService.adjustSoLuong(target.getMaHang(), -ban.getSoLuong());
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
            StatItem item = map.computeIfAbsent(code, c -> new StatItem(c, name));
            item.add(b.getSoLuong(), b.tongTien());
            item.setThoiGian(b.getThoiGianBan());
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
