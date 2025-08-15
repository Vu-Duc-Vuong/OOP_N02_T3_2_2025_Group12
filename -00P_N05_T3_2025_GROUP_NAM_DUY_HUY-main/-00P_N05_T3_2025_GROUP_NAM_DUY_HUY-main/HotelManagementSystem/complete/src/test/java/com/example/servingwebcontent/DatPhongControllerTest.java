package com.example.servingwebcontent;

import com.example.servingwebcontent.controller.DatPhongController;
import com.example.servingwebcontent.model.BookingStatus;
import com.example.servingwebcontent.model.DatPhong;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DatPhongController.class)
class DatPhongControllerTest {

    @Autowired
    MockMvc mvc;

    @Test
    void create_invalidDates_400() throws Exception {
        mvc.perform(post("/api/dat-phong")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"maDatPhong\":\"DP1\",\"dinhDanhKhach\":\"K1\",\"maPhong\":\"P1\",\"ngayNhan\":\"2025-08-20\",\"ngayTra\":\"2025-08-19\",\"soKhach\":2}"))
           .andExpect(status().isBadRequest());
    }

    @Test
    void create_roomBusy_409() throws Exception {
        try (MockedStatic<DatPhong> mocked = Mockito.mockStatic(DatPhong.class)) {
            mocked.when(() -> DatPhong.isPhongTrong(
                    "P1",
                    java.time.LocalDate.parse("2025-08-20"),
                    java.time.LocalDate.parse("2025-08-22"))).thenReturn(false);

            mvc.perform(post("/api/dat-phong")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"maDatPhong\":\"DP1\",\"dinhDanhKhach\":\"K1\",\"maPhong\":\"P1\",\"ngayNhan\":\"2025-08-20\",\"ngayTra\":\"2025-08-22\",\"soKhach\":2}"))
               .andExpect(status().isConflict());
        }
    }

    @Test
    void create_ok_201() throws Exception {
        try (MockedStatic<DatPhong> mocked = Mockito.mockStatic(DatPhong.class)) {
            mocked.when(() -> DatPhong.isPhongTrong(
                    "P1",
                    java.time.LocalDate.parse("2025-08-20"),
                    java.time.LocalDate.parse("2025-08-22"))).thenReturn(true);
            mocked.when(() -> DatPhong.insert(Mockito.any(DatPhong.class))).thenReturn(true);

            mvc.perform(post("/api/dat-phong")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"maDatPhong\":\"DP1\",\"dinhDanhKhach\":\"K1\",\"maPhong\":\"P1\",\"ngayNhan\":\"2025-08-20\",\"ngayTra\":\"2025-08-22\",\"soKhach\":2,\"trangThai\":\"DA_DAT\"}"))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.maDatPhong").value("DP1"))
               .andExpect(jsonPath("$.trangThai").value(BookingStatus.DA_DAT.name()));
        }
    }

    @Test
    void checkin_ok_200() throws Exception {
        try (MockedStatic<DatPhong> mocked = Mockito.mockStatic(DatPhong.class)) {
            mocked.when(() -> DatPhong.checkIn("DP1")).thenReturn(true);
            mvc.perform(post("/api/dat-phong/DP1/checkin"))
               .andExpect(status().isOk());
        }
    }

    @Test
    void checkin_fail_400() throws Exception {
        try (MockedStatic<DatPhong> mocked = Mockito.mockStatic(DatPhong.class)) {
            mocked.when(() -> DatPhong.checkIn("DP1")).thenReturn(false);
            mvc.perform(post("/api/dat-phong/DP1/checkin"))
               .andExpect(status().isBadRequest());
        }
    }
}
