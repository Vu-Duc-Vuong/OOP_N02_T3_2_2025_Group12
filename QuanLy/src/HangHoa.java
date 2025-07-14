public class HangHoa {
    private String hanghoaID;
    private String tenHangHoa;
    private int soLuongHangHoa;
    private String nhaSanXuat;
    private int namSanXuat;

    public HangHoa(String hanghoaID, String tenHangHoa, int soLuong, String nhaSanXuat, int namSanXuat) {
        this.hanghoaID = hanghoaID;
        this.tenHangHoa = tenHangHoa;
        this.soLuongHangHoa = soLuong;
        this.nhaSanXuat = nhaSanXuat;
        this.namSanXuat = namSanXuat;
    }

    public void tangSoLuong(int sl) {
        this.soLuongHangHoa += sl;
    }

    public void hienThiThongTin() {
        System.out.printf("Ma: %s | Ten: %s | SL: %d | NSX: %s | Nam: %d\n",
                hanghoaID, tenHangHoa, soLuongHangHoa, nhaSanXuat, namSanXuat);
    }

    public String getHanghoaID() {
        return hanghoaID;
    }
}
