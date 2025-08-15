package Model;
import java.time.LocalDate;

public class Nhap {
    public String maPhieu;
    public HangHoa hang;
    public int soLuongNhap;
    public LocalDate ngayNhap;

    public Nhap(String maPhieu, HangHoa hang, int soLuongNhap, LocalDate ngayNhap) {
        this.maPhieu = maPhieu;
        this.hang = hang;
        this.soLuongNhap = soLuongNhap;
        this.ngayNhap = ngayNhap;
    }

    public Nhap(String maPhieu, HangHoa hang, int soLuongNhap) {
        this.maPhieu = maPhieu;
        this.hang = hang;
        this.soLuongNhap = soLuongNhap;
        this.ngayNhap = LocalDate.now();
    }

    public double tongTien() {
        return soLuongNhap * hang.donGia;
    }

    public LocalDate getNgayNhap() {
        return ngayNhap;
    }

    public int getSoLuong() {
        return soLuongNhap;
    }
}
