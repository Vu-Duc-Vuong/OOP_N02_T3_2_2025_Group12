public class DoanhThu {
    private double trongNgay;
    private double trongThang;
    private double lai;
    private double lo;

    public void tinhLaiLo(double tongBan, double tongNhap) {
        if (tongBan >= tongNhap) {
            lai = tongBan - tongNhap;
            lo = 0;
        } else {
            lo = tongNhap - tongBan;
            lai = 0;
        }
    }

    public void setTrongNgay(double tongBan) {
        this.trongNgay = tongBan;
    }

    public void setTrongThang(double tongBan) {
        this.trongThang = tongBan;
    }

    public void hienThiDoanhThu() {
        System.out.printf("Ngay: %.2f - Thang: %.2f - Lai: %.2f - Lo: %.2f\n", trongNgay, trongThang, lai, lo);
    }
}
