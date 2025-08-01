# 🚀 Shell Commands Guide

Hệ thống shell scripts tiện ích cho Project Quản Lý Hàng Hóa - Group10.

## 📋 Danh Sách Scripts

### 🎯 Scripts Chính
- **`./project.sh`** - Script master quản lý tất cả các lệnh (khuyến nghị sử dụng)
- **`./run.sh`** - Chạy ứng dụng nhanh
- **`./start.sh`** - Wrapper cho start script (tương thích ngược)

### � Scripts Theo Thư Mục
- **`scripts/core/`** - Scripts cốt lõi (start, build, test)
- **`scripts/dev/`** - Development tools 
- **`scripts/utils/`** - Tiện ích (cleanup, help, check, quick-start)

## 🎮 Cách Sử Dụng

### Cách 1: Sử dụng Script Master (Khuyến nghị)
```bash
./project.sh           # Vào interactive mode
./project.sh start     # Chạy lệnh trực tiếp
./project.sh menu      # Xem menu
```

### Cách 2: Chạy Nhanh
```bash
./run.sh               # Chạy app nhanh nhất
./start.sh             # Wrapper tương thích
```

### Cách 3: Scripts Chuyên Biệt
```bash
./scripts/core/build.sh all      # Build tất cả
./scripts/dev/dev.sh             # Development tools
./scripts/utils/cleanup.sh       # Dọn dẹp
./scripts/utils/help.sh commands # Xem danh sách lệnh
```

## ⚡ Quick Start cho Người Mới

```bash
# 1. Setup và khởi động nhanh
./scripts/utils/quick-start.sh

# 2. Hoặc chạy trực tiếp
./run.sh               # Cách nhanh nhất

# 3. Hoặc từng bước
./scripts/utils/check.sh       # Kiểm tra hệ thống
./scripts/core/build.sh all    # Build project  
./project.sh start             # Khởi động app
```

## 🎯 Interactive Mode

Chạy `./project.sh` để vào chế độ tương tác:

```
project> start         # Khởi động app
project> build         # Build project
project> test          # Chạy tests
project> clean         # Dọn dẹp
project> help          # Xem hướng dẫn
project> status        # Xem trạng thái
project> exit          # Thoát
```

## 📖 Chi Tiết Từng Script

### 🏗️ build.sh
```bash
./build.sh              # Menu tương tác
./build.sh spring       # Build Spring Boot
./build.sh quanly       # Build QuanLy module
./build.sh test         # Build test files
./build.sh all          # Build tất cả
./build.sh summary      # Xem build summary
```

### 🧪 test.sh
```bash
./test.sh               # Menu tương tác  
./test.sh spring        # Test Spring Boot
./test.sh quanly        # Test QuanLy module
./test.sh all           # Test tất cả
./test.sh quick         # Test nhanh
./test.sh coverage      # Xem test coverage
```

### 🔍 check.sh
```bash
./check.sh              # Kiểm tra tổng quan
./check.sh java         # Kiểm tra Java
./check.sh maven        # Kiểm tra Maven
./check.sh structure    # Kiểm tra cấu trúc project
./check.sh ports        # Kiểm tra network ports
./check.sh all          # Kiểm tra tất cả
```

### 🧹 cleanup.sh
```bash
./cleanup.sh            # Menu tương tác
# Các tùy chọn: target dirs, class files, logs, temp files, maven cache
```

### 🔧 dev.sh
```bash
./dev.sh                # Menu tương tác
./dev.sh watch          # Theo dõi file changes
./dev.sh auto-rebuild   # Tự động rebuild
./dev.sh dev-server     # Development server với hot reload
./dev.sh quality        # Code quality check
./dev.sh docs           # Generate documentation
./dev.sh performance    # Performance test
```

### 📖 help.sh
```bash
./help.sh               # Menu tương tác
./help.sh commands      # Danh sách lệnh
./help.sh structure     # Cấu trúc project
./help.sh requirements  # Yêu cầu hệ thống
./help.sh usage         # Cách sử dụng
./help.sh troubleshooting # Khắc phục sự cố
./help.sh all           # Hiển thị tất cả
```

## 🌐 Truy Cập Ứng Dụng

Sau khi khởi động thành công:
- **Trang chủ**: http://localhost:8080
- **Greeting**: http://localhost:8080/greeting

## 🔧 Yêu Cầu Hệ Thống

- **Java**: 11+
- **Maven**: 3.6+ (hoặc sử dụng Maven Wrapper có sẵn)
- **OS**: Windows 10+, macOS 10.14+, Ubuntu 18.04+
- **RAM**: 4GB+ khuyến nghị
- **Disk**: 1GB+ free space

## 🩺 Troubleshooting

### Lỗi Permission Denied
```bash
chmod +x *.sh          # Cấp quyền thực thi
```

### Port 8080 đã được sử dụng
```bash
./check.sh ports       # Kiểm tra port
# Dừng service đang dùng port 8080
```

### Java không tìm thấy
```bash
./check.sh java        # Kiểm tra Java
# Cài đặt Java từ: https://adoptium.net/
```

### Build thất bại
```bash
./cleanup.sh           # Dọn dẹp
./build.sh all         # Build lại
```

## 💡 Tips & Tricks

1. **Sử dụng tab completion**: Nhiều scripts hỗ trợ tham số
2. **Interactive mode**: Dùng `./project.sh` cho trải nghiệm tốt nhất
3. **Quick commands**: Dùng alias ngắn như `s` thay vì `start`
4. **Development mode**: Dùng `./dev.sh dev-server` cho hot reload
5. **Auto rebuild**: Dùng `./dev.sh auto-rebuild` khi development

## 📞 Hỗ Trợ

- **Team**: Group10 - OOP_N02_T3_2_2025
- **Help**: `./help.sh`
- **Check status**: `./project.sh status`
- **Documentation**: Xem các file README.md khác trong project

---

**Happy Coding! 🎉**
