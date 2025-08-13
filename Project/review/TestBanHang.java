package Project.review;
import Model.HangHoa;
import Model.Ban;
import java.time.LocalDate;

public class TestBanHang {
    public static void testBanHang() {
        HangHoa h1 = new HangHoa("H01", "Gao", 50, "Vinafood", 18000);
        Ban ban1 = new Ban("PB01", h1, 20, LocalDate.now());
        h1.setSoLuong(h1.getSoLuong() - ban1.getSoLuong());
        System.out.println("Sau khi bán hàng: " + h1);
    }
}
