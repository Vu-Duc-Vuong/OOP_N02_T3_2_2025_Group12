public class TestHangHoa {
    public static void main(String[] args) {
        HangHoa h = HangHoa.taoHangHoaMau();
        h.hienThiThongTin();
        h.tangSoLuong(20);
        System.out.println("Sau khi nhap them:");
        h.hienThiThongTin();
    }
}
