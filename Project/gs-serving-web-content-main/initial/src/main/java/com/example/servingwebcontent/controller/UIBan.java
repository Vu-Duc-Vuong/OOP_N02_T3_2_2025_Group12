package com.example.servingwebcontent.controller;

import com.example.servingwebcontent.model.Ban;
import com.example.servingwebcontent.model.BanEntity;
import com.example.servingwebcontent.service.BanService;
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
    private final BanService banService;
    public UIBan(HangHoaService hangHoaService, BanService banService) {
        this.hangHoaService = hangHoaService;
        this.banService = banService;
    }

    @GetMapping("/edit/{maPhieu}")
    public String showEditForm(@PathVariable String maPhieu, Model model) {
        BanEntity entity = banService.getById(maPhieu).orElse(null);
        if (entity == null) {
            model.addAttribute("error", "Không tìm thấy phiếu bán!");
            return readList(model);
        }
        model.addAttribute("ban", toBan(entity));
        model.addAttribute("action", "edit");
        model.addAttribute("maPhieu", maPhieu);
        return "ban/form";
    }

    @PostMapping("/edit/{maPhieu}")
    public String editBan(@PathVariable String maPhieu, @ModelAttribute Ban ban, Model model) {
        BanEntity old = banService.getById(maPhieu).orElse(null);
        if (old == null) {
            model.addAttribute("error", "Không tìm thấy phiếu bán!");
            return readList(model);
        }
        BanEntity entity = toBanEntity(ban);
        entity.setMaPhieu(maPhieu);
        banService.save(entity);
        return "redirect:/ban";
    }

    @GetMapping("/delete/{maPhieu}")
    public String deleteBan(@PathVariable String maPhieu, Model model) {
        banService.delete(maPhieu);
        return "redirect:/ban";
    }


    private final HangHoaService hangHoaService;

    @GetMapping
    public String readList(Model model) {
        List<BanEntity> entities = banService.getAll();
        List<Ban> banList = new ArrayList<>();
        for (BanEntity e : entities) banList.add(toBan(e));
        model.addAttribute("banList", banList);
        model.addAttribute("tongSoPhieu", banList.size());
        double tongDoanhThu = banList.stream().mapToDouble(Ban::tongTien).sum();
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
            ban.setMaPhieu(com.example.servingwebcontent.util.CodeGenerator.nextBan());
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
                    // Nếu bán vượt kho, set số lượng về 0
                    target.setSoLuong(0);
                    hangHoaService.updateHangHoa(target);
                } else {
                    // Nếu bán không vượt kho, trừ kho như bình thường
                    hangHoaService.adjustSoLuong(target.getMaHang(), -ban.getSoLuong());
                }
                if(ban.getDonGia() <= 0 && target.getDonGia()!=null){
                    ban.setDonGia(target.getDonGia());
                }
            }
        }
        BanEntity entity = toBanEntity(ban);
        banService.save(entity);
        return "redirect:/ban";
    }


    @GetMapping("/list")
    public String listBan(Model model) {
        return readList(model);
    }

    @GetMapping("/stats")
    public String stats(Model model){
        Map<String, com.example.servingwebcontent.model.StatItem> map = new LinkedHashMap<>();
        List<BanEntity> entities = banService.getAll();
        for(BanEntity e: entities){
            String code = e.getMaPhieu();
            String name = e.getTenHang()!=null? e.getTenHang(): code;
            com.example.servingwebcontent.model.StatItem item = map.computeIfAbsent(code, c -> new com.example.servingwebcontent.model.StatItem(c, name));
            item.add(e.getSoLuongBan(), e.getSoLuongBan()*e.getDonGia());
            item.setThoiGian(e.getThoiGianBan());
        }
        List<com.example.servingwebcontent.model.StatItem> items = new ArrayList<>(map.values());
        double totalValue = items.stream().mapToDouble(com.example.servingwebcontent.model.StatItem::getTotalValue).sum();
        int totalQty = items.stream().mapToInt(com.example.servingwebcontent.model.StatItem::getQuantity).sum();
        model.addAttribute("items", items);
        model.addAttribute("totalValue", totalValue);
        model.addAttribute("totalQty", totalQty);
        return "statsBan";
    }

    // Chuyển đổi giữa Ban và BanEntity
    private Ban toBan(BanEntity e) {
        Ban b = new Ban();
        b.setMaPhieu(e.getMaPhieu());
        b.setTenHang(e.getTenHang());
        b.setTenKhach(e.getTenKhach());
        b.setSoLuong(e.getSoLuongBan());
        b.setDonGia(e.getDonGia());
        b.setNgayBan(e.getNgayBan());
        b.setThoiGianBan(e.getThoiGianBan());
        return b;
    }
    private BanEntity toBanEntity(Ban b) {
        return new BanEntity(
            b.getMaPhieu(),
            null, // maHang chưa dùng
            b.getTenHang(),
            b.getSoLuong(),
            b.getTenKhach(),
            b.getDonGia(),
            b.getNgayBan(),
            b.getThoiGianBan()
        );
    }
}
