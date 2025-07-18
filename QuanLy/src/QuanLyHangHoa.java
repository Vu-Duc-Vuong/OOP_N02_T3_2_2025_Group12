import java.util.ArrayList;
import java.util.Scanner;

public class QuanLyHangHoa {
    ArrayList<HangHoa> danhSach = new ArrayList<>();

    public ArrayList<HangHoa> them(HangHoa h) {
        danhSach.add(h);
        return danhSach;
    }

    public ArrayList<HangHoa> xoa(String maHang) {
        for (int i = 0; i < danhSach.size(); i++) {
            if (danhSach.get(i).maHang.equals(maHang)) {
                danhSach.remove(i);
                break;
            }
        }
        return danhSach;
    }

    public ArrayList<HangHoa> sua(String maHang) {
        Scanner sc = new Scanner(System.in);
        for (int i = 0; i < danhSach.size(); i++) {
            if (danhSach.get(i).maHang.equals(maHang)) {
                System.out.print("Nhập tên mới: ");
                String tenMoi = sc.nextLine();

                System.out.print("Nhập số lượng tồn mới: ");
                int soLuongMoi = Integer.parseInt(sc.nextLine());

                System.out.print("Nhập nhà sản xuất mới: ");
                String nsxMoi = sc.nextLine();

                danhSach.get(i).tenHang = tenMoi;
                danhSach.get(i).soLuongTon = soLuongMoi;
                danhSach.get(i).nhaSanXuat = nsxMoi;
                break;
            }
        }
        return danhSach;
    }

    public void lietKe() {
        for (HangHoa h : danhSach) {
            System.out.println("Mã: " + h.maHang);
            System.out.println("Tên: " + h.tenHang);
            System.out.println("SL tồn: " + h.soLuongTon);
            System.out.println("NSX: " + h.nhaSanXuat);
            System.out.println();
        }
    }
}
