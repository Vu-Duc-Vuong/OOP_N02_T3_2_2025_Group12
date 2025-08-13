import com.example.servingwebcontent.model.HangHoa;
import com.example.servingwebcontent.model.Nhap;
import java.time.LocalDate;
import java.util.*;

public class TestThongKeNhapTheoNgay {
    public static void main(String[] args) {
        HangHoa h1 = new HangHoa("H01", "Gao", 0, "Vinafood", 18000);
        List<Nhap> nhapList = Arrays.asList(
            new Nhap("PN01", h1, 10, LocalDate.of(2025,8,10)),
            new Nhap("PN02", h1, 20, LocalDate.of(2025,8,11)),
            new Nhap("PN03", h1, 30, LocalDate.of(2025,8,10))
        );
        int tongNhapNgay10 = nhapList.stream()
            .filter(n -> n.getNgayNhap().equals(LocalDate.of(2025,8,10)))
            .mapToInt(Nhap::getSoLuong).sum();
        System.out.println("Tổng nhập ngày 10/8/2025: " + tongNhapNgay10);
    }
}
