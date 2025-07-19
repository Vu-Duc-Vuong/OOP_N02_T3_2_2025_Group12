import java.util.*;


public class QuanLyNhap {
    ArrayList<Nhap> dsNhap = new ArrayList<>();

    public void them(Nhap n) {
        dsNhap.add(n);
    }

    public void xoa(String ma) {
        dsNhap.removeIf(p -> p.maPhieu.equals(ma));
    }

    public void sua(String ma) {
        Scanner sc = new Scanner(System.in);
        for (Nhap p : dsNhap) {
            if (p.maPhieu.equals(ma)) {
                System.out.print("SL nhap moi: ");
                p.soLuongNhap = Integer.parseInt(sc.nextLine());
                break;
            }
        }
    }

    public void hienThi() {
        for (Nhap p : dsNhap) {
            System.out.println("Phieu: " + p.maPhieu + " - Hang: " + p.hang.tenHang + 
                               ", SL: " + p.soLuongNhap + ", Tong: " + p.tongTien());
        }
    }

    public double tongNhap() {
        double tong = 0;
        for (Nhap p : dsNhap) {
            tong += p.tongTien();
        }
        return tong;
    }
}
