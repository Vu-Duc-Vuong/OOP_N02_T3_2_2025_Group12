# HƯỚNG DẪN CHẠY PROJECT - SPRING BOOT GUI QUẢN LÝ HÀNG HÓA

## 📋 Mô tả dự án
Ứng dụng Spring Boot Web GUI để quản lý Hàng Hóa với đầy đủ chức năng CRUD (Create, Read, Update, Delete), tìm kiếm và giao diện responsive sử dụng Bootstrap.

## 🛠️ Công nghệ sử dụng
- **Spring Boot 2.7.17** (tương thích với Java 11)
- **Spring Web MVC** - Framework web
- **Thymeleaf** - Template engine
- **Bootstrap 5.1.3** - CSS Framework
- **Font Awesome 6.0.0** - Icons
- **Maven** - Build tool
- **Tomcat Embedded** - Web server

## 📋 Yêu cầu hệ thống
- **Java 11** hoặc cao hơn
- **Maven 3.6+** (hoặc sử dụng Maven Wrapper có sẵn)
- **Web browser** hiện đại (Chrome, Firefox, Safari, Edge)

## 📁 Cấu trúc dự án
```
gs-serving-web-content-main/complete/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/servingwebcontent/
│   │   │       ├── ServingWebContentApplication.java    # Main class
│   │   │       ├── controller/
│   │   │       │   └── HangHoaController.java          # Web Controller
│   │   │       ├── model/
│   │   │       │   └── HangHoa.java                    # Model class
│   │   │       └── service/
│   │   │           └── HangHoaService.java             # Business Logic
│   │   └── resources/
│   │       ├── static/
│   │       │   └── index.html                          # Trang chủ
│   │       └── templates/
│   │           ├── hanghoa/
│   │           │   ├── list.html                       # Danh sách hàng hóa
│   │           │   ├── form.html                       # Form thêm/sửa
│   │           │   ├── detail.html                     # Chi tiết sản phẩm
│   │           │   └── search.html                     # Tìm kiếm
│   │           └── greeting.html                       # Template mẫu
├── pom.xml                                             # Maven configuration
└── target/                                             # Build output
```

## 🚀 Hướng dẫn chạy ứng dụng

### Phương pháp 1: Sử dụng Maven Wrapper (Khuyến nghị)

1. **Mở Terminal/Command Prompt**
2. **Điều hướng đến thư mục project:**
   ```bash
   cd gs-serving-web-content-main/complete
   ```

3. **Cho quyền thực thi cho Maven Wrapper (Linux/Mac):**
   ```bash
   chmod +x mvnw
   ```

4. **Build và chạy ứng dụng:**
   ```bash
   # Linux/Mac
   ./mvnw spring-boot:run
   
   # Windows
   mvnw.cmd spring-boot:run
   ```

### Phương pháp 2: Build JAR file và chạy

1. **Build project:**
   ```bash
   # Linux/Mac
   ./mvnw clean package -DskipTests
   
   # Windows
   mvnw.cmd clean package -DskipTests
   ```

2. **Chạy JAR file:**
   ```bash
   java -jar target/serving-web-content-complete-0.0.1-SNAPSHOT.jar
   ```

### Phương pháp 3: Sử dụng Maven cài đặt sẵn

```bash
mvn spring-boot:run
```

## 🌐 Truy cập ứng dụng

Sau khi khởi động thành công, ứng dụng sẽ chạy trên:
- **URL:** http://localhost:8080
- **Port:** 8080 (mặc định)

### Các endpoint chính:
- **Trang chủ:** http://localhost:8080/
- **Danh sách hàng hóa:** http://localhost:8080/hanghoa
- **Thêm hàng hóa mới:** http://localhost:8080/hanghoa/new
- **Tìm kiếm:** http://localhost:8080/hanghoa/search

## 📊 Chức năng chính

### 1. Quản lý Hàng Hóa
- ✅ **Xem danh sách** tất cả hàng hóa
- ✅ **Thêm hàng hóa mới** với validation
- ✅ **Sửa thông tin** hàng hóa
- ✅ **Xóa hàng hóa**
- ✅ **Xem chi tiết** từng sản phẩm

### 2. Tìm kiếm nâng cao
- 🔍 Tìm theo **tên hàng**
- 🔍 Tìm theo **nhà sản xuất**
- 🔍 Tìm theo **năm sản xuất**
- 🔍 Lọc theo **khoảng giá**

### 3. Giao diện người dùng
- 📱 **Responsive design** - tương thích mọi thiết bị
- 🎨 **Bootstrap UI** - giao diện đẹp, hiện đại
- ⚡ **Fast loading** - tốc độ tải nhanh
- 🔔 **Thông báo** thành công/lỗi

## 🔧 Cấu hình và tùy chỉnh

### Thay đổi port (tùy chọn)
Tạo file `application.properties` trong `src/main/resources/`:
```properties
server.port=9090
```

### Thay đổi context path
```properties
server.servlet.context-path=/hanghoa-app
```

## 📝 Dữ liệu mẫu

Ứng dụng được khởi tạo với dữ liệu mẫu gồm 10 sản phẩm:
- iPhone 15 Pro Max
- Samsung Galaxy S24 Ultra
- MacBook Pro M3
- Dell XPS 13
- Coca Cola
- Pepsi Cola
- Toyota Camry 2024
- Honda Civic 2024
- Nike Air Max
- Adidas Ultraboost

## 🚨 Xử lý sự cố

### Lỗi port đã được sử dụng
```
Port 8080 was already in use
```
**Giải pháp:**
- Tắt ứng dụng đang chạy trên port 8080
- Hoặc đổi port trong `application.properties`

### Lỗi Java version
```
java.lang.UnsupportedClassVersionError
```
**Giải pháp:**
- Đảm bảo đang sử dụng Java 11 trở lên
- Kiểm tra: `java -version`

### Lỗi Maven command not found
**Giải pháp:**
- Sử dụng Maven Wrapper: `./mvnw` thay vì `mvn`
- Hoặc cài đặt Maven: https://maven.apache.org/install.html

### Lỗi Permission denied (Linux/Mac)
```bash
chmod +x mvnw
```

## 📞 Hỗ trợ kỹ thuật

### Kiểm tra logs
```bash
# Xem logs trong terminal khi chạy
./mvnw spring-boot:run

# Hoặc xem logs từ JAR file
java -jar target/serving-web-content-complete-0.0.1-SNAPSHOT.jar
```

### Thông tin phiên bản
- Spring Boot: 2.7.17
- Java Target: 11
- Maven: 3.6+

## 🎯 Hướng dẫn sử dụng

1. **Khởi động ứng dụng** theo một trong các phương pháp trên
2. **Mở trình duyệt** và truy cập http://localhost:8080
3. **Nhấp "Quản lý Hàng Hóa"** để bắt đầu
4. **Thêm/Sửa/Xóa** hàng hóa theo nhu cầu
5. **Sử dụng tìm kiếm** để lọc dữ liệu

## 💡 Tips và Tricks

- **Ctrl+C** để dừng ứng dụng
- Sử dụng **F12** để mở Developer Tools
- **Refresh** trang sau khi thay đổi dữ liệu
- Kiểm tra **console logs** nếu có lỗi

## 📋 TODO và mở rộng

- [ ] Thêm database persistent (MySQL/PostgreSQL)
- [ ] Thêm authentication/authorization
- [ ] Export dữ liệu ra Excel/PDF
- [ ] API REST cho mobile app
- [ ] Unit tests và integration tests

---

**📧 Liên hệ:** Nếu có thắc mắc, vui lòng tạo issue trong repository này.

**🌟 Đánh giá:** Nếu project hữu ích, hãy cho một ⭐ star!
