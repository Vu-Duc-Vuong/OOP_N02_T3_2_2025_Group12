#!/bin/bash

echo "================================================"
echo "🚀 SPRING BOOT GUI - QUẢN LÝ HÀNG HÓA"
echo "================================================"
echo ""

# Kiểm tra Java version
echo "📋 Kiểm tra Java version..."
java -version

if [ $? -ne 0 ]; then
    echo "❌ Java không được cài đặt hoặc không trong PATH"
    echo "💡 Vui lòng cài đặt Java 11 trở lên"
    exit 1
fi

echo ""
echo "📁 Chuyển đến thư mục project..."
cd gs-serving-web-content-main/complete

if [ ! -d "$(pwd)" ]; then
    echo "❌ Không tìm thấy thư mục project"
    exit 1
fi

echo "✅ Đã vào thư mục: $(pwd)"
echo ""

# Cho quyền thực thi cho mvnw
echo "🔧 Cấp quyền thực thi cho Maven Wrapper..."
chmod +x mvnw

echo ""
echo "🔨 Đang build và khởi động ứng dụng..."
echo "⏳ Vui lòng đợi..."
echo ""

# Chạy ứng dụng
./mvnw spring-boot:run

echo ""
echo "👋 Ứng dụng đã dừng!"
