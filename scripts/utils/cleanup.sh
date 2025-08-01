#!/bin/bash

echo "================================================"
echo "🧹 CLEANUP - DỌN DẸP PROJECT"
echo "================================================"
echo ""

# Function để xác nhận
confirm() {
    read -p "$1 (y/N): " -n 1 -r
    echo
    if [[ $REPLY =~ ^[Yy]$ ]]; then
        return 0
    else
        return 1
    fi
}

# Dọn dẹp target directory
cleanup_target() {
    echo "🗂️  Dọn dẹp thư mục target..."
    
    # Spring Boot complete
    if [ -d "../../gs-serving-web-content-main/complete/target" ]; then
        if confirm "Xóa ../../gs-serving-web-content-main/complete/target?"; then
            rm -rf ../../gs-serving-web-content-main/complete/target
            echo "✅ Đã xóa complete/target"
        fi
    fi
    
    # Spring Boot initial
    if [ -d "../../gs-serving-web-content-main/initial/target" ]; then
        if confirm "Xóa ../../gs-serving-web-content-main/initial/target?"; then
            rm -rf ../../gs-serving-web-content-main/initial/target
            echo "✅ Đã xóa initial/target"
        fi
    fi
}

# Dọn dẹp class files
cleanup_class_files() {
    echo "📝 Dọn dẹp các file .class..."
    
    if confirm "Xóa tất cả file .class trong ../../QuanLy/?"; then
        find ../../QuanLy/ -name "*.class" -type f -delete 2>/dev/null
        echo "✅ Đã xóa các file .class trong QuanLy/"
    fi
    
    if confirm "Xóa tất cả file .class trong ../../test/?"; then
        find ../../test/ -name "*.class" -type f -delete 2>/dev/null
        echo "✅ Đã xóa các file .class trong test/"
    fi
}

# Dọn dẹp logs
cleanup_logs() {
    echo "📋 Dọn dẹp log files..."
    
    if confirm "Xóa các file log (*.log)?"; then
        find . -name "*.log" -type f -delete 2>/dev/null
        echo "✅ Đã xóa các file log"
    fi
}

# Dọn dẹp temporary files
cleanup_temp() {
    echo "🗄️  Dọn dẹp temporary files..."
    
    if confirm "Xóa các file tạm (*.tmp, *~, .DS_Store)?"; then
        find . -name "*.tmp" -type f -delete 2>/dev/null
        find . -name "*~" -type f -delete 2>/dev/null
        find . -name ".DS_Store" -type f -delete 2>/dev/null
        echo "✅ Đã xóa các file tạm"
    fi
}

# Dọn dẹp Maven cache local
cleanup_maven_cache() {
    echo "📦 Dọn dẹp Maven cache..."
    
    if confirm "Xóa Maven local repository cache (.m2/repository)?"; then
        if [ -d "$HOME/.m2/repository" ]; then
            rm -rf "$HOME/.m2/repository"
            echo "✅ Đã xóa Maven cache"
        else
            echo "ℹ️  Maven cache không tồn tại"
        fi
    fi
}

# Menu chính
main_menu() {
    echo "Chọn loại cleanup:"
    echo "1. Dọn dẹp target directories"
    echo "2. Dọn dẹp class files"
    echo "3. Dọn dẹp log files"
    echo "4. Dọn dẹp temporary files"
    echo "5. Dọn dẹp Maven cache"
    echo "6. Dọn dẹp tất cả"
    echo "0. Thoát"
    echo ""
    
    read -p "Nhập lựa chọn (0-6): " choice
    
    case $choice in
        1) cleanup_target ;;
        2) cleanup_class_files ;;
        3) cleanup_logs ;;
        4) cleanup_temp ;;
        5) cleanup_maven_cache ;;
        6) 
            cleanup_target
            cleanup_class_files
            cleanup_logs
            cleanup_temp
            ;;
        0) 
            echo "👋 Thoát cleanup"
            exit 0
            ;;
        *) 
            echo "❌ Lựa chọn không hợp lệ"
            main_menu
            ;;
    esac
}

# Chạy menu chính
main_menu

echo ""
echo "✨ Cleanup hoàn tất!"
echo ""
