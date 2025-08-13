package Project.review;
import Model.HangHoa;

public class TestTonKho {
    public static void testTonKho() {
        HangHoa h1 = new HangHoa("H01", "Gao", 30, "Vinafood", 18000);
        System.out.println("Tồn kho hiện tại: " + h1.getSoLuong());
    }
}
