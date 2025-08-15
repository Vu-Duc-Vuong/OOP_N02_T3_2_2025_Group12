package com.example.servingwebcontent.core;

import com.example.servingwebcontent.controller.*;
import com.example.servingwebcontent.controller.ChiTietDichVuController.AddRequest;
import com.example.servingwebcontent.controller.HoaDonController.CreateInvoiceRequest;
import com.example.servingwebcontent.model.*;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

@Service
public class HotelCoreService {

    private final KhachHangController khachCtrl;
    private final PhongKhachSanController phongCtrl;
    private final DatPhongController datPhongCtrl;
    private final ChiTietDichVuController ctdvCtrl;
    private final HoaDonController hoaDonCtrl;
    private final DichVuController dichVuCtrl;

    public HotelCoreService(
            KhachHangController khachCtrl,
            PhongKhachSanController phongCtrl,
            DatPhongController datPhongCtrl,
            ChiTietDichVuController ctdvCtrl,
            HoaDonController hoaDonCtrl,
            DichVuController dichVuCtrl
    ) {
        this.khachCtrl = khachCtrl;
        this.phongCtrl = phongCtrl;
        this.datPhongCtrl = datPhongCtrl;
        this.ctdvCtrl = ctdvCtrl;
        this.hoaDonCtrl = hoaDonCtrl;
        this.dichVuCtrl = dichVuCtrl;
    }

    // ====================== DTO kết quả ======================
    public static final class KetQuaDatPhong {
        private final String maDatPhong;
        private final String maHoaDon;
        private final double tongTienPhong;
        private final double tongTienDichVu;
        private final double thue;
        private final double giamGia;
        private final double tongThanhToan;

        public KetQuaDatPhong(String maDatPhong, String maHoaDon,
                              double tongTienPhong, double tongTienDichVu,
                              double thue, double giamGia, double tongThanhToan) {
            this.maDatPhong = maDatPhong;
            this.maHoaDon = maHoaDon;
            this.tongTienPhong = tongTienPhong;
            this.tongTienDichVu = tongTienDichVu;
            this.thue = thue;
            this.giamGia = giamGia;
            this.tongThanhToan = tongThanhToan;
        }
        public String getMaDatPhong() { return maDatPhong; }
        public String getMaHoaDon() { return maHoaDon; }
        public double getTongTienPhong() { return tongTienPhong; }
        public double getTongTienDichVu() { return tongTienDichVu; }
        public double getThue() { return thue; }
        public double getGiamGia() { return giamGia; }
        public double getTongThanhToan() { return tongThanhToan; }
    }

    // ====================== 1) Core flow: đặt phòng + (DV) + hoá đơn ======================
    public KetQuaDatPhong datPhongVaThanhToan(
            String dinhDanhKhach,
            String maPhong,
            LocalDate ngayNhan,
            LocalDate ngayTra,
            int soKhach,
            Map<String,Integer> dichVuSoLuong,
            double giamGia,
            PaymentMethod phuongThuc
    ) {
        // 1) Kiểm tra khách tồn tại (dùng controller)
        var khRes = khachCtrl.getOne(dinhDanhKhach);
        if (!khRes.getStatusCode().is2xxSuccessful() || khRes.getBody() == null) {
            throw new IllegalArgumentException("Khách hàng chưa tồn tại: " + dinhDanhKhach);
        }

        // 2) Kiểm tra phòng tồn tại
        var pRes = phongCtrl.one(maPhong);
        var phong = pRes.getBody();
        if (!pRes.getStatusCode().is2xxSuccessful() || phong == null) {
            throw new IllegalArgumentException("Phòng không tồn tại: " + maPhong);
        }
        if (soKhach <= 0 || soKhach > phong.getSoNguoiToiDa()) {
            throw new IllegalArgumentException("Số khách không hợp lệ (vượt sức chứa phòng)");
        }

        // 3) Kiểm tra phòng trống (dùng DatPhongController.timPhongTrong)
        var timReq = new DatPhongController.TimPhongRequest(ngayNhan, ngayTra, soKhach, null);
        var timRes = datPhongCtrl.timPhongTrong(timReq);
        if (!timRes.getStatusCode().is2xxSuccessful()) {
            throw new IllegalStateException("Lỗi kiểm tra phòng: " + timRes.getBody());
        }
        // có thể bỏ qua list trả về và rely vào isPhongTrong trong create dưới đây

        // 4) Tạo đặt phòng
        String maDatPhong = "DP-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        DatPhong dp = new DatPhong(
                maDatPhong,
                dinhDanhKhach,
                maPhong,
                ngayNhan,
                ngayTra,
                soKhach,
                BookingStatus.DA_DAT
        );
        ResponseEntity<?> dpRes = datPhongCtrl.create(dp);
        if (!dpRes.getStatusCode().is2xxSuccessful()) {
            throw new IllegalStateException("Không thể tạo đặt phòng: " + dpRes.getBody());
        }

        // (Tuỳ) đổi trạng thái phòng cho dễ quản trị
        try { phongCtrl.updateStatus(maPhong, RoomStatus.DA_DAT.name()); } catch (Exception ignore) {}

        // 5) Thêm dịch vụ (nếu có)
        if (dichVuSoLuong != null && !dichVuSoLuong.isEmpty()) {
            for (var e : dichVuSoLuong.entrySet()) {
                String maDv = e.getKey();
                int sl = e.getValue() == null ? 0 : e.getValue();
                if (maDv == null || maDv.isBlank() || sl <= 0) continue;

                // ensure dịch vụ tồn tại (gọi controller)
                var dvRes = dichVuCtrl.one(maDv);
                if (!dvRes.getStatusCode().is2xxSuccessful() || dvRes.getBody() == null) {
                    throw new IllegalArgumentException("Dịch vụ không tồn tại: " + maDv);
                }

                // thêm dòng DV
                var addReq = new AddRequest(maDv, sl);
                var addRes = ctdvCtrl.addToBooking(maDatPhong, addReq);
                if (!addRes.getStatusCode().is2xxSuccessful()) {
                    throw new IllegalStateException("Không thể thêm dịch vụ " + maDv + ": " + addRes.getBody());
                }
            }
        }

        // 6) Tạo hoá đơn từ booking (dùng controller HoaDonController)
        String phuongThucStr = (phuongThuc == null ? PaymentMethod.TIEN_MAT.name() : phuongThuc.name());
        var createHdReq = new CreateInvoiceRequest(null, maDatPhong, giamGia, phuongThucStr);
        var hdRes = hoaDonCtrl.createFromBooking(createHdReq);
        if (!hdRes.getStatusCode().is2xxSuccessful() || !(hdRes.getBody() instanceof HoaDon hd)) {
            throw new IllegalStateException("Không thể tạo hoá đơn: " + hdRes.getBody());
        }

        return new KetQuaDatPhong(
                maDatPhong,
                hd.getMaHoaDon(),
                nz(hd.getTongTienPhong()),
                nz(hd.getTongTienDichVu()),
                nz(hd.getThue()),
                nz(hd.getGiamGia()),
                nz(hd.getTongThanhToan())
        );
    }

    // ====================== 2) Thêm phòng qua Controller ======================
    public PhongKhachSan themPhong(String maPhong,
                                   RoomType loaiPhong,
                                   Double giaMoiDem,
                                   RoomStatus tinhTrang,
                                   Integer soNguoiToiDa,
                                   String tienNghiKemTheo) {

        PhongKhachSan p = new PhongKhachSan();
        p.setMaPhong(maPhong);
        p.setLoaiPhong(loaiPhong);
        p.setGiaMoiDem(giaMoiDem == null ? 0.0 : giaMoiDem);
        p.setTinhTrang(tinhTrang == null ? RoomStatus.TRONG : tinhTrang);
        p.setSoNguoiToiDa(soNguoiToiDa == null ? 1 : soNguoiToiDa);
        p.setTienNghiKemTheo(tienNghiKemTheo);

        var res = phongCtrl.create(p);
        if (!res.getStatusCode().is2xxSuccessful() || !(res.getBody() instanceof PhongKhachSan saved)) {
            throw new IllegalStateException("Không thể tạo phòng: " + res.getBody());
        }
        return saved;
    }

    // ====================== 3) Thêm dịch vụ qua Controller ======================
    public DichVu themDichVu(String maDichVu,
                              String tenDichVu,
                              Double gia,
                              ServiceType loai) {

        DichVu dv = new DichVu();
        dv.setMaDichVu(maDichVu);
        dv.setTenDichVu(tenDichVu);
        dv.setGia(gia == null ? 0.0 : gia); // model dùng primitive double theo lỗi bạn báo
        dv.setLoai(loai == null ? ServiceType.THEO_LAN : loai);

        var res = dichVuCtrl.create(dv);
        if (!res.getStatusCode().is2xxSuccessful() || !(res.getBody() instanceof DichVu saved)) {
            throw new IllegalStateException("Không thể tạo dịch vụ: " + res.getBody());
        }
        return saved;
    }

    // ====================== Helpers ======================
    private static double nz(Double d) { return d == null ? 0.0 : d; }
}
