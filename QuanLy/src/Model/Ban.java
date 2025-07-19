
public class Ban {
    String maPhieu;
    HangHoa hang;
    int soLuongBan;

    public Ban(String maPhieu, HangHoa hang, int soLuongBan) {
        this.maPhieu = maPhieu;
        this.hang = hang;
        this.soLuongBan = soLuongBan;
    }

    public double tongTien() {
        return soLuongBan * hang.donGia;
    }
}
