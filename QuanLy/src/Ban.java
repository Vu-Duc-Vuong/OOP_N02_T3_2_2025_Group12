public class Ban {
    private String hanghoaID;
    private String tenHangHoa;
    private int soLuongBan;
    private double giaBan;

    public Ban(String hanghoaID, String tenHangHoa, int soLuongBan, double giaBan) {
        this.hanghoaID = hanghoaID;
        this.tenHangHoa = tenHangHoa;
        this.soLuongBan = soLuongBan;
        this.giaBan = giaBan;
    }

    public double tongBan() {
        return soLuongBan * giaBan;
    }

    public void hienThiBan() {
        System.out.printf("Ban: %s - %s - SL: %d - Gia: %.2f\n", hanghoaID, tenHangHoa, soLuongBan, giaBan);
    }

    public String getHanghoaID() {
        return hanghoaID;
    }

    public int getSoLuongBan() {
        return soLuongBan;
    }
}
