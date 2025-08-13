
import com.example.servingwebcontent.model.HangHoa;
import java.util.*;

public class TestSelector {
    public static void main(String[] args) {
        List<HangHoa> ds = new ArrayList<>();
        ds.add(new HangHoa("H01", "Gao ST25", 100, "Vinafood", 18000));
        ds.add(new HangHoa("H02", "Sua tuoi", 45, "Vinamilk", 12000));
        ds.add(new HangHoa("H03", "Nuoc ngot", 120, "Coca Cola", 15000));
        ds.add(new HangHoa("H04", "Banh mi", 25, "ABC Bakery", 35000));

        System.out.println("Lọc theo tên 'sua':");
        for (HangHoa h : Selector.filterByName(ds, "sua")) {
            System.out.println(h);
        }
        System.out.println("\nLọc theo số lượng > 50:");
        for (HangHoa h : Selector.filterBySoLuong(ds, 50)) {
            System.out.println(h);
        }
    }
}
