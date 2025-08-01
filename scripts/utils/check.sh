#!/bin/bash

echo "================================================"
echo "🔍 CHECK - KIỂM TRA HỆ THỐNG"
echo "================================================"
echo ""

# Function kiểm tra và hiển thị status
check_status() {
    local name="$1"
    local command="$2"
    local required="$3"
    
    echo -n "🔍 $name: "
    
    if eval "$command" >/dev/null 2>&1; then
        echo "✅ OK"
        return 0
    else
        if [ "$required" = "required" ]; then
            echo "❌ MISSING (Required)"
            return 1
        else
            echo "⚠️  MISSING (Optional)"
            return 0
        fi
    fi
}

# Kiểm tra Java
check_java() {
    echo "☕ JAVA ENVIRONMENT"
    echo "=================="
    
    if java -version >/dev/null 2>&1; then
        echo "✅ Java: Available"
        java -version 2>&1 | head -3 | sed 's/^/   /'
        
        # Check JAVA_HOME
        if [ -n "$JAVA_HOME" ]; then
            echo "✅ JAVA_HOME: $JAVA_HOME"
        else
            echo "⚠️  JAVA_HOME: Not set (optional)"
        fi
        
        return 0
    else
        echo "❌ Java: Not found"
        echo "💡 Install Java 11+ from: https://adoptium.net/"
        return 1
    fi
}

# Kiểm tra Maven
check_maven() {
    echo ""
    echo "📦 MAVEN"
    echo "========"
    
    # Check system Maven
    if mvn -version >/dev/null 2>&1; then
        echo "✅ System Maven: Available"
        mvn -version 2>&1 | head -2 | sed 's/^/   /'
    else
        echo "⚠️  System Maven: Not found (optional - using wrapper)"
    fi
    
    # Check Maven Wrapper
    if [ -f "../../../../gs-serving-web-content-main/complete/mvnw" ]; then
        echo "✅ Maven Wrapper: Available"
        if [ -x "../../../../gs-serving-web-content-main/complete/mvnw" ]; then
            echo "✅ Maven Wrapper: Executable"
        else
            echo "⚠️  Maven Wrapper: Not executable (will be fixed)"
        fi
    else
        echo "❌ Maven Wrapper: Missing"
        return 1
    fi
    
    return 0
}

# Kiểm tra cấu trúc project
check_project_structure() {
    echo ""
    echo "📁 PROJECT STRUCTURE"
    echo "===================="
    
    local errors=0
    
    # Core directories
    check_status "Spring Boot Complete" "[ -d '../../../../gs-serving-web-content-main/complete' ]" "required"
    errors=$((errors + $?))
    
    check_status "Spring Boot POM" "[ -f '../../../../gs-serving-web-content-main/complete/pom.xml' ]" "required"
    errors=$((errors + $?))
    
    check_status "QuanLy Source" "[ -d '../../QuanLy/src' ]" "required"
    errors=$((errors + $?))
    
    check_status "Test Directory" "[ -d '../../test' ]" "optional"
    
    # Java source files
    check_status "HangHoa Model" "[ -f '../../QuanLy/src/Model/HangHoa.java' ]" "required"
    errors=$((errors + $?))
    
    check_status "QuanLyHangHoa" "[ -f '../../QuanLy/src/QuanLyHangHoa.java' ]" "required"
    errors=$((errors + $?))
    
    return $errors
}

# Kiểm tra dependencies
check_dependencies() {
    echo ""
    echo "🔗 DEPENDENCIES"
    echo "==============="
    
    local errors=0
    
    # Check if we can reach Maven Central
    echo -n "🌐 Maven Central: "
    if ping -c 1 repo1.maven.org >/dev/null 2>&1; then
        echo "✅ Reachable"
    else
        echo "⚠️  Not reachable (check internet)"
    fi
    
    # Check Spring Boot dependencies in POM
    if [ -f "../../gs-serving-web-content-main/complete/pom.xml" ]; then
        echo -n "🌱 Spring Boot Starter Web: "
        if grep -q "spring-boot-starter-web" ../../gs-serving-web-content-main/complete/pom.xml; then
            echo "✅ Found"
        else
            echo "❌ Missing"
            errors=$((errors + 1))
        fi
        
        echo -n "🌿 Spring Boot Starter Thymeleaf: "
        if grep -q "spring-boot-starter-thymeleaf" ../../gs-serving-web-content-main/complete/pom.xml; then
            echo "✅ Found"
        else
            echo "⚠️  Missing (optional)"
        fi
    fi
    
    return $errors
}

# Kiểm tra ports
check_ports() {
    echo ""
    echo "🌐 NETWORK PORTS"
    echo "==============="
    
    # Check port 8080
    echo -n "🔌 Port 8080: "
    if netstat -tuln 2>/dev/null | grep -q ":8080 " || ss -tuln 2>/dev/null | grep -q ":8080 "; then
        echo "⚠️  In use"
        echo "   💡 Stop the service using port 8080 or change the port"
    else
        echo "✅ Available"
    fi
}

# Kiểm tra build artifacts
check_build_artifacts() {
    echo ""
    echo "🏗️  BUILD ARTIFACTS"
    echo "=================="
    
    # Spring Boot JAR
    echo -n "📦 Spring Boot JAR: "
    if [ -f "../../gs-serving-web-content-main/complete/target/serving-web-content-complete-0.0.1-SNAPSHOT.jar" ]; then
        echo "✅ Available"
        ls -lh ../../gs-serving-web-content-main/complete/target/*.jar | tail -1 | awk '{print "   Size: " $5 " | Date: " $6 " " $7 " " $8}'
    else
        echo "⚠️  Not built (run ./build.sh)"
    fi
    
    # QuanLy classes
    local quanly_classes=$(find ../../QuanLy/src -name "*.class" 2>/dev/null | wc -l)
    echo "📝 QuanLy Classes: $quanly_classes compiled"
    
    # Test classes
    local test_classes=$(find ../../test -name "*.class" 2>/dev/null | wc -l)
    echo "🧪 Test Classes: $test_classes compiled"
}

# Kiểm tra git repository
check_git() {
    echo ""
    echo "📚 GIT REPOSITORY"
    echo "================="
    
    if [ -d ".git" ]; then
        echo "✅ Git Repository: Initialized"
        
        # Current branch
        local branch=$(git branch --show-current 2>/dev/null)
        if [ -n "$branch" ]; then
            echo "🌿 Current Branch: $branch"
        fi
        
        # Check for uncommitted changes
        if git diff --quiet 2>/dev/null; then
            echo "✅ Working Directory: Clean"
        else
            echo "⚠️  Working Directory: Has uncommitted changes"
        fi
        
        # Check remote
        local remote=$(git remote get-url origin 2>/dev/null)
        if [ -n "$remote" ]; then
            echo "🔗 Remote Origin: $remote"
        else
            echo "⚠️  Remote Origin: Not set"
        fi
    else
        echo "⚠️  Git Repository: Not initialized"
    fi
}

# Kiểm tra system info
check_system() {
    echo ""
    echo "💻 SYSTEM INFO"
    echo "=============="
    
    echo "🖥️  OS: $(uname -s) $(uname -r)"
    echo "🏗️  Architecture: $(uname -m)"
    
    # Memory
    if command -v free >/dev/null 2>&1; then
        local memory=$(free -h | awk '/^Mem:/ {print $2}')
        echo "💾 Memory: $memory"
    elif command -v vm_stat >/dev/null 2>&1; then
        echo "💾 Memory: $(sysctl -n hw.memsize | awk '{print int($1/1024/1024/1024) "GB"}')"
    fi
    
    # Disk space
    echo "💽 Disk Space: $(df -h . | awk 'NR==2 {print $4 " available"}')"
}

# Tổng kết
show_summary() {
    echo ""
    echo "📊 SUMMARY"
    echo "=========="
    
    local total_errors=0
    
    # Chạy lại các checks cơ bản và đếm errors
    check_java >/dev/null 2>&1 || total_errors=$((total_errors + 1))
    check_maven >/dev/null 2>&1 || total_errors=$((total_errors + 1))
    check_project_structure >/dev/null 2>&1 || total_errors=$((total_errors + $?))
    
    if [ $total_errors -eq 0 ]; then
        echo "✅ System Status: READY"
        echo "🚀 You can run: ./start.sh"
    else
        echo "❌ System Status: ISSUES FOUND ($total_errors errors)"
        echo "🔧 Please fix the issues above before running the application"
    fi
    
    echo ""
    echo "📖 For help: ./help.sh"
    echo "🏗️  To build: ./build.sh"
    echo "🧪 To test: ./test.sh"
}

# Menu chính
main_menu() {
    echo "Chọn loại kiểm tra:"
    echo "1. Kiểm tra Java"
    echo "2. Kiểm tra Maven"
    echo "3. Kiểm tra cấu trúc project"
    echo "4. Kiểm tra dependencies"
    echo "5. Kiểm tra network ports"
    echo "6. Kiểm tra build artifacts"
    echo "7. Kiểm tra git repository"
    echo "8. Kiểm tra system info"
    echo "9. Kiểm tra tất cả"
    echo "0. Thoát"
    echo ""
    
    read -p "Nhập lựa chọn (0-9): " choice
    
    case $choice in
        1) check_java ;;
        2) check_maven ;;
        3) check_project_structure ;;
        4) check_dependencies ;;
        5) check_ports ;;
        6) check_build_artifacts ;;
        7) check_git ;;
        8) check_system ;;
        9) 
            check_java
            check_maven
            check_project_structure
            check_dependencies
            check_ports
            check_build_artifacts
            check_git
            check_system
            show_summary
            ;;
        0) 
            echo "👋 Thoát check"
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

# Nếu có tham số, chạy trực tiếp
if [ $# -gt 0 ]; then
    case $1 in
        "java") check_java ;;
        "maven") check_maven ;;
        "structure") check_project_structure ;;
        "dependencies"|"deps") check_dependencies ;;
        "ports") check_ports ;;
        "build") check_build_artifacts ;;
        "git") check_git ;;
        "system") check_system ;;
        "all") 
            check_java
            check_maven
            check_project_structure
            check_dependencies
            check_ports
            check_build_artifacts
            check_git
            check_system
            show_summary
            ;;
        "summary") show_summary ;;
        *) 
            echo "❌ Tham số không hợp lệ: $1"
            echo "Sử dụng: ./check.sh [java|maven|structure|dependencies|ports|build|git|system|all|summary]"
            ;;
    esac
else
    # Chạy kiểm tra tổng quan
    check_java
    check_maven
    check_project_structure
    check_dependencies
    check_ports
    check_build_artifacts
    check_git
    check_system
    show_summary
fi
