#!/bin/bash

# ================================================
# 🎯 PROJECT MANAGER - QUẢN LÝ PROJECT
# ================================================
# Script chính để quản lý tất cả các công cụ project

VERSION="1.0.0"
PROJECT_NAME="Quản Lý Hàng Hóa - Group10"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
PURPLE='\033[0;35m'
CYAN='\033[0;36m'
NC='\033[0m' # No Color

# Function để hiển thị banner
show_banner() {
    echo -e "${BLUE}================================================${NC}"
    echo -e "${CYAN}🎯 $PROJECT_NAME${NC}"
    echo -e "${BLUE}================================================${NC}"
    echo -e "${YELLOW}Version: $VERSION${NC}"
    echo -e "${PURPLE}Team: Group10 - OOP_N02_T3_2_2025${NC}"
    echo ""
}

# Function để hiển thị menu chính
show_main_menu() {
    echo -e "${GREEN}📋 MENU CHÍNH${NC}"
    echo -e "${GREEN}==============${NC}"
    echo ""
    echo -e "${CYAN}🚀 KHỞI ĐỘNG & CHẠY:${NC}"
    echo "  s, start          - Khởi động Spring Boot application"
    echo "  q, quick          - Quick start (setup + khởi động nhanh)"
    echo ""
    echo -e "${CYAN}🔧 BUILD & TEST:${NC}"
    echo "  b, build          - Build project"
    echo "  t, test           - Chạy tests"
    echo "  c, check          - Kiểm tra hệ thống & môi trường"
    echo ""
    echo -e "${CYAN}🧹 BẢO TRÌ:${NC}"
    echo "  clean, cleanup    - Dọn dẹp project"
    echo "  dev               - Development tools"
    echo ""
    echo -e "${CYAN}📖 TRỢ GIÚP:${NC}"
    echo "  h, help           - Hướng dẫn sử dụng"
    echo "  v, version        - Thông tin version"
    echo "  status            - Trạng thái project"
    echo ""
    echo -e "${CYAN}🎛️  KHÁC:${NC}"
    echo "  menu              - Hiển thị menu này"
    echo "  exit, quit        - Thoát"
    echo ""
}

# Function để hiển thị status
show_status() {
    echo -e "${GREEN}📊 TRẠNG THÁI PROJECT${NC}"
    echo -e "${GREEN}====================${NC}"
    echo ""
    
    # Java status
    if java -version >/dev/null 2>&1; then
        echo -e "☕ Java: ${GREEN}✅ Available${NC}"
    else
        echo -e "☕ Java: ${RED}❌ Missing${NC}"
    fi
    
    # Build status
    if [ -f "gs-serving-web-content-main/complete/target/serving-web-content-complete-0.0.1-SNAPSHOT.jar" ]; then
        echo -e "📦 Spring Boot JAR: ${GREEN}✅ Built${NC}"
    else
        echo -e "📦 Spring Boot JAR: ${YELLOW}⚠️  Not built${NC}"
    fi
    
    # QuanLy classes
    local quanly_classes=$(find QuanLy/src -name "*.class" 2>/dev/null | wc -l)
    if [ $quanly_classes -gt 0 ]; then
        echo -e "📝 QuanLy Classes: ${GREEN}✅ $quanly_classes compiled${NC}"
    else
        echo -e "📝 QuanLy Classes: ${YELLOW}⚠️  Not compiled${NC}"
    fi
    
    # Test classes
    local test_classes=$(find test -name "*.class" 2>/dev/null | wc -l)
    if [ $test_classes -gt 0 ]; then
        echo -e "🧪 Test Classes: ${GREEN}✅ $test_classes compiled${NC}"
    else
        echo -e "🧪 Test Classes: ${YELLOW}⚠️  Not compiled${NC}"
    fi
    
    # Port status
    if netstat -tuln 2>/dev/null | grep -q ":8080 " || ss -tuln 2>/dev/null | grep -q ":8080 "; then
        echo -e "🌐 Port 8080: ${YELLOW}⚠️  In use${NC}"
    else
        echo -e "🌐 Port 8080: ${GREEN}✅ Available${NC}"
    fi
    
    echo ""
}

# Function để hiển thị help
show_help() {
    echo -e "${GREEN}📖 HƯỚNG DẪN SỬ DỤNG${NC}"
    echo -e "${GREEN}===================${NC}"
    echo ""
    echo -e "${CYAN}🚀 KHỞI ĐỘNG:${NC}"
    echo "  ./start.sh          - Khởi động ứng dụng"
    echo "  ./quick-start.sh    - Khởi động nhanh"
    echo "  ./project.sh start  - Khởi động qua menu"
    echo ""
    echo -e "${CYAN}🔧 BUILD & TEST:${NC}"
    echo "  ./project.sh build  - Build project"
    echo "  ./project.sh test   - Chạy tests"
    echo ""
    echo -e "${CYAN}🌐 TRUY CẬP:${NC}"
    echo "  URL: http://localhost:8080"
    echo ""
    echo -e "${CYAN}⚡ NHANH:${NC}"
    echo "  chmod +x *.sh && ./start.sh"
    echo ""
}

# Function để xử lý commands
handle_command() {
    local cmd="$1"
    
    case "$cmd" in
        's'|'start')
            echo -e "${GREEN}🚀 Khởi động ứng dụng...${NC}"
            ./start.sh
            ;;
                'q'|'quick')
            echo -e "${GREEN}⚡ Quick start...${NC}"
            ./quick-start.sh
            ;;
        "b"|"build")
        "q"|"quick")
            echo -e "${CYAN}⚡ Quick start...${NC}"
            ./scripts/utils/quick-start.sh
            ;;
        "b"|"build")
            echo -e "${CYAN}🏗️  Building project...${NC}"
            cd gs-serving-web-content-main/complete && ./mvnw clean package -DskipTests
            ;;
        "t"|"test")
            echo -e "${CYAN}🧪 Running tests...${NC}"
            cd gs-serving-web-content-main/complete && ./mvnw test
            ;;
        "c"|"check")
            echo -e "${CYAN}🔍 Checking system...${NC}"
            java -version
            echo ""
            echo "Maven Wrapper status:"
            ls -la gs-serving-web-content-main/complete/mvnw
            ;;
        "clean"|"cleanup")
            echo -e "${CYAN}🧹 Cleaning up...${NC}"
            cd gs-serving-web-content-main/complete && ./mvnw clean
            ;;
        "dev")
            echo -e "${CYAN}🔧 Development tools...${NC}"
            cd gs-serving-web-content-main/complete && ./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
            ;;
        "h"|"help")
            echo -e "${CYAN}📖 Help...${NC}"
            show_help
            ;;
        "v"|"version")
            echo -e "${GREEN}📋 PROJECT INFO${NC}"
            echo -e "${GREEN}===============${NC}"
            echo "Project: $PROJECT_NAME"
            echo "Version: $VERSION"
            echo "Scripts version: $(date +'%Y.%m.%d')"
            echo ""
            java -version 2>&1 | head -1
            ;;
        "status")
            show_status
            ;;
        "menu")
            show_main_menu
            ;;
        "exit"|"quit")
            echo -e "${GREEN}👋 Tạm biệt!${NC}"
            exit 0
            ;;
        "")
            # Empty command, do nothing
            ;;
        *)
            echo -e "${RED}❌ Lệnh không hợp lệ: $cmd${NC}"
            echo -e "${YELLOW}💡 Gõ 'menu' để xem danh sách lệnh${NC}"
            echo -e "${YELLOW}💡 Gõ 'help' để xem hướng dẫn chi tiết${NC}"
            ;;
    esac
}

# Interactive mode
interactive_mode() {
    echo -e "${GREEN}🎮 INTERACTIVE MODE${NC}"
    echo -e "${GREEN}===================${NC}"
    echo -e "${YELLOW}Gõ lệnh hoặc 'menu' để xem danh sách lệnh${NC}"
    echo -e "${YELLOW}Gõ 'exit' để thoát${NC}"
    echo ""
    
    while true; do
        echo -ne "${BLUE}project> ${NC}"
        read -r command
        echo ""
        handle_command "$command"
        echo ""
    done
}

# Main execution
main() {
    # Change to project directory
    cd "$(dirname "$0")"
    
    show_banner
    
    # Nếu có tham số, thực hiện lệnh trực tiếp
    if [ $# -gt 0 ]; then
        handle_command "$1"
    else
        # Hiển thị menu và vào interactive mode
        show_main_menu
        interactive_mode
    fi
}

# Chạy main function
main "$@"
