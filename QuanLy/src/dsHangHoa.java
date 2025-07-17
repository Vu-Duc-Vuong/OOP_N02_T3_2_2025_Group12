import java.util.ArrayList;
public class dsHangHoa {
    private ArrayList<HangHoa> danhSach;

    public dsHangHoa() {
        danhSach = new ArrayList<>();
    }

    public void themHangHoa(HangHoa h) {
        danhSach.add(h);
    }

    public void hienThiTatCa() {
        System.out.println("DANH SACH HANG HOA:");
        for (HangHoa h : danhSach) {
            h.hienThiThongTin();
        }
    }

    public void tangSoLuongTheoMa(String ma, int sl) {
        for (HangHoa h : danhSach) {
            if (h.getHanghoaID().equals(ma)) {
                h.tangSoLuong(sl);
                return;
            }
        }
        System.out.println("Khong tim thay hang hoa co ma: " + ma);
    }

    public HangHoa timTheoMa(String ma) {
        for (HangHoa h : danhSach) {
            if (h.getHanghoaID().equalsIgnoreCase(ma)) {
                return h;
            }
        }
        return null;
    }

    public void taoHangHoaMau() {
        themHangHoa(new HangHoa("H01", "Áo phông", 7, "Việt Nam", 2022));
        themHangHoa(new HangHoa("H02", "Quần bò", 16, "Việt Nam", 2023));
        themHangHoa(new HangHoa("H03", "Giày thể thao", 23, "Bitica", 2021));
    }
}
