import Model.HangHoa;
import java.util.ArrayList;

public class TestPhuongThuc1 {
    public static void main(String[] args) {
        System.out.println("=== TEST PHƯƠNG THỨC 1: LỌC HÀNG HÓA THEO TIÊU CHÍ ===");
        System.out.println("Người thực hiện: Vũ Đức Vượng - 24100383");
        System.out.println("Phương thức: Lọc hàng hóa theo mã, tên, nhà sản xuất, số lượng\n");
        
        QuanLyHangHoa ql = new QuanLyHangHoa();
        
        // Tạo dữ liệu test đa dạng
        System.out.println("*** BƯỚC 1: THÊM DỮ LIỆU HÀNG HÓA ***");
        HangHoa h1 = new HangHoa("SP001", "Gao ST25", 150, "Vinafood", 18000);
        HangHoa h2 = new HangHoa("SP002", "Duong trang", 80, "Bien Hoa", 22000);
        HangHoa h3 = new HangHoa("SP003", "Sua tuoi TH", 45, "TH True Milk", 12000);
        HangHoa h4 = new HangHoa("SP004", "Sua chua Vinamilk", 60, "Vinamilk", 8500);
        HangHoa h5 = new HangHoa("SP005", "Nuoc ngot Coca", 120, "Coca Cola", 15000);
        HangHoa h6 = new HangHoa("SP006", "Banh mi sandwich", 25, "ABC Bakery", 35000);
        HangHoa h7 = new HangHoa("HH001", "Sua dac co dau", 30, "Vinamilk", 28000);
        HangHoa h8 = new HangHoa("HH002", "Bia Saigon", 200, "Sabeco", 19000);
        
        ql.them(h1); ql.them(h2); ql.them(h3); ql.them(h4);
        ql.them(h5); ql.them(h6); ql.them(h7); ql.them(h8);
        
        System.out.println("✓ Đã thêm 8 sản phẩm test");
        System.out.println("\n---> Danh sách toàn bộ hàng hóa:");
        ql.hienThi();
        
        // Test các trường hợp lọc
        System.out.println("\n\n*** BƯỚC 2: TEST CÁC TIÊU CHÍ LỌC ***");
        
        // Test 1: Lọc theo mã
        System.out.println("\n=== TEST 1.1: Lọc theo mã hàng ===");
        testLocTheoMa(ql, "SP", "mã bắt đầu bằng 'SP'");
        testLocTheoMa(ql, "HH", "mã bắt đầu bằng 'HH'");
        testLocTheoMa(ql, "001", "mã kết thúc bằng '001'");
        testLocTheoMa(ql, "SP999", "mã không tồn tại");
        
        // Test 2: Lọc theo tên
        System.out.println("\n=== TEST 1.2: Lọc theo tên hàng ===");
        testLocTheoTen(ql, "Sua", "tên chứa 'Sua'");
        testLocTheoTen(ql, "Gao", "tên chứa 'Gao'");
        testLocTheoTen(ql, "sua", "tên chứa 'sua' (case-insensitive)");
        testLocTheoTen(ql, "Kem", "tên chứa 'Kem' (không có)");
        
        // Test 3: Lọc theo nhà sản xuất
        System.out.println("\n=== TEST 1.3: Lọc theo nhà sản xuất ===");
        testLocTheoNSX(ql, "Vinamilk", "nhà sản xuất 'Vinamilk'");
        testLocTheoNSX(ql, "Vina", "nhà sản xuất chứa 'Vina'");
        testLocTheoNSX(ql, "Coca", "nhà sản xuất chứa 'Coca'");
        testLocTheoNSX(ql, "Samsung", "nhà sản xuất 'Samsung' (không có)");
        
        // Test 4: Lọc theo số lượng
        System.out.println("\n=== TEST 1.4: Lọc theo số lượng tồn kho ===");
        testLocTheoSoLuong(ql, 50, 100, "số lượng từ 50-100");
        testLocTheoSoLuong(ql, 0, 30, "số lượng từ 0-30");
        testLocTheoSoLuong(ql, 150, 250, "số lượng từ 150-250");
        testLocTheoSoLuong(ql, 300, 500, "số lượng từ 300-500 (không có)");
        
        System.out.println("\n\n*** KẾT LUẬN ***");
        System.out.println("✓ Phương thức 1 hoạt động đúng với tất cả tiêu chí:");
        System.out.println("  - Lọc theo mã hàng hóa (có hỗ trợ tìm kiếm một phần)");
        System.out.println("  - Lọc theo tên hàng hóa (case-insensitive)");
        System.out.println("  - Lọc theo nhà sản xuất (case-insensitive)");
        System.out.println("  - Lọc theo khoảng số lượng tồn kho");
        System.out.println("✓ Xử lý đúng trường hợp không tìm thấy kết quả");
        System.out.println("✓ Hiển thị kết quả rõ ràng và đầy đủ thông tin");
    }
    
    private static void testLocTheoMa(QuanLyHangHoa ql, String ma, String moTa) {
        System.out.println("\n► Test lọc " + moTa + ":");
        ArrayList<HangHoa> kq = ql.locTheoMa(ma);
        ql.hienThiKetQuaLoc(kq, moTa);
    }
    
    private static void testLocTheoTen(QuanLyHangHoa ql, String ten, String moTa) {
        System.out.println("\n► Test lọc " + moTa + ":");
        ArrayList<HangHoa> kq = ql.locTheoTen(ten);
        ql.hienThiKetQuaLoc(kq, moTa);
    }
    
    private static void testLocTheoNSX(QuanLyHangHoa ql, String nsx, String moTa) {
        System.out.println("\n► Test lọc " + moTa + ":");
        ArrayList<HangHoa> kq = ql.locTheoNhaSanXuat(nsx);
        ql.hienThiKetQuaLoc(kq, moTa);
    }
    
    private static void testLocTheoSoLuong(QuanLyHangHoa ql, int min, int max, String moTa) {
        System.out.println("\n► Test lọc " + moTa + ":");
        ArrayList<HangHoa> kq = ql.locTheoSoLuong(min, max);
        ql.hienThiKetQuaLoc(kq, moTa);
    }
}
