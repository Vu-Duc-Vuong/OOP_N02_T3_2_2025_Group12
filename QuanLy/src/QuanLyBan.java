import java.util.ArrayList;

public class QuanLyBan {
    ArrayList<Ban> dsBan = new ArrayList<>();

    public void them(Ban b) {
        dsBan.add(b);
    }

    public double tongBan() {
        double tong = 0;
        for (Ban b : dsBan) {
            tong += b.tongBan();
        }
        return tong;
    }

    public void hienThi() {
        for (Ban b : dsBan) {
            b.hienThiBan();
        }
    }
}
