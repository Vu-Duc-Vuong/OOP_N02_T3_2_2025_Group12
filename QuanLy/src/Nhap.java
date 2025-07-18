public class Nhap {
    private String hanghoaID;
    private int soLuongNhap;
    private double giaNhap;
    private String ngayNhap;

    public Nhap(String hanghoaID, int soLuongNhap, double giaNhap, String ngayNhap) {
        this.hanghoaID = hanghoaID;
        this.soLuongNhap = soLuongNhap;
        this.giaNhap = giaNhap;
        this.ngayNhap = ngayNhap;
    }

    public double tongNhap() {
        return soLuongNhap * giaNhap;
    }

    public void hienThiNhap() {
        System.out.printf("Nhap: %s - SL: %d - Gia: %.2f - Ngay: %s\n", hanghoaID, soLuongNhap, giaNhap, ngayNhap);
    }

    public String getHanghoaID() {
        return hanghoaID;
    }

    public int getSoLuongNhap() {
        return soLuongNhap;
    }
}
