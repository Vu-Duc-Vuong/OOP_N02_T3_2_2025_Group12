package com.example.servingwebcontent.service;

import com.example.servingwebcontent.model.Nhap;
import com.example.servingwebcontent.model.NhapEntity;
import com.example.servingwebcontent.model.HangHoa;
import com.example.servingwebcontent.repository.NhapRepository;
import com.example.servingwebcontent.repository.HangHoaRepository;
import org.springframework.stereotype.Service;
import com.example.servingwebcontent.util.CodeGenerator;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NhapService {
    public Nhap getNhapById(Long id) {
        NhapEntity entity = nhapRepository.findById(id).orElse(null);
        return entity != null ? map(entity) : null;
    }

    @Transactional
    public void updateNhapById(Long id, Nhap nhap) {
        NhapEntity entity = nhapRepository.findById(id).orElse(null);
        if (entity == null) throw new IllegalArgumentException("Không tìm thấy phiếu nhập!");
        // Cập nhật các trường
    entity.setTenHang(nhap.getTenHang());
    entity.setSoLuongNhap(nhap.getSoLuongNhap());
    entity.setGiaNhap(nhap.getGiaNhap());
    entity.setThoiGianNhap(nhap.getThoiGianNhap());
    entity.setNgayNhap(nhap.getNgayNhap());
    entity.setNhaSanXuat(nhap.getNhaSanXuat());
        nhapRepository.save(entity);
        // Đồng bộ lại hàng hóa
        HangHoa hangHoa = hangHoaRepository.findById(entity.getHanghoaID()).orElse(null);
        if (hangHoa != null) {
            hangHoa.setTenHang(nhap.getTenHang());
            hangHoa.setDonGia(nhap.getGiaNhap());
            hangHoa.setNhaSanXuat(nhap.getNhaSanXuat());
            hangHoaRepository.save(hangHoa);
        }
    }

    @Transactional
    public void deleteNhapById(Long id) {
        NhapEntity entity = nhapRepository.findById(id).orElse(null);
        if (entity == null) throw new IllegalArgumentException("Không tìm thấy phiếu nhập!");
        nhapRepository.delete(entity);
    }

    private final NhapRepository nhapRepository;
    private final HangHoaRepository hangHoaRepository;

    public NhapService(NhapRepository nhapRepository, HangHoaRepository hangHoaRepository) {
    this.nhapRepository = nhapRepository;
    this.hangHoaRepository = hangHoaRepository;
    }

    private Nhap map(NhapEntity e) {
        Nhap n = new Nhap();
        n.setId(e.getId());
        n.setHanghoaID(e.getHanghoaID());
        n.setTenHang(e.getTenHang());
        n.setSoLuongNhap(e.getSoLuongNhap());
        n.setGiaNhap(e.getGiaNhap());
        n.setNgayNhap(e.getNgayNhap());
        n.setThoiGianNhap(e.getThoiGianNhap());
        // Map nhà sản xuất: ưu tiên lấy từ entity, nếu null thì lấy từ hàng hóa
        if (e.getNhaSanXuat() != null && !e.getNhaSanXuat().isEmpty()) {
            n.setNhaSanXuat(e.getNhaSanXuat());
        } else {
            HangHoa hang = hangHoaRepository.findById(e.getHanghoaID()).orElse(null);
            if (hang != null) {
                n.setNhaSanXuat(hang.getNhaSanXuat());
            } else {
                n.setNhaSanXuat("");
            }
        }
        return n;
    }

    public List<Nhap> getAllNhap() {
        return nhapRepository.findAll().stream()
                .sorted(Comparator.comparing(NhapEntity::getThoiGianNhap, Comparator.nullsLast(Comparator.naturalOrder())).reversed())
                .map(this::map)
                .collect(Collectors.toList());
    }

    private void validate(Nhap nhap){
        if (nhap.getHanghoaID() == null || nhap.getHanghoaID().isEmpty()) {
            // Sinh mã nhập mới (nếu không truyền) dùng chung làm mã hàng hóa mới.
            String code = CodeGenerator.nextNhap();
            nhap.setHanghoaID(code);
        }
        if (nhap.getTenHang() == null || nhap.getTenHang().isEmpty()) {
            throw new IllegalArgumentException("Tên hàng hóa không được để trống!");
        }
        if (nhap.getSoLuongNhap() <= 0) {
            throw new IllegalArgumentException("Số lượng nhập phải lớn hơn 0!");
        }
        if (nhap.getGiaNhap() < 0) {
            throw new IllegalArgumentException("Giá nhập phải >= 0!");
        }
        if (nhap.getNgayNhap() == null) {
            nhap.setNgayNhap(LocalDate.now());
        }
        if (nhap.getThoiGianNhap() == null || !nhap.getThoiGianNhap().toLocalDate().equals(nhap.getNgayNhap())) {
            nhap.setThoiGianNhap(LocalDateTime.of(nhap.getNgayNhap(), java.time.LocalTime.now()));
        }
    }

    @Transactional
    public void addNhap(Nhap nhap) { addNhapInternal(nhap); }

    private void addNhapInternal(Nhap nhap){
        validate(nhap);
        NhapEntity entity = new NhapEntity(
                nhap.getHanghoaID(),
                nhap.getTenHang()!=null? nhap.getTenHang(): nhap.getHanghoaID(),
                nhap.getSoLuongNhap(),
                nhap.getGiaNhap(),
                nhap.getNgayNhap(),
                nhap.getThoiGianNhap()
        );
        nhapRepository.save(entity);

        HangHoa hangHoa = hangHoaRepository.findById(nhap.getHanghoaID()).orElse(null);
        if (hangHoa == null) {
            hangHoa = new HangHoa(
                nhap.getHanghoaID(),
                nhap.getTenHang(),
                nhap.getSoLuongNhap(),
                nhap.getNhaSanXuat() != null ? nhap.getNhaSanXuat() : "Nhập Kho",
                nhap.getGiaNhap(),
                LocalDate.now().getYear()
            );
        } else {
            hangHoa.setSoLuong(hangHoa.getSoLuong() + nhap.getSoLuongNhap());
            if (nhap.getTenHang()!=null) hangHoa.setTenHang(nhap.getTenHang());
            if (nhap.getGiaNhap()>0) hangHoa.setDonGia(nhap.getGiaNhap());
            if (nhap.getNhaSanXuat()!=null && !nhap.getNhaSanXuat().isEmpty()) hangHoa.setNhaSanXuat(nhap.getNhaSanXuat());
        }
        hangHoaRepository.save(hangHoa);
    }

    public Nhap getNhapByIndex(int index) {
        List<Nhap> all = getAllNhap();
        if (index >=0 && index < all.size()) return all.get(index);
        return null;
    }

    public void updateNhap(int index, Nhap nhap) {
        addNhap(nhap);
    }

    public void deleteNhap(int index) {
        throw new UnsupportedOperationException("Xóa theo index không còn hỗ trợ sau khi chuyển JPA");
    }

    public List<Nhap> getNhapByDate(LocalDate date) {
        return nhapRepository.findByNgayNhap(date).stream().map(this::map).collect(Collectors.toList());
    }

    public double getTongTienNhapTheoNgay(LocalDate date) {
        return getNhapByDate(date).stream().mapToDouble(Nhap::getTongTien).sum();
    }

    public int getTotalCount() { return (int) nhapRepository.count(); }

    public List<Nhap> searchNhap(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) return getAllNhap();
        String kw = keyword.toLowerCase();
        return getAllNhap().stream()
                .filter(n -> n.getHanghoaID().toLowerCase().contains(kw) || (n.getTenHang()!=null && n.getTenHang().toLowerCase().contains(kw)))
                .collect(Collectors.toList());
    }
}
