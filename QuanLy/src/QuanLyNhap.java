import java.util.ArrayList;

public class QuanLyNhap {
    ArrayList<Nhap> dsNhap = new ArrayList<>();

    public void them(Nhap n) {
        dsNhap.add(n);
    }

    public double tongNhap() {
        double tong = 0;
        for (Nhap n : dsNhap) {
            tong += n.tongNhap();
        }
        return tong;
    }

    public void hienThi() {
        for (Nhap n : dsNhap) {
            n.hienThiNhap();
        }
    }
}
