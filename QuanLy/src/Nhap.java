public class Nhap {
    String maPhieu;
    HangHoa hang;
    int soLuongNhap;

    public Nhap(String maPhieu, HangHoa hang, int soLuongNhap) {
        this.maPhieu = maPhieu;
        this.hang = hang;
        this.soLuongNhap = soLuongNhap;
    }

    public double tongTien() {
        return soLuongNhap * hang.donGia;
    }
}
