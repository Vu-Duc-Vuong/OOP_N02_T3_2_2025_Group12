import Model.Ban;
import Model.Nhap;
import Model.HangHoa;

public class TestQuanLyHangHoa {
    public static void test() {
        testQuanLyHangHoa();
        testLocHangHoa(); // Test cho phương thức 1 ngay trong file này
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

    // Test cho phương thức 1: Lọc hàng hóa theo tiêu chí
    static void testLocHangHoa() {
        System.out.println("\n*** Test Phương thức 1: Lọc hàng hóa theo tiêu chí ***");
        QuanLyHangHoa ql = new QuanLyHangHoa();
        
        // Thêm dữ liệu test
        HangHoa h1 = new HangHoa("H01", "Gao Tam Xoan", 100, "Vinafood", 15000);
        HangHoa h2 = new HangHoa("H02", "Duong Cat Lam", 50, "Ha Dong", 20000);
        HangHoa h3 = new HangHoa("H03", "Sua Tuoi", 30, "Vinamilk", 25000);
        HangHoa h4 = new HangHoa("H04", "Sua Chua", 40, "Vinamilk", 18000);
        HangHoa h5 = new HangHoa("H05", "Nuoc Ngot", 80, "Coca Cola", 12000);
        
        ql.them(h1);
        ql.them(h2);
        ql.them(h3);
        ql.them(h4);
        ql.them(h5);
        
        System.out.println("---> Danh sách toàn bộ hàng hóa:");
        ql.hienThi();
        
        // Test lọc theo tên
        System.out.println("\n---> Test lọc theo tên 'Sua':");
        ql.hienThiKetQuaLoc(ql.locTheoTen("Sua"), "tên chứa 'Sua'");
        
        // Test lọc theo nhà sản xuất
        System.out.println("\n---> Test lọc theo nhà sản xuất 'Vinamilk':");
        ql.hienThiKetQuaLoc(ql.locTheoNhaSanXuat("Vinamilk"), "nhà sản xuất 'Vinamilk'");
        
        // Test lọc theo số lượng
        System.out.println("\n---> Test lọc theo số lượng từ 40 đến 80:");
        ql.hienThiKetQuaLoc(ql.locTheoSoLuong(40, 80), "số lượng từ 40-80");
        
        // Test lọc theo mã
        System.out.println("\n---> Test lọc theo mã chứa 'H0':");
        ql.hienThiKetQuaLoc(ql.locTheoMa("H0"), "mã chứa 'H0'");
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

        DoanhThu qldt = new DoanhThu(qlNhap, qlBan);
        qldt.thongKe();
    }
}
