#!/bin/bash

echo "================================================"
echo "🧪 TEST - CHẠY TESTS"
echo "================================================"
echo ""

# Function kiểm tra Java
check_java() {
    echo "☕ Kiểm tra Java..."
    if java -version >/dev/null 2>&1; then
        echo "✅ Java đã được cài đặt"
    else
        echo "❌ Java không được tìm thấy"
        echo "💡 Vui lòng cài đặt Java 11+ và thêm vào PATH"
        exit 1
    fi
}

# Build trước khi test
ensure_built() {
    echo "🔧 Đảm bảo project đã được build..."
    
    # Check if QuanLy classes exist
    if [ ! -f "../../QuanLy/src/Model/HangHoa.class" ]; then
        echo "⚠️  QuanLy chưa được build, đang build..."
        cd ../../QuanLy/src
        javac Model/*.java
        javac -cp . *.java
        cd - > /dev/null
    fi
    
    # Check if test classes exist
    if [ ! -f "../../test/TestMain.class" ]; then
        echo "⚠️  Test files chưa được build, đang build..."
        cd ../../test
        javac -cp .:../QuanLy/src *.java
        cd - > /dev/null
    fi
}

# Chạy Spring Boot tests
run_spring_boot_tests() {
    echo ""
    echo "🧪 Chạy Spring Boot Tests..."
    echo "============================"
    
    cd ../../gs-serving-web-content-main/complete
    
    if [ ! -f "pom.xml" ]; then
        echo "❌ Không tìm thấy pom.xml"
        cd - > /dev/null
        return 1
    fi
    
    echo "📁 Thư mục hiện tại: $(pwd)"
    chmod +x mvnw
    
    echo "🔍 Chạy unit tests..."
    ./mvnw test
    
    if [ $? -eq 0 ]; then
        echo "✅ Spring Boot tests passed!"
    else
        echo "❌ Spring Boot tests failed!"
        cd - > /dev/null
        return 1
    fi
    
    cd - > /dev/null
    return 0
}

# Chạy QuanLy tests
run_quan_ly_tests() {
    echo ""
    echo "🧪 Chạy QuanLy Tests..."
    echo "======================"
    
    cd ../../test
    echo "📁 Thư mục hiện tại: $(pwd)"
    
    local test_results=0
    
    # Test TestMain
    if [ -f "TestMain.class" ]; then
        echo ""
        echo "▶️  Chạy TestMain..."
        echo "-------------------"
        java -cp .:../QuanLy/src TestMain
        if [ $? -eq 0 ]; then
            echo "✅ TestMain passed"
        else
            echo "❌ TestMain failed"
            test_results=1
        fi
    fi
    
    # Test TestPhuongThuc1
    if [ -f "TestPhuongThuc1.class" ]; then
        echo ""
        echo "▶️  Chạy TestPhuongThuc1..."
        echo "--------------------------"
        java -cp .:../QuanLy/src TestPhuongThuc1
        if [ $? -eq 0 ]; then
            echo "✅ TestPhuongThuc1 passed"
        else
            echo "❌ TestPhuongThuc1 failed"
            test_results=1
        fi
    fi
    
    # Test TestQuanLyHangHoa
    if [ -f "TestQuanLyHangHoa.class" ]; then
        echo ""
        echo "▶️  Chạy TestQuanLyHangHoa..."
        echo "----------------------------"
        java -cp .:../QuanLy/src TestQuanLyHangHoa
        if [ $? -eq 0 ]; then
            echo "✅ TestQuanLyHangHoa passed"
        else
            echo "❌ TestQuanLyHangHoa failed"
            test_results=1
        fi
    fi
    
    # Test TestTinhTongNhapTheoNgay
    if [ -f "TestTinhTongNhapTheoNgay.class" ]; then
        echo ""
        echo "▶️  Chạy TestTinhTongNhapTheoNgay..."
        echo "----------------------------------"
        java -cp .:../QuanLy/src TestTinhTongNhapTheoNgay
        if [ $? -eq 0 ]; then
            echo "✅ TestTinhTongNhapTheoNgay passed"
        else
            echo "❌ TestTinhTongNhapTheoNgay failed"
            test_results=1
        fi
    fi
    
    cd - > /dev/null
    return $test_results
}

# Test coverage report
show_test_coverage() {
    echo ""
    echo "📊 TEST COVERAGE REPORT"
    echo "======================="
    echo ""
    
    # Count test files
    local total_tests=0
    local java_tests=0
    
    if [ -d "test" ]; then
        total_tests=$(find test -name "Test*.java" | wc -l)
        java_tests=$(find test -name "Test*.class" | wc -l)
    fi
    
    echo "📝 Tổng số test files: $total_tests"
    echo "✅ Test files đã build: $java_tests"
    
    # Spring Boot test info
    if [ -d "gs-serving-web-content-main/complete/src/test" ]; then
        local spring_tests=$(find gs-serving-web-content-main/complete/src/test -name "*.java" | wc -l)
        echo "🌱 Spring Boot test files: $spring_tests"
    fi
    
    echo ""
    echo "📋 Danh sách test files:"
    echo "------------------------"
    if [ -d "test" ]; then
        ls -la test/Test*.java 2>/dev/null | awk '{print "  " $9}' || echo "  Không có test files"
    fi
}

# Quick test - chỉ chạy tests cơ bản
run_quick_tests() {
    echo "⚡ Quick Test Mode..."
    echo "Chỉ chạy các tests cơ bản và nhanh"
    echo ""
    
    ensure_built
    
    cd ../../test
    
    # Chỉ chạy TestMain và TestPhuongThuc1
    if [ -f "TestMain.class" ]; then
        echo "▶️  Quick Test: TestMain"
        java -cp .:../QuanLy/src TestMain
    fi
    
    if [ -f "TestPhuongThuc1.class" ]; then
        echo "▶️  Quick Test: TestPhuongThuc1"
        java -cp .:../QuanLy/src TestPhuongThuc1
    fi
    
    cd - > /dev/null
    echo "⚡ Quick tests hoàn tất!"
}

# Menu chính
main_menu() {
    echo "Chọn loại test:"
    echo "1. Chạy Spring Boot tests"
    echo "2. Chạy QuanLy tests"
    echo "3. Chạy tất cả tests"
    echo "4. Quick test (tests cơ bản)"
    echo "5. Hiển thị test coverage"
    echo "0. Thoát"
    echo ""
    
    read -p "Nhập lựa chọn (0-5): " choice
    
    case $choice in
        1) 
            check_java
            run_spring_boot_tests
            ;;
        2) 
            check_java
            ensure_built
            run_quan_ly_tests
            ;;
        3) 
            check_java
            ensure_built
            local spring_result=0
            local quanly_result=0
            
            run_spring_boot_tests
            spring_result=$?
            
            run_quan_ly_tests
            quanly_result=$?
            
            echo ""
            echo "📊 TỔNG KẾT:"
            echo "============"
            if [ $spring_result -eq 0 ]; then
                echo "✅ Spring Boot tests: PASSED"
            else
                echo "❌ Spring Boot tests: FAILED"
            fi
            
            if [ $quanly_result -eq 0 ]; then
                echo "✅ QuanLy tests: PASSED"
            else
                echo "❌ QuanLy tests: FAILED"
            fi
            ;;
        4) 
            check_java
            run_quick_tests
            ;;
        5) 
            show_test_coverage
            ;;
        0) 
            echo "👋 Thoát test"
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
        "spring"|"springboot") run_spring_boot_tests ;;
        "quanly") 
            ensure_built
            run_quan_ly_tests 
            ;;
        "all") 
            ensure_built
            run_spring_boot_tests
            run_quan_ly_tests
            ;;
        "quick") run_quick_tests ;;
        "coverage") show_test_coverage ;;
        *) 
            echo "❌ Tham số không hợp lệ: $1"
            echo "Sử dụng: ./test.sh [spring|quanly|all|quick|coverage]"
            ;;
    esac
else
    main_menu
fi

echo ""
echo "✨ Test process hoàn tất!"
