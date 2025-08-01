# 🚀 Quản Lý Hàng Hóa - Group10

## Quick Start

Chạy ứng dụng nhanh nhất:
```bash
./start.sh
```

Hoặc:
```bash
./quick-start.sh
```

## Các cách chạy khác

### 1. Sử dụng menu interactive
```bash
./project.sh
```

### 2. Chạy trực tiếp
```bash
./project.sh start
```

### 3. Chạy từ thư mục Spring Boot
```bash
cd gs-serving-web-content-main/complete
./mvnw spring-boot:run
```

## Truy cập ứng dụng

Sau khi khởi động thành công:
- URL: **http://localhost:8080**

## Build & Test

```bash
./project.sh build    # Build project
./project.sh test     # Chạy tests  
./project.sh clean    # Dọn dẹp
```

## Cấu trúc project

- `start.sh` - Script chính để chạy ứng dụng
- `quick-start.sh` - Chạy nhanh
- `project.sh` - Menu interactive với nhiều tùy chọn
- `run_server.sh` - Chạy server đơn giản
- `gs-serving-web-content-main/` - Ứng dụng Spring Boot chính
- `QuanLy/` - Module quản lý hàng hóa (Java classes)
- `test/` - Test cases

## Yêu cầu hệ thống

- Java 11 trở lên
- Maven (hoặc sử dụng Maven Wrapper có sẵn)
