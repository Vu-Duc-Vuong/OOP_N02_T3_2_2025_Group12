public class QuanLyDoanhThu {
    DoanhThu dt = new DoanhThu();

    public void tinhDoanhThu(double tongBan, double tongNhap) {
        dt.setTrongNgay(tongBan);
        dt.setTrongThang(tongBan);
        dt.tinhLaiLo(tongBan, tongNhap);
        dt.hienThiDoanhThu();
    }
}
