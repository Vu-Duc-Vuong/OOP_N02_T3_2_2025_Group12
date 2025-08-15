
package Model;

public class Ban {
    public String maPhieu;
    public HangHoa hang;
    public int soLuongBan;

    // Giả sử có các trường này để lưu đủ thông tin cho DB
    public String tenKhach;
    public java.time.LocalDate ngayBan = java.time.LocalDate.now();
    public java.time.LocalDateTime thoiGianBan = java.time.LocalDateTime.now();

    public Ban(String maPhieu, HangHoa hang, int soLuongBan) {
        this.maPhieu = maPhieu;
        this.hang = hang;
        this.soLuongBan = soLuongBan;
    }

    public String getMaPhieu() { return maPhieu; }
    public String getMaHang() { return hang != null ? hang.maHang : null; }
    public String getTenHang() { return hang != null ? hang.tenHang : null; }
    public String getTenKhach() { return tenKhach; }
    public int getSoLuong() { return soLuongBan; }
    public double getDonGia() { return hang != null ? hang.donGia : 0; }
    public java.time.LocalDate getNgayBan() { return ngayBan; }
    public java.time.LocalDateTime getThoiGianBan() { return thoiGianBan; }

    public double tongTien() {
        return soLuongBan * (hang != null ? hang.donGia : 0);
    }
}
