
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;
import Model.Nhap;
import Model.HangHoa;

public class QuanLyNhap {
    ArrayList<Nhap> dsNhap = new ArrayList<>();

    public boolean them(Nhap n) {
        if (n == null) {
            System.out.println("Phiếu nhập không được null!");
            return false;
        }
        if (n.maPhieu == null || n.maPhieu.trim().isEmpty()) {
            System.out.println("Mã phiếu nhập không hợp lệ!");
            return false;
        }
        if (n.hang == null) {
            System.out.println("Hàng hóa không được null!");
            return false;
        }
        if (n.soLuongNhap <= 0) {
            System.out.println("Số lượng nhập phải lớn hơn 0!");
            return false;
        }
        if (n.ngayNhap == null) {
            System.out.println("Ngày nhập không được null!");
            return false;
        }
        try {
            dsNhap.add(n);
            return true;
        } catch (Exception e) {
            System.out.println("Lỗi khi thêm phiếu nhập: " + e.getMessage());
            return false;
        }
    }

    public void xoa(String ma) {
        dsNhap.removeIf(p -> p.maPhieu.equals(ma));
    }

    public void sua(String ma) {
        Scanner sc = new Scanner(System.in);
        for (Nhap p : dsNhap) {
            if (p.maPhieu.equals(ma)) {
                System.out.print("SL nhap moi: ");
                p.soLuongNhap = Integer.parseInt(sc.nextLine());
                break;
            }
        }
    }

    public void hienThi() {
        for (Nhap p : dsNhap) {
            System.out.println("Phieu: " + p.maPhieu + " - Hang: " + p.hang.tenHang +
                               ", SL: " + p.soLuongNhap + ", Tong: " + p.tongTien() +
                               ", Ngay: " + p.getNgayNhap());
        }
    }

    public double tongNhap() {
        double tong = 0;
        for (Nhap p : dsNhap) {
            tong += p.tongTien();
        }
        return tong;
    }

    public double tongNhapTrongNgayHienTai() {
        double tong = 0;
        LocalDate ngayHienTai = LocalDate.now();
        for (Nhap p : dsNhap) {
            if (p.getNgayNhap().equals(ngayHienTai)) {
                tong += p.soLuongNhap * p.hang.donGia;
            }
        }
        return tong;
    }
 public void tinhTongNhapTheoNgay() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Nhập ngày cần thống kê (định dạng dd-MM-yyyy): ");
        String input = sc.nextLine();
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate ngay = LocalDate.parse(input, formatter);
            double tong = tongNhapTrongNgay(ngay);
            System.out.println("Tổng nhập theo ngày " + ngay.format(formatter) + " là: " + tong);

        } catch (DateTimeParseException e) {
            System.out.println("NGÀY KHÔNG HỢP LỆ. Hãy nhập theo dd-MM-yyyy");
        }
    }
}
