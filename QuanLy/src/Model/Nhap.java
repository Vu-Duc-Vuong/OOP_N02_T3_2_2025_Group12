import java.time.LocalDate;

public class Nhap {
    String maPhieu;
    HangHoa hang;
    int soLuongNhap;
    LocalDate ngayNhap; // ⬅ Thêm dòng này

    public Nhap(String maPhieu, HangHoa hang, int soLuongNhap, LocalDate ngayNhap) {
        this.maPhieu = maPhieu;
        this.hang = hang;
        this.soLuongNhap = soLuongNhap;
        this.ngayNhap = ngayNhap;
    }

    public double tongTien() {
        return soLuongNhap * hang.donGia;
    }

    public LocalDate getNgayNhap() {
        return ngayNhap;
    }
}
