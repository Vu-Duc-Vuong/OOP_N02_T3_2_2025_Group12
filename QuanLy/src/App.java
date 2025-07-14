import java.util.ArrayList;
import java.util.Scanner;
import java.time.LocalDate;

public class Main {
  public static void main(String[] args) {
    ArrayList<HangHoa> dsHangHoa = new ArrayList<>();
    ArrayList<Nhap> dsNhap = new ArrayList<>();
    Scanner sc = new Scanner(System.in);

    dsHangHoa.add(HangHoa.taoHangHoaMau());

    System.out.println("=== Nhap hang ===");
    System.out.print("Nhap ma hang hoa: ");
    String maHH = sc.nextLine();

    HangHoa tim = null;
    for (HangHoa h : dsHangHoa) {
      if (h.getHanghoaID().equalsIgnoreCase(maHH)) {
        tim = h;
        break;
      }
    }

    if (tim != null) {
      System.out.print("Nhap so luong: ");
      int sl = Integer.parseInt(sc.nextLine());
      System.out.print("Nhap gia nhap: ");
      double gia = Double.parseDouble(sc.nextLine());

      Nhap n = new Nhap(maHH, sl, gia, LocalDate.now());
      dsNhap.add(n);
      tim.tangSoLuong(sl);

      System.out.println("Da nhap hang thanh cong!");
      n.hienThiNhap();
      tim.hienThiThongTin();
    } else {
      System.out.println("Khong tim thay hang hoa!");
    }
  }
}
