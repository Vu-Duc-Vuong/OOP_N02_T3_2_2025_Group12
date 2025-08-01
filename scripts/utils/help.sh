#!/bin/bash

echo "================================================"
echo "📖 HELP - HƯỚNG DẪN SỬ DỤNG PROJECT"
echo "================================================"
echo ""

show_commands() {
    echo "🚀 CÁC LỆNH CHÍNH:"
    echo "=================="
    echo ""
    echo "📱 Khởi động ứng dụng:"
    echo "  ./start.sh              - Khởi động Spring Boot app (Linux/Mac)"
    echo "  start.bat               - Khởi động Spring Boot app (Windows)"
    echo ""
    echo "🧹 Dọn dẹp project:"
    echo "  ./cleanup.sh            - Dọn dẹp files tạm, build artifacts"
    echo ""
    echo "🔧 Build & Test:"
    echo "  ./build.sh              - Build project"
    echo "  ./test.sh               - Chạy tests"
    echo ""
    echo "📊 Kiểm tra hệ thống:"
    echo "  ./check.sh              - Kiểm tra môi trường & dependencies"
    echo ""
    echo "📖 Trợ giúp:"
    echo "  ./help.sh               - Hiển thị hướng dẫn này"
    echo ""
}

show_project_structure() {
    echo "📁 CẤU TRÚC PROJECT:"
    echo "==================="
    echo ""
    echo "📂 gs-serving-web-content-main/"
    echo "  ├── complete/           - Spring Boot application hoàn chỉnh"
    echo "  │   ├── src/main/java/  - Java source code"
    echo "  │   ├── src/main/resources/ - Resources (HTML, CSS, etc.)"
    echo "  │   └── pom.xml         - Maven configuration"
    echo "  └── initial/            - Template khởi tạo"
    echo ""
    echo "📂 QuanLy/"
    echo "  ├── src/                - Quản lý hàng hóa (Java thuần)"
    echo "  └── Model/              - Data models"
    echo ""
    echo "📂 test/"
    echo "  └── *.java              - Test cases"
    echo ""
}

show_requirements() {
    echo "⚙️  YÊU CẦU HỆ THỐNG:"
    echo "==================="
    echo ""
    echo "☕ Java:"
    echo "  - Java 11 hoặc cao hơn"
    echo "  - Thiết lập JAVA_HOME"
    echo ""
    echo "🔨 Build Tools:"
    echo "  - Maven 3.6+ (hoặc sử dụng Maven Wrapper có sẵn)"
    echo ""
    echo "🌐 Web Browser:"
    echo "  - Chrome, Firefox, Safari, Edge (modern browsers)"
    echo ""
    echo "💻 OS Support:"
    echo "  - Windows 10+"
    echo "  - macOS 10.14+"
    echo "  - Ubuntu 18.04+"
    echo ""
}

show_usage() {
    echo "🎯 CÁCH SỬ DỤNG:"
    echo "==============="
    echo ""
    echo "1️⃣  Khởi động lần đầu:"
    echo "   ./check.sh              # Kiểm tra môi trường"
    echo "   ./start.sh              # Khởi động ứng dụng"
    echo ""
    echo "2️⃣  Truy cập ứng dụng:"
    echo "   http://localhost:8080   # Trang chủ"
    echo "   http://localhost:8080/greeting # Trang greeting"
    echo ""
    echo "3️⃣  Development:"
    echo "   ./test.sh               # Chạy tests trước khi code"
    echo "   # Edit code..."
    echo "   ./build.sh              # Build lại"
    echo "   ./start.sh              # Test ứng dụng"
    echo ""
    echo "4️⃣  Dọn dẹp:"
    echo "   ./cleanup.sh            # Khi cần dọn dẹp"
    echo ""
}

show_troubleshooting() {
    echo "🩺 TROUBLESHOOTING:"
    echo "=================="
    echo ""
    echo "❌ Java not found:"
    echo "   - Cài đặt OpenJDK 11+ từ: https://adoptium.net/"
    echo "   - Thiết lập JAVA_HOME environment variable"
    echo ""
    echo "❌ Port 8080 đã được sử dụng:"
    echo "   - Dừng process đang dùng port 8080"
    echo "   - Hoặc thay đổi port trong application.properties"
    echo ""
    echo "❌ Maven build failed:"
    echo "   - Chạy: ./cleanup.sh để dọn dẹp"
    echo "   - Kiểm tra internet connection"
    echo "   - Chạy lại: ./build.sh"
    echo ""
    echo "❌ Permission denied:"
    echo "   - Linux/Mac: chmod +x *.sh"
    echo "   - Windows: Chạy với Run as Administrator"
    echo ""
}

show_contacts() {
    echo "📞 LIÊN HỆ & HỖ TRỢ:"
    echo "==================="
    echo ""
    echo "👥 Team: Group10 - OOP_N02_T3_2_2025"
    echo "📧 Báo lỗi: Tạo issue trên GitHub repository"
    echo "📚 Tài liệu: Xem README.md và các file hướng dẫn"
    echo ""
    echo "🔗 Links:"
    echo "   - Spring Boot Guide: README_SPRING_BOOT.md"
    echo "   - Quick Start: QUICK_START.md"
    echo "   - Hướng dẫn chạy: HUONG_DAN_CHAY_PROJECT.md"
    echo ""
}

# Menu chính
main_menu() {
    echo "Chọn phần trợ giúp:"
    echo "1. Các lệnh chính"
    echo "2. Cấu trúc project"
    echo "3. Yêu cầu hệ thống"
    echo "4. Cách sử dụng"
    echo "5. Troubleshooting"
    echo "6. Liên hệ & Hỗ trợ"
    echo "7. Hiển thị tất cả"
    echo "0. Thoát"
    echo ""
    
    read -p "Nhập lựa chọn (0-7): " choice
    
    case $choice in
        1) show_commands ;;
        2) show_project_structure ;;
        3) show_requirements ;;
        4) show_usage ;;
        5) show_troubleshooting ;;
        6) show_contacts ;;
        7) 
            show_commands
            echo ""
            show_project_structure
            echo ""
            show_requirements
            echo ""
            show_usage
            echo ""
            show_troubleshooting
            echo ""
            show_contacts
            ;;
        0) 
            echo "👋 Thoát help"
            exit 0
            ;;
        *) 
            echo "❌ Lựa chọn không hợp lệ"
            main_menu
            ;;
    esac
    
    echo ""
    echo "Press any key to continue..."
    read -n 1
    echo ""
    main_menu
}

# Nếu có tham số, hiển thị trực tiếp
if [ $# -gt 0 ]; then
    case $1 in
        "commands") show_commands ;;
        "structure") show_project_structure ;;
        "requirements") show_requirements ;;
        "usage") show_usage ;;
        "troubleshooting") show_troubleshooting ;;
        "contacts") show_contacts ;;
        "all") 
            show_commands
            echo ""
            show_project_structure
            echo ""
            show_requirements
            echo ""
            show_usage
            echo ""
            show_troubleshooting
            echo ""
            show_contacts
            ;;
        *) 
            echo "❌ Tham số không hợp lệ: $1"
            echo "Sử dụng: ./help.sh [commands|structure|requirements|usage|troubleshooting|contacts|all]"
            ;;
    esac
else
    # Chạy menu tương tác
    main_menu
fi
