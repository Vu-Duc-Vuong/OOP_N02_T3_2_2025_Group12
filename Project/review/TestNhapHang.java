import com.example.servingwebcontent.model.HangHoa;
import com.example.servingwebcontent.model.Nhap;
import java.time.LocalDate;

public class TestNhapHang {
    public static void main(String[] args) {
        HangHoa h1 = new HangHoa("H01", "Gao", 0, "Vinafood", 18000);
        Nhap nhap1 = new Nhap("PN01", h1, 50, LocalDate.now());
        h1.setSoLuong(h1.getSoLuong() + nhap1.getSoLuong());
        System.out.println("Sau khi nhập hàng: " + h1);
    }
}
