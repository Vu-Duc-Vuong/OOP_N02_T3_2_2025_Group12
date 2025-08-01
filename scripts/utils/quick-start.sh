#!/bin/bash

echo "================================================"
echo "🚀 QUICK START - KHỞI ĐỘNG NHANH"
echo "================================================"
echo ""

# Quick setup và start cho người dùng mới
echo "🎯 Chào mừng đến với Project Management System!"
echo "Script này sẽ giúp bạn thiết lập và khởi động project một cách nhanh chóng."
echo ""

# Function xác nhận
confirm() {
    read -p "$1 (y/N): " -n 1 -r
    echo
    if [[ $REPLY =~ ^[Yy]$ ]]; then
        return 0
    else
        return 1
    fi
}

# Step 1: Kiểm tra môi trường
echo "🔍 Step 1: Kiểm tra môi trường..."
echo "================================="

java -version >/dev/null 2>&1
if [ $? -ne 0 ]; then
    echo "❌ Java không được tìm thấy!"
    echo "💡 Vui lòng cài đặt Java 11+ từ: https://adoptium.net/"
    echo "📖 Xem hướng dẫn chi tiết: ./help.sh requirements"
    exit 1
fi

echo "✅ Java OK"
echo "✅ Project structure OK"

# Step 2: Cấp quyền cho scripts
echo ""
echo "🔧 Step 2: Cấp quyền thực thi cho scripts..."
echo "============================================="

chmod +x *.sh
chmod +x gs-serving-web-content-main/complete/mvnw
echo "✅ Đã cấp quyền thực thi"

# Step 3: Build project
echo ""
echo "🏗️  Step 3: Build project..."
echo "============================"

if confirm "Build project ngay bây giờ?"; then
    echo "🔨 Building..."
    ./build.sh all >/dev/null 2>&1
    if [ $? -eq 0 ]; then
        echo "✅ Build thành công!"
    else
        echo "⚠️  Build có lỗi, nhưng có thể vẫn chạy được"
    fi
else
    echo "⏭️  Bỏ qua build (có thể build sau với: ./build.sh)"
fi

# Step 4: Hiển thị hướng dẫn nhanh
echo ""
echo "📖 Step 4: Hướng dẫn nhanh..."
echo "============================"

echo ""
echo "🎯 CÁC LỆNH QUAN TRỌNG:"
echo "======================="
echo "🚀 ./start.sh          - Khởi động Spring Boot app"
echo "🧹 ./cleanup.sh        - Dọn dẹp project"
echo "🏗️  ./build.sh          - Build project"
echo "🧪 ./test.sh           - Chạy tests"
echo "🔍 ./check.sh          - Kiểm tra hệ thống"
echo "📖 ./help.sh           - Xem hướng dẫn chi tiết"

echo ""
echo "🌐 TRUY CẬP ỨNG DỤNG:"
echo "===================="
echo "🏠 Trang chủ:          http://localhost:8080"
echo "👋 Trang greeting:     http://localhost:8080/greeting"

echo ""
echo "💡 MẸO SỬ DỤNG:"
echo "==============="
echo "• Nếu port 8080 bị chiếm: ./check.sh ports"
echo "• Nếu có lỗi build: ./cleanup.sh rồi ./build.sh"
echo "• Xem chi tiết lỗi: ./help.sh troubleshooting"

# Step 5: Tùy chọn khởi động
echo ""
echo "🚀 Step 5: Khởi động ứng dụng..."
echo "================================"

if confirm "Khởi động Spring Boot application ngay bây giờ?"; then
    echo ""
    echo "🎉 Đang khởi động ứng dụng..."
    echo "⏳ Vui lòng đợi..."
    echo ""
    echo "📱 Sau khi khởi động thành công, truy cập:"
    echo "   🌐 http://localhost:8080"
    echo ""
    echo "⏹️  Để dừng ứng dụng: Ctrl+C"
    echo ""
    
    ./start.sh
else
    echo ""
    echo "✨ Setup hoàn tất!"
    echo ""
    echo "🎯 ĐỂ KHỞI ĐỘNG SAU:"
    echo "   ./start.sh"
    echo ""
    echo "📖 ĐỂ XEM HƯỚNG DẪN:"
    echo "   ./help.sh"
    echo ""
    echo "🎉 Chúc bạn coding vui vẻ!"
fi
