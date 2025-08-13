import java.util.*;
import Model.HangHoa;

public class QuanLyHangHoa {
    ArrayList<HangHoa> ds = new ArrayList<>();

    // Đã xóa chức năng thêm hàng hóa

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

    // Phương thức 1: Lọc hàng hóa theo tiêu chí
    public ArrayList<HangHoa> locTheoMa(String ma) {
        ArrayList<HangHoa> ketQua = new ArrayList<>();
        for (HangHoa h : ds) {
            if (h.maHang.toLowerCase().contains(ma.toLowerCase())) {
                ketQua.add(h);
            }
        }
        return ketQua;
    }

    public ArrayList<HangHoa> locTheoTen(String ten) {
        ArrayList<HangHoa> ketQua = new ArrayList<>();
        for (HangHoa h : ds) {
            if (h.tenHang.toLowerCase().contains(ten.toLowerCase())) {
                ketQua.add(h);
            }
        }
        return ketQua;
    }

    public ArrayList<HangHoa> locTheoNhaSanXuat(String nsx) {
        ArrayList<HangHoa> ketQua = new ArrayList<>();
        for (HangHoa h : ds) {
            if (h.nhaSanXuat.toLowerCase().contains(nsx.toLowerCase())) {
                ketQua.add(h);
            }
        }
        return ketQua;
    }

    public ArrayList<HangHoa> locTheoSoLuong(int soLuongMin, int soLuongMax) {
        ArrayList<HangHoa> ketQua = new ArrayList<>();
        for (HangHoa h : ds) {
            if (h.soLuong >= soLuongMin && h.soLuong <= soLuongMax) {
                ketQua.add(h);
            }
        }
        return ketQua;
    }

    public void hienThiKetQuaLoc(ArrayList<HangHoa> ketQua, String tieuChi) {
        System.out.println("\n=== KẾT QUẢ LỌC THEO " + tieuChi.toUpperCase() + " ===");
        if (ketQua.isEmpty()) {
            System.out.println("Không tìm thấy hàng hóa nào thỏa mãn tiêu chí.");
        } else {
            System.out.println("Tìm thấy " + ketQua.size() + " hàng hóa:");
            for (HangHoa h : ketQua) {
                System.out.println("[" + h.maHang + "] " + h.tenHang + " - SL: " + h.soLuong +
                                   ", NSX: " + h.nhaSanXuat + ", Gia: " + h.donGia);
            }
        }
    }
}
