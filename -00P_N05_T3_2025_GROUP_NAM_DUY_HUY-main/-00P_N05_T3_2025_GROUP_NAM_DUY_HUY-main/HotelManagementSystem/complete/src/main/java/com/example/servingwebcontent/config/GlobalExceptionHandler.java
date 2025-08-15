package com.example.servingwebcontent.config;

import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(MissingServletRequestParameterException.class)
  public ResponseEntity<Map<String, Object>> handleMissingParam(MissingServletRequestParameterException ex) {
    Map<String, Object> body = new LinkedHashMap<>();
    body.put("error", "Thiếu tham số");
    body.put("param", ex.getParameterName());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
  }

  @SuppressWarnings("null")
  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<Map<String, Object>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
    Map<String, Object> body = new LinkedHashMap<>();
    body.put("error", "Sai kiểu tham số");
    body.put("param", ex.getName());
    body.put("requiredType", ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : null);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
  }

  @ExceptionHandler(SQLException.class)
  public ResponseEntity<?> handleSql(SQLException e) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(Map.of("error", "Lỗi CSDL", "message", e.getMessage()));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<?> handleValidation(MethodArgumentNotValidException e) {
    return ResponseEntity.badRequest()
        .body(Map.of("error", "Dữ liệu không hợp lệ"));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<?> handleOther(Exception e) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(Map.of("error", "Lỗi hệ thống", "message", e.getMessage()));
  }

}
