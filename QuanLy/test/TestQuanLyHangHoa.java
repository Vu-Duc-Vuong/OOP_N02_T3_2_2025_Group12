import Model.Ban;
import Model.Nhap;

public class TestQuanLyHangHoa {
    public static void test() {
        testQuanLyHangHoa();
        testQuanLyNhap();
        testQuanLyBan();
        testThongKeDoanhThu();
    }

    static void testQuanLyHangHoa() {
        System.out.println("\n*** Quan ly Hang Hoa ***");
        QuanLyHangHoa ql = new QuanLyHangHoa();

        HangHoa h1 = new HangHoa("H01", "Gao", 100, "Vina", 15000);
        HangHoa h2 = new HangHoa("H02", "Duong", 50, "Ha Dong", 20000);
        ql.them(h1);
        ql.them(h2);

        System.out.println("---> Sau khi them:");
        ql.hienThi();

        System.out.println("---> Sau khi sua H01:");
        ql.sua("H01");
        ql.hienThi();

        System.out.println("---> Sau khi xoa H02:");
        ql.xoa("H02");
        ql.hienThi();
    }

    static void testQuanLyNhap() {
        System.out.println("\n*** Quan ly Phieu Nhap ***");
        QuanLyNhap qlNhap = new QuanLyNhap();
        HangHoa h = new HangHoa("H03", "Muoi", 30, "ViSaclo", 7000);

        qlNhap.them(new Nhap("PN1", h, 10));
        qlNhap.them(new Nhap("PN2", h, 15));

        System.out.println("---> Sau khi them:");
        qlNhap.hienThi();

        System.out.println("---> Sau khi sua PN1:");
        qlNhap.sua("PN1");
        qlNhap.hienThi();

        System.out.println("---> Sau khi xoa PN2:");
        qlNhap.xoa("PN2");
        qlNhap.hienThi();
    }

    static void testQuanLyBan() {
        System.out.println("\n*** Quan ly Phieu Ban ***");
        QuanLyBan qlBan = new QuanLyBan();
        HangHoa h = new HangHoa("H04", "Bot Mi", 80, "BanhNgon", 12000);

        qlBan.them(new Ban("PB1", h, 20));
        qlBan.them(new Ban("PB2", h, 30));

        System.out.println("---> Sau khi them:");
        qlBan.hienThi();

        System.out.println("---> Sau khi sua PB1:");
        qlBan.sua("PB1");
        qlBan.hienThi();

        System.out.println("---> Sau khi xoa PB2:");
        qlBan.xoa("PB2");
        qlBan.hienThi();
    }

    static void testThongKeDoanhThu() {
        System.out.println("\n*** Thong ke Doanh Thu ***");
        QuanLyNhap qlNhap = new QuanLyNhap();
        QuanLyBan qlBan = new QuanLyBan();

        HangHoa h = new HangHoa("H05", "Sua", 50, "Vinamilk", 25000);
        qlNhap.them(new Nhap("PN1", h, 20));
        qlBan.them(new Ban("PB1", h, 15));

        QuanLyDoanhThu qldt = new QuanLyDoanhThu(qlNhap, qlBan);
        qldt.thongKe();
    }
}
