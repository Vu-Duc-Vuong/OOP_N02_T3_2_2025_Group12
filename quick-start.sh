#!/bin/bash

echo "🚀 Quick Start - Spring Boot Application"
echo "========================================="

# Chuyển đến thư mục Spring Boot
cd "$(dirname "$0")/gs-serving-web-content-main/complete"

# Cấp quyền và chạy
chmod +x mvnw
echo "📦 Starting application on http://localhost:8080"
./mvnw spring-boot:run
