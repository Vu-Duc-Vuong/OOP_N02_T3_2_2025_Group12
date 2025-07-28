package com.example.servingwebcontent.quanly;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;
import com.example.servingwebcontent.quanly.model.Nhap;
import com.example.servingwebcontent.quanly.model.HangHoa;

public class QuanLyNhap {
    ArrayList<Nhap> dsNhap = new ArrayList<>();

    // Thêm phiếu nhập
    public void them(Nhap n) {
        dsNhap.add(n);
    }

    // Xoá phiếu nhập theo mã
    public void xoa(String ma) {
        dsNhap.removeIf(p -> p.maPhieu.equals(ma));
    }

    // Sửa số lượng nhập
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

    // Hiển thị danh sách đã nhập
    public void hienThi() {
        for (Nhap p : dsNhap) {
            System.out.println("Phieu: " + p.maPhieu + " - Hang: " + p.hang.tenHang +
                               ", SL: " + p.soLuongNhap + ", Tong: " + p.tongTien() +
                               ", Ngay: " + p.getNgayNhap());
        }
    }

    // Tính tổng tiền nhập các phiếu
    public double tongNhap() {
        double tong = 0;
        for (Nhap p : dsNhap) {
            tong += p.tongTien();
        }
        return tong;
    }

//phương thức 2 tính tổng nhập trong ngày ...: 
    public double tongNhapTrongNgay(LocalDate ngay) {
        double tong = 0;
        for (Nhap p : dsNhap) {
            if (p.getNgayNhap().equals(ngay)) {
                tong += p.tongTien();
            }
        }
        return tong;
    }
    // Phương thức 2: tính tổng nhập theo ngày:
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
