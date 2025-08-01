# 🏪 Ứng Dụng Quản Lý Cửa Hàng Tạp Hóa

## 📋 Thông Tin Dự Án

**Môn học:** Lập Trình Hướng Đối Tượng (OOP)  
**Lớp:** N02_T3_2_2025  
**Nhóm:** 12  
**Học kỳ:** 2 - Năm học 2024-2025  

### 👥 Thành Viên Nhóm

1. **Vũ Đức Vượng** (24100383)  
   - GitHub: [Vu-Duc-Vuong](https://github.com/Vu-Duc-Vuong)
   - Email: 24100383@student.hust.edu.vn
   - Vai trò: Team Leader, Backend Developer

2. **Tưởng Văn Tuyên** (24100462)  
   - GitHub: [vantuyendev](https://github.com/vantuyendev)  
   - Email: 24100462@student.hust.edu.vn
   - Vai trò: Frontend Developer, UI/UX Designer

---

## 🎯 Mô Tả Dự Án

Ứng dụng **Quản Lý Cửa Hàng Tạp Hóa** được xây dựng bằng Spring Boot Framework, cung cấp giải pháp toàn diện cho việc quản lý hoạt động kinh doanh của cửa hàng tạp hóa. Ứng dụng hỗ trợ quản lý nhập hàng, bán hàng, theo dõi tồn kho và tính toán doanh thu một cách hiệu quả.

### ✨ Tính Năng Chính

- 📦 **Quản lý nhập hàng**: Thêm, sửa, xóa phiếu nhập
- 🛒 **Quản lý bán hàng**: Xử lý các giao dịch bán hàng
- 📊 **Báo cáo doanh thu**: Thống kê theo ngày, tháng
- 🔍 **Tìm kiếm và lọc**: Tìm kiếm hàng hóa theo nhiều tiêu chí
- 💰 **Tính toán lãi/lỗ**: Phân tích hiệu quả kinh doanh
- 📱 **Giao diện responsive**: Hỗ trợ đa nền tảng

---

### 🗂️ Các Lớp Đối Tượng Chính

#### **1. 📦 Hàng Hóa (HangHoa)**
```java
public class HangHoa {
    private String hanghoaID;      // Mã hàng hóa (Primary Key)
    private String tenHangHoa;     // Tên hàng hóa
    private int soLuongHangHoa;    // Số lượng tồn kho
    private String nhaSanXuat;     // Nhà sản xuất
    private int namSanXuat;        // Năm sản xuất
    private double donGia;         // Đơn giá
}
```

#### **2. 📥 Nhập Hàng (Nhap)**
```java
public class Nhap {
    private String hanghoaID;      // Mã hàng hóa (Foreign Key)
    private int soLuongNhap;       // Số lượng nhập
    private double giaNhap;        // Giá nhập
    private LocalDate ngayNhap;    // Ngày nhập
    private String nhaCungCap;     // Nhà cung cấp
}
```

#### **3. 🛒 Bán Hàng (Ban)**
```java
public class Ban {
    private String hanghoaID;      // Mã hàng hóa (Foreign Key)
    private String tenHangHoa;     // Tên hàng hóa
    private int soLuongBan;        // Số lượng bán
    private double giaBan;         // Giá bán
    private LocalDate ngayBan;     // Ngày bán
}
```

#### **4. 💰 Doanh Thu (DoanhThu)**
```java
public class DoanhThu {
    private LocalDate ngay;           // Ngày tính doanh thu
    private double tongTienNhap;      // Tổng tiền nhập
    private double tongTienBan;       // Tổng tiền bán
    private double doanhThu;          // Doanh thu = Bán - Nhập
    private String trangThai;         // "LÃI" hoặc "LỖ"
}
```

---

## ⚙️ Các Phương Thức Chính

### 🔍 **1. Phương thức lọc hàng hóa theo tiêu chí** (Tưởng Văn Tuyên)

```java
public List<HangHoa> locHangHoaTheoTieuChi(String tieuChi, String giaTri) {
    // Lọc danh sách hàng hóa theo các tiêu chí:
    // - Mã hàng hóa, tên hàng hóa, nhà sản xuất
    // - Số lượng tồn kho, năm sản xuất
}
```

**Chức năng:** Cho phép người dùng lọc danh sách hàng hóa dựa trên nhiều tiêu chí như mã hàng hóa, tên hàng hóa, số lượng tồn kho, hoặc nhà sản xuất.

**Đầu vào:** Người dùng nhập từ bàn phím tiêu chí cần lọc (ví dụ: nhập tên hàng là "Sữa").

**Đầu ra:** Danh sách các hàng hóa thỏa mãn tiêu chí lọc được hiển thị ra màn hình.

---

### 💰 **2. Phương thức tính tổng tiền nhập hàng trong ngày** (Vũ Đức Vượng)

```java
public double getTongTienNhapTheoNgay(LocalDate ngay) {
    return danhSachNhap.stream()
        .filter(nhap -> nhap.getNgayNhap().equals(ngay))
        .mapToDouble(nhap -> nhap.getSoLuongNhap() * nhap.getGiaNhap())
        .sum();
}
```

**Chức năng:** Tính tổng số tiền đã nhập hàng trong ngày hiện tại, dựa trên các phiếu nhập đã lưu.

**Đầu vào:** Danh sách các phiếu nhập có ngày nhập bằng ngày hiện tại.

**Đầu ra:** Tổng số tiền nhập (tổng = tổng của (số lượng nhập × đơn giá) của từng mặt hàng trong phiếu nhập).

---

### 📊 **3. Phương thức tính doanh thu và xác định lãi/lỗ trong ngày**

```java
public DoanhThu tinhDoanhThuNgay(LocalDate ngay) {
    double tongTienNhap = getTongTienNhapTheoNgay(ngay);
    double tongTienBan = getTongTienBanTheoNgay(ngay);
    double doanhThu = tongTienBan - tongTienNhap;
    
    return new DoanhThu(ngay, tongTienNhap, tongTienBan, doanhThu,
                       doanhThu >= 0 ? "LÃI" : "LỖ");
}
```

**Chức năng:** Tính tổng tiền bán và tiền nhập trong ngày. Từ đó xác định lãi (tổng bán > tổng nhập) hoặc lỗ (tổng bán < tổng nhập).

**Đầu vào:** Danh sách các phiếu nhập và phiếu bán có ngày thực hiện là ngày hiện tại.

**Đầu ra:** 
- Tổng tiền nhập
- Tổng tiền bán  
- Trạng thái lãi/lỗ
- Mức độ lãi/lỗ

---

## 🎯 Kết Quả Đạt Được

### ✅ Các Tính Năng Hoàn Thành

1. **✅ Nhập Hàng (Nhap)**
   (Vũ Đức Vượng)
