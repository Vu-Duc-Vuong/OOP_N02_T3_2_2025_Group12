public class QuanLyDoanhThu {
    QuanLyNhap qlNhap;
    QuanLyBan qlBan;

    public QuanLyDoanhThu(QuanLyNhap qlNhap, QuanLyBan qlBan) {
        this.qlNhap = qlNhap;
        this.qlBan = qlBan;
    }

    public void thongKe() {
        double tongNhap = qlNhap.tongNhap();
        double tongBan = qlBan.tongBan();
        double chenhLech = tongBan - tongNhap;

        System.out.println("Tong nhap " + tongNhap);
        System.out.println("Tong ban: " + tongBan);

        if (chenhLech > 0)
            System.out.println("Lai: " + chenhLech);
        else if (chenhLech < 0)
            System.out.println("Lo: " + (-chenhLech));
        else
            System.out.println("Hoa tien.");
    }
}
