# OOP_N02_Group10

## Nhóm làm việc số 10

### Thành viên

1. **Vũ Đức Vượng** (24100383)  
   GitHub ID: [Vu-Duc-Vuong](https://github.com/Vu-Duc-Vuong)

2. **Tưởng Văn Tuyên** (24100462)  
   GitHub ID: [vantuyendev](https://github.com/vantuyendev)

---

## Ứng dụng: Xây dựng quản lý cửa hàng tạp hóa


### **Phân tích đối tượng**

---

#### **1. Hàng hóa (Hanghoa)**
- **hanghoaID**: Mã hàng hóa
- **tenHangHoa**: Tên hàng hóa
- **soLuongHangHoa**: Số lượng hàng hóa
- **nhaSanXuat**: Nhà sản xuất
- **namSanXuat**: Năm sản xuất

---

#### **2. Nhập hàng (Nhap)**
- **hanghoaID**: Mã hàng hóa
- **soLuongNhap**: Số lượng nhập
- **giaNhap**: Giá nhập
- **ngayNhap**: Ngày nhập

---

#### **3. Bán hàng (Ban)**
- **hanghoaID**: Mã hàng hóa
- **tenHangHoa**: Tên hàng hóa
- **soLuongBan**: Số lượng bán
- **giaBan**: Giá bán


#### **4. Doanh thu (Doanhthu)**
- **trongNgay**: Doanh thu trong ngày
- **trongThang**: Doanh thu trong tháng
- **lai**: Lãi
- **lo**: Lỗ

### **Phương thức chính**

1. Phương thức lọc hàng hóa theo tiêu chí
•	Chức năng: Cho phép người dùng lọc danh sách hàng hóa dựa trên nhiều tiêu chí như: mã hàng hóa, tên hàng hóa, số lượng tồn kho, hoặc nhà sản xuất.

•	Đầu vào: Người dùng nhập từ bàn phím tiêu chí cần lọc (ví dụ: nhập tên hàng là "Sữa").

•	Đầu ra: Danh sách các hàng hóa thỏa mãn tiêu chí lọc sẽ được hiển thị ra màn hình.
________________________________________
 2. Phương thức tính tổng tiền nhập hàng trong ngày (Vũ Đức Vượng)

•	Chức năng: Tính tổng số tiền đã nhập hàng trong ngày hiện tại, dựa trên các phiếu nhập đã lưu.

•	Đầu vào: Danh sách các phiếu nhập có ngày nhập bằng ngày hiện tại.

•	Đầu ra: In ra tổng số tiền nhập (tổng =  số lượng * giá nhập của từng mặt hàng trong phiếu nhập).
________________________________________
 3. Phương thức tính doanh thu và xác định lãi/lỗ trong ngày

•	Chức năng: Tính tổng tiền bán và tiền nhập trong ngày. Từ đó xác định lãi (tổng bán > tổng nhập) hoặc lỗ (tổng bán < tổng nhập).

•	Đầu vào: Danh sách các phiếu nhập và phiếu bán có ngày thực hiện là ngày hiện tại.

•	Đầu ra:
o	Tổng tiền nhập.
o	Tổng tiền bán.
