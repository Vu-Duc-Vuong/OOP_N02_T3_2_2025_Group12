import java.util.*;
import Model.Ban;
import Model.HangHoa;

public class QuanLyBan {
    ArrayList<Ban> dsBan;

    public QuanLyBan() {
        try {
            dsBan = Model.BanDAO.getAll();
        } catch (Exception e) {
            dsBan = new ArrayList<>();
            System.err.println("Lỗi đọc dữ liệu từ MySQL: " + e.getMessage());
        }
    }

    public void them(Ban b) {
        dsBan.add(b);
        try {
            Model.BanDAO.save(b);
        } catch (Exception e) {
            System.err.println("Lỗi lưu vào MySQL: " + e.getMessage());
        }
    }

    public void xoa(String ma) {
        dsBan.removeIf(p -> p.maPhieu.equals(ma));
    }

    public void sua(String ma) {
        Scanner sc = new Scanner(System.in);
        for (Ban p : dsBan) {
            if (p.maPhieu.equals(ma)) {
                System.out.print("SL ban moi: ");
                p.soLuongBan = Integer.parseInt(sc.nextLine());
                break;
            }
        }
    }

    public void hienThi() {
        for (Ban p : dsBan) {
            System.out.println("Phieu: " + p.maPhieu + " - Hang: " + p.hang.tenHang +
                               ", SL: " + p.soLuongBan + ", Tong: " + p.tongTien());
        }
    }

    public double tongBan() {
        double tong = 0;
        for (Ban p : dsBan) tong += p.tongTien();
        return tong;
    }
}
