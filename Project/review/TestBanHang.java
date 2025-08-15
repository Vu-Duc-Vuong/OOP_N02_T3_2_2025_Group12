package Project.review;
import Model.HangHoa;
import Model.Ban;
import java.time.LocalDate;

public class TestBanHang {
    public static void testBanHang() {
    HangHoa h1 = new HangHoa("H01", "Gao", 50, "Vinafood", 18000);
    // Sinh mã phiếu ngẫu nhiên để không trùng
    String maPhieu = "PB" + System.currentTimeMillis();
    Ban ban1 = new Ban(maPhieu, h1, 20);
    ban1.tenKhach = "Khach Test";
    h1.soLuong = h1.soLuong - ban1.getSoLuong();
    Model.QuanLyBan qlBan = new Model.QuanLyBan();
    qlBan.them(ban1);
    System.out.println("Sau khi bán hàng và lưu vào MySQL: " + h1);
    }
}
