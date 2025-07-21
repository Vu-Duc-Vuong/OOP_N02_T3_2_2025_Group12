package Model;

public class HangHoa {
    public String maHang;
    public String tenHang;
    public int soLuong;
    public String nhaSanXuat;
    public double donGia;

    public HangHoa(String maHang, String tenHang, int soLuong, String nhaSanXuat, double donGia) {
        this.maHang = maHang;
        this.tenHang = tenHang;
        this.soLuong = soLuong;
        this.nhaSanXuat = nhaSanXuat;
        this.donGia = donGia;
    }

    public double thanhTien() {
        return soLuong * donGia;
    }
}
