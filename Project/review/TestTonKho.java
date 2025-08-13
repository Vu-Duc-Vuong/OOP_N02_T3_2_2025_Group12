import com.example.servingwebcontent.model.HangHoa;

public class TestTonKho {
    public static void main(String[] args) {
        HangHoa h1 = new HangHoa("H01", "Gao", 30, "Vinafood", 18000);
        System.out.println("Tồn kho hiện tại: " + h1.getSoLuong());
    }
}
