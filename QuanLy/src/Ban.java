import java.time.LocalDate;

public class Ban {
    private String hanghoaID;
    private int soLuongBan;
    private double giaBan;
    private LocalDate ngayBan;
    
    public Ban(String hanghoaID, int soLuongBan, double giaBan, LocalDate ngayBan) {
        this.hanghoaID = hanghoaID;
        this.soLuongBan = soLuongBan;
        this.giaBan = giaBan;
        this.ngayBan = ngayBan;
    }
    
    public double tongTienBan() {
        return soLuongBan * giaBan;
    }
    
    public void hienThiBan() {
        System.out.printf("Ma HH: %s | SL ban: %d | Gia: %.2f | Ngay: %s\n", 
                         hanghoaID, soLuongBan, giaBan, ngayBan.toString());
    }
    
    public String getHanghoaID() {
        return hanghoaID;
    }
    
    public int getSoLuongBan() {
        return soLuongBan;
    }
    
    public double getGiaBan() {
        return giaBan;
    }
    
    public LocalDate getNgayBan() {
        return ngayBan;
    }
    
    public void setSoLuongBan(int soLuongBan) {
        this.soLuongBan = soLuongBan;
    }
    
    public void setGiaBan(double giaBan) {
        this.giaBan = giaBan;
    }
    
    public void setNgayBan(LocalDate ngayBan) {
        this.ngayBan = ngayBan;
    }
}
