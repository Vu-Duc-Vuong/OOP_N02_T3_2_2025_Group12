import java.util.*;

public class QuanLyHangHoa {
    ArrayList<HangHoa> ds = new ArrayList<>();

    public void them(HangHoa h) {
        ds.add(h);
    }

    public void xoa(String ma) {
        ds.removeIf(h -> h.maHang.equals(ma));
    }

    public void sua(String ma) {
        Scanner sc = new Scanner(System.in);
        for (HangHoa h : ds) {
            if (h.maHang.equals(ma)) {
                System.out.print("Ten moi: ");
                h.tenHang = sc.nextLine();
                System.out.print("SL moi: ");
                h.soLuong = Integer.parseInt(sc.nextLine());
                System.out.print("NSX moi: ");
                h.nhaSanXuat = sc.nextLine();
                System.out.print("Gia moi: ");
                h.donGia = Double.parseDouble(sc.nextLine());
                break;
            }
        }
    }

    public void hienThi() {
        for (HangHoa h : ds) {
            System.out.println("[" + h.maHang + "] " + h.tenHang + " - SL: " + h.soLuong +
                               ", NSX: " + h.nhaSanXuat + ", Gia: " + h.donGia);
        }
    }

    public HangHoa timTheoMa(String ma) {
        for (HangHoa h : ds) {
            if (h.maHang.equals(ma)) return h;
        }
        return null;
    }

    public double tongTienNhap() {
        double tong = 0;
        for (HangHoa h : ds) tong += h.thanhTien();
        return tong;
    }
}
