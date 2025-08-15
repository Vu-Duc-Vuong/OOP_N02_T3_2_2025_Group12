package com.example.servingwebcontent.controller;

import com.example.servingwebcontent.database.aivenConnection;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// SQL
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.SQLException;
import java.sql.Date;

// Time
import java.time.LocalDate;
import java.time.YearMonth;

// Collections
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/bao-cao")
public class BaoCaoController {

    // ================== 1) DOANH THU THEO THÁNG ==================
    // GET /api/bao-cao/doanh-thu?year=2025&month=8
    @GetMapping("/doanh-thu")
    public ResponseEntity<?> doanhThuTheoThang(@RequestParam int year, @RequestParam int month) {
        LocalDate start = YearMonth.of(year, month).atDay(1);
        LocalDate end = start.plusMonths(1); // [start, end)

        String sql = """
                SELECT
                  COALESCE(SUM(tong_thanh_toan),0)   AS tong,
                  COALESCE(SUM(tong_tien_phong),0)   AS phong,
                  COALESCE(SUM(tong_tien_dich_vu),0) AS dv,
                  COALESCE(SUM(thue),0)              AS thue,
                  COALESCE(SUM(giam_gia),0)          AS giam
                FROM ledger_doanh_thu
                WHERE ngay_thanh_toan >= ? AND ngay_thanh_toan < ?
                """;

        Map<String, Object> res = new LinkedHashMap<>();
        res.put("year", year);
        res.put("month", month);

        try (Connection cn = aivenConnection.getConnection();
                PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setTimestamp(1, Timestamp.valueOf(start.atStartOfDay()));
            ps.setTimestamp(2, Timestamp.valueOf(end.atStartOfDay()));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    res.put("tongDoanhThu", rs.getDouble("tong"));
                    res.put("doanhThuPhong", rs.getDouble("phong"));
                    res.put("doanhThuDichVu", rs.getDouble("dv"));
                    res.put("thue", rs.getDouble("thue"));
                    res.put("giamGia", rs.getDouble("giam"));
                }
            }
        } catch (SQLException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
        return ResponseEntity.ok(res);
    }

    // ================== 2) CÔNG SUẤT PHÒNG ==================
    // GET /api/bao-cao/cong-suat-phong?year=2025&month=8
    @GetMapping("/cong-suat-phong")
    public ResponseEntity<?> congSuatPhong(@RequestParam int year, @RequestParam int month) {
        YearMonth ym = YearMonth.of(year, month);
        LocalDate start = ym.atDay(1);
        LocalDate end = start.plusMonths(1); // [start, end)
        int daysInMonth = ym.lengthOfMonth();

        Map<String, Object> res = new LinkedHashMap<>();
        res.put("year", year);
        res.put("month", month);
        res.put("daysInMonth", daysInMonth);

        String sqlCountRooms = "SELECT COUNT(*) FROM phong_khach_san";
        String sqlBookedNights = """
                  SELECT COALESCE(SUM(GREATEST(0,
                           DATEDIFF(LEAST(ld.ngay_tra, ?), GREATEST(ld.ngay_nhan, ?))
                         )), 0) AS nights
                  FROM ledger_doanh_thu ld
                """;
        String sqlBookedNightsByType = """
                  SELECT p.loai_phong,
                         COALESCE(SUM(GREATEST(0,
                           DATEDIFF(LEAST(ld.ngay_tra, ?), GREATEST(ld.ngay_nhan, ?))
                         )), 0) AS nights
                  FROM ledger_doanh_thu ld
                  JOIN phong_khach_san p ON ld.ma_phong = p.ma_phong
                  GROUP BY p.loai_phong
                """;
        String sqlRoomCountByType = "SELECT loai_phong, COUNT(*) AS cnt FROM phong_khach_san GROUP BY loai_phong";

        try (Connection cn = aivenConnection.getConnection()) {
            int roomCount;
            try (PreparedStatement ps = cn.prepareStatement(sqlCountRooms);
                    ResultSet rs = ps.executeQuery()) {
                rs.next();
                roomCount = rs.getInt(1);
            }
            long totalRoomNights = (long) roomCount * daysInMonth;

            long bookedNights;
            try (PreparedStatement ps = cn.prepareStatement(sqlBookedNights)) {
                ps.setDate(1, Date.valueOf(end));
                ps.setDate(2, Date.valueOf(start));
                try (ResultSet rs = ps.executeQuery()) {
                    rs.next();
                    bookedNights = rs.getLong("nights");
                }
            }

            double occupancy = totalRoomNights == 0 ? 0.0 : (double) bookedNights / totalRoomNights;

            // Theo loại phòng
            Map<String, Integer> roomByType = new HashMap<>();
            try (PreparedStatement ps = cn.prepareStatement(sqlRoomCountByType);
                    ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    roomByType.put(rs.getString("loai_phong"), rs.getInt("cnt"));
                }
            }

            List<Map<String, Object>> perType = new ArrayList<>();
            try (PreparedStatement ps = cn.prepareStatement(sqlBookedNightsByType)) {
                ps.setDate(1, Date.valueOf(end));
                ps.setDate(2, Date.valueOf(start));
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        String loai = rs.getString("loai_phong");
                        long nights = rs.getLong("nights");
                        int countRoomType = roomByType.getOrDefault(loai, 0);
                        long totalNightsType = (long) countRoomType * daysInMonth;
                        double occType = totalNightsType == 0 ? 0.0 : (double) nights / totalNightsType;

                        Map<String, Object> row = new LinkedHashMap<>();
                        row.put("loaiPhong", loai);
                        row.put("soPhong", countRoomType);
                        row.put("nightsBooked", nights);
                        row.put("totalRoomNights", totalNightsType);
                        row.put("occupancy", occType);
                        perType.add(row);
                    }
                }
            }

            res.put("rooms", roomCount);
            res.put("nightsBooked", bookedNights);
            res.put("totalRoomNights", totalRoomNights);
            res.put("occupancy", occupancy);
            res.put("byRoomType", perType);

            return ResponseEntity.ok(res);
        } catch (SQLException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // ================== 3) DỊCH VỤ BÁN CHẠY ==================
    // GET /api/bao-cao/dich-vu-ban-chay?year=2025&month=8&limit=5
    @GetMapping("/dich-vu-ban-chay")
    public ResponseEntity<?> dichVuBanChay(@RequestParam int year,
            @RequestParam int month,
            @RequestParam(required = false, defaultValue = "5") int limit) {
        LocalDate start = YearMonth.of(year, month).atDay(1);
        LocalDate end = start.plusMonths(1);

        String sql = """
                    SELECT ma_dich_vu,
                           ten_dich_vu,
                           SUM(so_luong)   AS so_luong,
                           SUM(doanh_thu)  AS doanh_thu
                    FROM ledger_dich_vu
                    WHERE ngay_thanh_toan >= ? AND ngay_thanh_toan < ?
                    GROUP BY ma_dich_vu, ten_dich_vu
                    ORDER BY doanh_thu DESC
                    LIMIT ?
                """;

        List<Map<String, Object>> list = new ArrayList<>();
        try (Connection cn = aivenConnection.getConnection();
                PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setTimestamp(1, Timestamp.valueOf(start.atStartOfDay()));
            ps.setTimestamp(2, Timestamp.valueOf(end.atStartOfDay()));
            ps.setInt(3, Math.max(1, limit));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> row = new LinkedHashMap<>();
                    row.put("maDichVu", rs.getString("ma_dich_vu"));
                    row.put("tenDichVu", rs.getString("ten_dich_vu"));
                    row.put("soLuong", rs.getLong("so_luong"));
                    row.put("doanhThu", rs.getDouble("doanh_thu"));
                    list.add(row);
                }
            }
        } catch (SQLException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
        return ResponseEntity.ok(list);
    }

}
