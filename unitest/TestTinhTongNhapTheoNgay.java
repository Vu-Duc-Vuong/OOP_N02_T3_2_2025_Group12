import java.time.LocalDate;
import Model.HangHoa;
import Model.Nhap;

public class TestTinhTongNhapTheoNgay {
    public static void test() {
        HangHoa h1 = new HangHoa("H01", "Bút", 0, "DucVuong", 5000);
        HangHoa h2 = new HangHoa("H02", "Vở năm roi", 0, "DucVuong", 10000);
        HangHoa h3 = new HangHoa("H03", "Thước", 0, "DucVuong", 7000);

        QuanLyNhap qln = new QuanLyNhap();

        qln.them(new Nhap("PN01", h1, 10, LocalDate.of(2025, 7, 21)));
        qln.them(new Nhap("PN02", h2, 5, LocalDate.of(2025, 7, 21)));
        qln.them(new Nhap("PN03", h3, 3, LocalDate.of(2025, 7, 20)));

        qln.tinhTongNhapTheoNgay();
    }
}
