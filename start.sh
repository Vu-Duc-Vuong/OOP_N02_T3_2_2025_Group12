#!/bin/bash

echo "================================================"
echo "🚀 SPRING BOOT - QUẢN LÝ HÀNG HÓA GROUP10"
echo "================================================"



echo "📁 Chuyển đến thư mục project: k18/gs-serving-web-content-main/initial"
cd k18/gs-serving-web-content-main/initial

# Nạp biến môi trường DB_URL
if [ -f "setenv.sh" ]; then
    echo "🔑 Nạp biến môi trường từ setenv.sh"
    source setenv.sh
fi

if [ ! -f "mvnw" ]; then
    echo "❌ Không tìm thấy Maven Wrapper (mvnw)"
    exit 1
fi

echo "🔧 Cấp quyền thực thi cho Maven Wrapper..."
chmod +x mvnw

echo "🚀 Khởi động ứng dụng Spring Boot..."
./mvnw spring-boot:run
