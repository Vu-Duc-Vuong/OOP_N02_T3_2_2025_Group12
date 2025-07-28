package com.example.servingwebcontent.quanly;

public class DoanhThu {
    QuanLyNhap qlNhap;
    QuanLyBan qlBan;

    public DoanhThu(QuanLyNhap qlNhap, QuanLyBan qlBan) {
        this.qlNhap = qlNhap;
        this.qlBan = qlBan;
    }

    public void thongKe() {
        double tongNhap = qlNhap.tongNhap();
        double tongBan = qlBan.tongBan();
        double chenhLech = tongBan - tongNhap;

        System.out.println("=== THONG KE DOANH THU ===");
        System.out.printf("Tong tien nhap hang: %.2f\n", tongNhap);
        System.out.printf("Tong tien ban hang : %.2f\n", tongBan);
        
        if (chenhLech > 0) {
            System.out.printf("→ Lai: %.2f\n", chenhLech);
        } else if (chenhLech < 0) {
            System.out.printf("→ Lo: %.2f\n", -chenhLech);
        } else {
            System.out.println("→ Hoa tien.");
        }
    }
}
