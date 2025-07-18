public class TestQuanLyHangHoa {
    public static void test() {
        QuanLyHangHoa ql = new QuanLyHangHoa();

        HangHoa h1 = new HangHoa("HH01", "Gạo", 100, "Vinafood");
        HangHoa h2 = new HangHoa("HH02", "Đường", 50, "Đường Biên Hòa");

        ql.them(h1);
        ql.them(h2);

        System.out.println("=== Danh sách sau khi thêm ===");
        ql.lietKe();

        ql.xoa("HH01");
        System.out.println("=== Danh sách sau khi xóa HH01 ===");
        ql.lietKe();

        ql.sua("HH02");
        System.out.println("=== Danh sách sau khi sửa HH02 ===");
        ql.lietKe();
    }
}
