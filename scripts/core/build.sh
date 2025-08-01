#!/bin/bash

echo "================================================"
echo "🔧 BUILD - XÂY DỰNG PROJECT"
echo "================================================"
echo ""

# Function kiểm tra Java
check_java() {
    echo "☕ Kiểm tra Java..."
    if java -version >/dev/null 2>&1; then
        echo "✅ Java đã được cài đặt"
        java -version
    else
        echo "❌ Java không được tìm thấy"
        echo "💡 Vui lòng cài đặt Java 11+ và thêm vào PATH"
        exit 1
    fi
}

# Build Spring Boot application
build_spring_boot() {
    echo ""
    echo "🏗️  Building Spring Boot Application..."
    echo "======================================="
    
    cd ../../gs-serving-web-content-main/complete
    
    if [ ! -f "pom.xml" ]; then
        echo "❌ Không tìm thấy pom.xml"
        exit 1
    fi
    
    echo "📁 Thư mục hiện tại: $(pwd)"
    echo "🔧 Cấp quyền thực thi cho Maven Wrapper..."
    chmod +x mvnw
    
    echo "📦 Cleaning previous build..."
    ./mvnw clean
    
    echo "🔨 Compiling and packaging..."
    ./mvnw compile package -DskipTests
    
    if [ $? -eq 0 ]; then
        echo "✅ Spring Boot build thành công!"
        echo "📦 JAR file: target/serving-web-content-complete-0.0.1-SNAPSHOT.jar"
    else
        echo "❌ Spring Boot build thất bại!"
        exit 1
    fi
    
    cd - > /dev/null
}

# Build Java QuanLy module
build_quan_ly() {
    echo ""
    echo "🏗️  Building QuanLy Module..."
    echo "============================="
    
    cd ../../QuanLy/src
    
    echo "📁 Thư mục hiện tại: $(pwd)"
    echo "🧹 Dọn dẹp các file .class cũ..."
    find . -name "*.class" -delete 2>/dev/null
    
    echo "🔨 Compiling Java files..."
    
    # Compile Model classes first
    echo "📝 Compiling Model classes..."
    javac Model/*.java
    if [ $? -ne 0 ]; then
        echo "❌ Compilation failed for Model classes"
        cd - > /dev/null
        exit 1
    fi
    
    # Compile main classes
    echo "📝 Compiling main classes..."
    javac -cp . *.java
    if [ $? -ne 0 ]; then
        echo "❌ Compilation failed for main classes"
        cd - > /dev/null
        exit 1
    fi
    
    echo "✅ QuanLy module build thành công!"
    echo "📋 Các class files đã được tạo:"
    find . -name "*.class" | sort
    
    cd - > /dev/null
}

# Build test files
build_tests() {
    echo ""
    echo "🏗️  Building Test Files..."
    echo "=========================="
    
    cd ../../test
    
    echo "📁 Thư mục hiện tại: $(pwd)"
    echo "🧹 Dọn dẹp các file .class cũ..."
    find . -name "*.class" -delete 2>/dev/null
    
    echo "🔨 Compiling test files..."
    javac -cp .:../QuanLy/src *.java
    
    if [ $? -eq 0 ]; then
        echo "✅ Test files build thành công!"
        echo "📋 Các test class files:"
        find . -name "*.class" | sort
    else
        echo "❌ Test files build thất bại!"
        cd - > /dev/null
        exit 1
    fi
    
    cd - > /dev/null
}

# Show build summary
show_summary() {
    echo ""
    echo "📊 BUILD SUMMARY"
    echo "================"
    echo ""
    
    # Spring Boot JAR
    if [ -f "gs-serving-web-content-main/complete/target/serving-web-content-complete-0.0.1-SNAPSHOT.jar" ]; then
        echo "✅ Spring Boot JAR: Có sẵn"
        ls -lh gs-serving-web-content-main/complete/target/*.jar
    else
        echo "❌ Spring Boot JAR: Không tồn tại"
    fi
    
    echo ""
    
    # QuanLy classes
    quan_ly_classes=$(find QuanLy/src -name "*.class" 2>/dev/null | wc -l)
    echo "📝 QuanLy classes: $quan_ly_classes files"
    
    # Test classes
    test_classes=$(find test -name "*.class" 2>/dev/null | wc -l)
    echo "🧪 Test classes: $test_classes files"
    
    echo ""
    echo "🎯 Để chạy ứng dụng, sử dụng: ./start.sh"
    echo "🧪 Để chạy tests, sử dụng: ./test.sh"
}

# Menu chính
main_menu() {
    echo "Chọn loại build:"
    echo "1. Build Spring Boot Application"
    echo "2. Build QuanLy Module"
    echo "3. Build Test Files"
    echo "4. Build tất cả"
    echo "5. Hiển thị build summary"
    echo "0. Thoát"
    echo ""
    
    read -p "Nhập lựa chọn (0-5): " choice
    
    case $choice in
        1) 
            check_java
            build_spring_boot
            ;;
        2) 
            check_java
            build_quan_ly
            ;;
        3) 
            check_java
            build_tests
            ;;
        4) 
            check_java
            build_spring_boot
            build_quan_ly
            build_tests
            show_summary
            ;;
        5) 
            show_summary
            ;;
        0) 
            echo "👋 Thoát build"
            exit 0
            ;;
        *) 
            echo "❌ Lựa chọn không hợp lệ"
            main_menu
            ;;
    esac
}

# Nếu có tham số, chạy trực tiếp
if [ $# -gt 0 ]; then
    check_java
    case $1 in
        "spring"|"springboot") build_spring_boot ;;
        "quanly") build_quan_ly ;;
        "test") build_tests ;;
        "all") 
            build_spring_boot
            build_quan_ly
            build_tests
            show_summary
            ;;
        "summary") show_summary ;;
        *) 
            echo "❌ Tham số không hợp lệ: $1"
            echo "Sử dụng: ./build.sh [spring|quanly|test|all|summary]"
            ;;
    esac
else
    check_java
    main_menu
fi

echo ""
echo "✨ Build process hoàn tất!"
