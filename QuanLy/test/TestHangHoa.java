public class TestHangHoa {
    public static void main(String[] args) {
        dsHangHoa ds = new dsHangHoa();

        // Thêm hàng hóa
        ds.themHangHoa(new HangHoa("HH01", "Bánh kẹo", 100, "Bibica", 2023));
        ds.themHangHoa(new HangHoa("HH02", "Sữa tươi", 50, "Vinamilk", 2024));
        ds.themHangHoa(new HangHoa("HH03", "Mì tôm", 200, "Acecook", 2022));

        // Hiển thị danh sách
        ds.hienThiTatCa();

        System.out.println("\nTăng số lượng hàng hóa có mã HH02 thêm 20...");
        ds.tangSoLuongTheoMa("HH02", 20);

        // Hiển thị lại
        System.out.println("\nDANH SÁCH SAU KHI CẬP NHẬT:");
        ds.hienThiTatCa();
    }
}
