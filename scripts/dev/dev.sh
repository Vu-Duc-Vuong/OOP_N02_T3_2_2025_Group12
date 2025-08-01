#!/bin/bash

echo "================================================"
echo "🔄 DEVELOPMENT - CÔNG CỤ PHÁT TRIỂN"
echo "================================================"
echo ""

# Function để theo dõi file changes
watch_files() {
    echo "👀 Theo dõi thay đổi files..."
    echo "Ctrl+C để dừng"
    echo ""
    
    # Sử dụng inotifywait nếu có, không thì fallback to find
    if command -v inotifywait >/dev/null 2>&1; then
        echo "📡 Sử dụng inotifywait để theo dõi..."
        inotifywait -m -r -e modify --format '%w%f' \
            --include='.*\.(java|html|css|js|properties)$' \
            ../../gs-serving-web-content-main/complete/src/ ../../QuanLy/src/ ../../test/ 2>/dev/null | \
        while read file; do
            echo "🔄 File changed: $file"
            echo "💡 Chạy ./build.sh để rebuild"
        done
    else
        echo "⚠️  inotifywait không có sẵn, sử dụng polling..."
        local last_check=$(date +%s)
        while true; do
            local changed_files=$(find ../../gs-serving-web-content-main/complete/src/ ../../QuanLy/src/ ../../test/ \
                -name "*.java" -o -name "*.html" -o -name "*.css" -o -name "*.js" -o -name "*.properties" \
                -newer /tmp/dev_watch_${last_check} 2>/dev/null)
            
            if [ -n "$changed_files" ]; then
                echo "🔄 Files changed:"
                echo "$changed_files" | sed 's/^/  /'
                echo "💡 Chạy ./build.sh để rebuild"
                last_check=$(date +%s)
                touch /tmp/dev_watch_${last_check}
            fi
            
            sleep 2
        done
    fi
}

# Auto rebuild when files change
auto_rebuild() {
    echo "🔄 Auto-rebuild mode..."
    echo "Sẽ tự động rebuild khi có file thay đổi"
    echo "Ctrl+C để dừng"
    echo ""
    
    # Create initial timestamp
    local timestamp=$(date +%s)
    touch /tmp/auto_rebuild_${timestamp}
    
    while true; do
        # Check for Java file changes
        local changed_java=$(find ../../QuanLy/src/ ../../test/ -name "*.java" -newer /tmp/auto_rebuild_${timestamp} 2>/dev/null)
        local changed_spring=$(find ../../gs-serving-web-content-main/complete/src/ -name "*.java" -newer /tmp/auto_rebuild_${timestamp} 2>/dev/null)
        
        if [ -n "$changed_java" ] || [ -n "$changed_spring" ]; then
            echo "🔄 Detected changes, rebuilding..."
            
            if [ -n "$changed_java" ]; then
                echo "📝 Rebuilding QuanLy..."
                ./build.sh quanly >/dev/null 2>&1
            fi
            
            if [ -n "$changed_spring" ]; then
                echo "🌱 Rebuilding Spring Boot..."
                ./build.sh spring >/dev/null 2>&1
            fi
            
            echo "✅ Rebuild completed at $(date)"
            timestamp=$(date +%s)
            touch /tmp/auto_rebuild_${timestamp}
        fi
        
        sleep 3
    done
}

# Development server với hot reload
dev_server() {
    echo "🌱 Starting Development Server..."
    echo "================================="
    
    # Build trước
    echo "🏗️  Initial build..."
    ./build.sh all >/dev/null 2>&1
    
    cd ../../gs-serving-web-content-main/complete
    
    echo "🚀 Starting Spring Boot in dev mode..."
    echo "📱 Access: http://localhost:8080"
    echo "🔄 Changes to templates/static files will be hot-reloaded"
    echo ""
    
    # Spring Boot dev mode với hot reload
    ./mvnw spring-boot:run -Dspring-boot.run.jvmArguments="-Dspring.devtools.restart.enabled=true"
    
    cd - > /dev/null
}

# Code quality check
quality_check() {
    echo "🔍 Code Quality Check..."
    echo "========================"
    
    local issues=0
    
    echo "📝 Checking Java code style..."
    
    # Check for common issues
    echo "🔍 Kiểm tra naming conventions..."
    if grep -r "class [a-z]" ../../QuanLy/src/ ../../test/ 2>/dev/null; then
        echo "⚠️  Found lowercase class names"
        issues=$((issues + 1))
    fi
    
    echo "🔍 Kiểm tra imports không sử dụng..."
    local unused_imports=$(grep -r "^import " ../../QuanLy/src/ ../../test/ 2>/dev/null | \
        awk -F: '{print $2}' | sort | uniq -c | awk '$1 == 1 {print $2}' | wc -l)
    if [ $unused_imports -gt 0 ]; then
        echo "⚠️  Found $unused_imports potentially unused imports"
        issues=$((issues + 1))
    fi
    
    echo "🔍 Kiểm tra code duplication..."
    # Simple check for duplicate lines
    local duplicates=$(find ../../QuanLy/src/ ../../test/ -name "*.java" -exec cat {} \; | \
        grep -v "^$" | grep -v "^//" | sort | uniq -d | wc -l)
    if [ $duplicates -gt 5 ]; then
        echo "⚠️  Found potential code duplication"
        issues=$((issues + 1))
    fi
    
    echo ""
    if [ $issues -eq 0 ]; then
        echo "✅ Code quality: GOOD"
    else
        echo "⚠️  Code quality: $issues issues found"
    fi
    
    return $issues
}

# Generate documentation
generate_docs() {
    echo "📚 Generating Documentation..."
    echo "=============================="
    
    local docs_dir="docs"
    mkdir -p "$docs_dir"
    
    echo "📝 Generating Java docs..."
    
    # Generate simple documentation
    cat > "$docs_dir/API.md" << 'EOF'
# API Documentation

## QuanLy Classes

### HangHoa
- Quản lý thông tin hàng hóa
- Properties: mã hàng, tên hàng, giá, số lượng

### QuanLyHangHoa
- Quản lý danh sách hàng hóa
- Methods: thêm, sửa, xóa, tìm kiếm

### QuanLyBan
- Quản lý bán hàng
- Methods: tạo hóa đơn, tính tổng

### QuanLyNhap
- Quản lý nhập hàng
- Methods: nhập hàng, cập nhật tồn kho

## Spring Boot Endpoints

### GET /
- Trang chủ ứng dụng

### GET /greeting
- Trang chào mừng
- Parameters: name (optional)

EOF

    echo "📊 Generating project statistics..."
    
    cat > "$docs_dir/STATS.md" << EOF
# Project Statistics

Generated on: $(date)

## File Counts
- Java files: $(find . -name "*.java" | wc -l)
- HTML files: $(find . -name "*.html" | wc -l)
- CSS files: $(find . -name "*.css" | wc -l)
- JS files: $(find . -name "*.js" | wc -l)

## Lines of Code
- Java: $(find . -name "*.java" -exec cat {} \; | wc -l)
- HTML: $(find . -name "*.html" -exec cat {} \; | wc -l)

## Test Coverage
- Test files: $(find ../../test/ -name "Test*.java" 2>/dev/null | wc -l)

EOF

    echo "✅ Documentation generated in $docs_dir/"
    ls -la "$docs_dir/"
}

# Performance test
performance_test() {
    echo "⚡ Performance Test..."
    echo "====================="
    
    echo "🔍 Testing Spring Boot startup time..."
    
    cd ../../gs-serving-web-content-main/complete
    
    # Time the startup
    local start_time=$(date +%s)
    timeout 30s ./mvnw spring-boot:run -Dspring-boot.run.arguments="--server.port=0" >/dev/null 2>&1 &
    local pid=$!
    
    # Wait for startup or timeout
    sleep 10
    kill $pid 2>/dev/null
    
    local end_time=$(date +%s)
    local duration=$((end_time - start_time))
    
    echo "⏱️  Startup time: ~${duration}s"
    
    if [ $duration -lt 15 ]; then
        echo "✅ Performance: GOOD"
    elif [ $duration -lt 30 ]; then
        echo "⚠️  Performance: ACCEPTABLE"
    else
        echo "❌ Performance: SLOW"
    fi
    
    cd - > /dev/null
}

# Menu chính
main_menu() {
    echo "Chọn công cụ development:"
    echo "1. Watch files (theo dõi thay đổi)"
    echo "2. Auto-rebuild (tự động build khi có thay đổi)"
    echo "3. Development server (hot reload)"
    echo "4. Code quality check"
    echo "5. Generate documentation"
    echo "6. Performance test"
    echo "0. Thoát"
    echo ""
    
    read -p "Nhập lựa chọn (0-6): " choice
    
    case $choice in
        1) watch_files ;;
        2) auto_rebuild ;;
        3) dev_server ;;
        4) quality_check ;;
        5) generate_docs ;;
        6) performance_test ;;
        0) 
            echo "👋 Thoát development tools"
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
    case $1 in
        "watch") watch_files ;;
        "auto-rebuild") auto_rebuild ;;
        "dev-server") dev_server ;;
        "quality") quality_check ;;
        "docs") generate_docs ;;
        "performance") performance_test ;;
        *) 
            echo "❌ Tham số không hợp lệ: $1"
            echo "Sử dụng: ./dev.sh [watch|auto-rebuild|dev-server|quality|docs|performance]"
            ;;
    esac
else
    main_menu
fi

echo ""
echo "✨ Development tools hoàn tất!"
