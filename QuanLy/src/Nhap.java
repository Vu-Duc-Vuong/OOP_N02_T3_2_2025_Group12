import java.time.LocalDate;
public class Nhap {
    private String hanghoaID;
    private int soLuongNhap;
    private double giaNhap;
    private LocalDate ngayNhap;
    public Nhap(String hanghoaID, int soLuongNhap, double giaNhap, LocalDate ngayNhap) {
        this.hanghoaID = hanghoaID;
        this.soLuongNhap = soLuongNhap;
        this.giaNhap = giaNhap;
        this.ngayNhap = ngayNhap;
    }
    public double tongTienNhap() {
        return soLuongNhap * giaNhap;
    }
    public void hienThiNhap() {
        System.out.printf("Ma HH: %s | SL nhap: %d | Gia: %.2f | Ngay: %s\n", hanghoaID, soLuongNhap, giaNhap, ngayNhap.toString());
    }
    public String getHanghoaID() {
        return hanghoaID;
    }
    public int getSoLuongNhap() {
        return soLuongNhap;
    }
}
