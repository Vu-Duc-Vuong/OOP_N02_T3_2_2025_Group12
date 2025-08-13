package Project.QuanLy;

import Project.review.*;

public class Main {
	public static void main(String[] args) {
		System.out.println("=== TEST NHẬP HÀNG ===");
	Project.review.TestNhapHang.testNhapHang();
		System.out.println("\n=== TEST BÁN HÀNG ===");
	Project.review.TestBanHang.testBanHang();
		System.out.println("\n=== TEST TỒN KHO ===");
	Project.review.TestTonKho.testTonKho();
		System.out.println("\n=== TEST DOANH THU ===");
	Project.review.TestDoanhThu.testDoanhThu();
		System.out.println("\n=== TEST THỐNG KÊ NHẬP THEO NGÀY ===");
	Project.review.TestThongKeNhapTheoNgay.testThongKeNhapTheoNgay();
		System.out.println("\n=== TEST SELECTOR ===");
	Project.review.TestSelector.testSelector();
	}
}
