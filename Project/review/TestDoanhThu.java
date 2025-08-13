import com.example.servingwebcontent.model.HangHoa;
import com.example.servingwebcontent.model.Ban;
import java.time.LocalDate;
import java.util.*;

public class TestDoanhThu {
    public static void main(String[] args) {
        HangHoa h1 = new HangHoa("H01", "Gao", 0, "Vinafood", 18000);
        List<Ban> banList = Arrays.asList(
            new Ban("PB01", h1, 5, LocalDate.of(2025,8,10)),
            new Ban("PB02", h1, 10, LocalDate.of(2025,8,11)),
            new Ban("PB03", h1, 15, LocalDate.of(2025,8,10))
        );
        int doanhThu = banList.stream().mapToInt(b -> b.getSoLuong() * h1.getGia()).sum();
        System.out.println("Tổng doanh thu: " + doanhThu);
    }
}
