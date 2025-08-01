#!/bin/bash

echo "================================================"
echo "🚀 SPRING BOOT - QUẢN LÝ HÀNG HÓA GROUP10"
echo "================================================"

# Lấy đường dẫn của script
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="${SCRIPT_DIR}/gs-serving-web-content-main/complete"

echo "📁 Chuyển đến thư mục project: ${PROJECT_DIR}"
cd "${PROJECT_DIR}"

if [ ! -f "mvnw" ]; then
    echo "❌ Không tìm thấy Maven Wrapper (mvnw)"
    exit 1
fi

echo "🔧 Cấp quyền thực thi cho Maven Wrapper..."
chmod +x mvnw

echo "🚀 Khởi động ứng dụng Spring Boot..."
./mvnw spring-boot:run
