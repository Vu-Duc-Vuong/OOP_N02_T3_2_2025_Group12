import java.time.LocalDate;
public class TestNhap {
    public static void main(String[] args) {
        Nhap n = new Nhap("H01", 10, 25000, LocalDate.of(2025, 7, 14));
        n.hienThiNhap();
        System.out.println("Tong tien nhap: " + n.tongTienNhap());
    }
}
