// Selector.java - ví dụ review logic cho quản lý hàng hóa
import Model.HangHoa;
import java.util.List;
import java.util.stream.Collectors;

public class Selector {

    public static List<HangHoa> filterByName(List<HangHoa> list, String keyword) {
        return list.stream()
                .filter(h -> h.getTen().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }
 
    public static List<HangHoa> filterBySoLuong(List<HangHoa> list, int min) {
        return list.stream()
                .filter(h -> h.getSoLuong() > min)
                .collect(Collectors.toList());
    }
}
