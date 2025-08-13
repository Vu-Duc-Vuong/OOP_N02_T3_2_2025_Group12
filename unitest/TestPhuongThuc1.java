import Model.HangHoa;
import java.util.ArrayList;

public class TestPhuongThuc1 {
    public static void main(String[] args) {
        System.out.println("=== TEST PHƯƠNG THỨC LỌC HÀNG HÓA ===");
        System.out.println("Người thực hiện: Tưởng Văn Tuyên - 24100462\n");

        QuanLyHangHoa ql = new QuanLyHangHoa();

        ql.them(new HangHoa("SP001", "Gao ST25", 150, "Vinafood", 18000));
        ql.them(new HangHoa("SP002", "Sua tuoi", 45, "Vinamilk", 12000));
        ql.them(new HangHoa("HH001", "Nuoc ngot", 120, "Coca Cola", 15000));
        ql.them(new HangHoa("HH002", "Banh mi", 25, "ABC Bakery", 35000));

        System.out.println("Danh sách hàng hóa:");
        ql.hienThi();

        System.out.println("\n1. Lọc theo mã 'SP':");
        ArrayList<HangHoa> kq1 = ql.locTheoMa("SP");
        ql.hienThiKetQuaLoc(kq1, "mã chứa SP");

        System.out.println("\n2. Lọc theo tên 'Sua':");
        ArrayList<HangHoa> kq2 = ql.locTheoTen("Sua");
        ql.hienThiKetQuaLoc(kq2, "tên chứa Sua");

        System.out.println("\n3. Lọc theo nhà sản xuất 'Vinamilk':");
        ArrayList<HangHoa> kq3 = ql.locTheoNhaSanXuat("Vinamilk");
        ql.hienThiKetQuaLoc(kq3, "nhà sản xuất Vinamilk");

        System.out.println("\n4. Lọc theo số lượng 50-150:");
        ArrayList<HangHoa> kq4 = ql.locTheoSoLuong(50, 150);
        ql.hienThiKetQuaLoc(kq4, "số lượng 50-150");

        System.out.println("\n=== Test hoàn thành ===");
    }
}
