package com.example.servingwebcontent;

import com.example.servingwebcontent.config.GlobalExceptionHandler;
import com.example.servingwebcontent.controller.PhongKhachSanController;
import com.example.servingwebcontent.model.PhongKhachSan;
import com.example.servingwebcontent.model.RoomStatus;
import com.example.servingwebcontent.model.RoomType;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test Controller cho /api/phong
 * - Dùng WebMvcTest + mockito-inline (mock static methods của Model)
 * - Import GlobalExceptionHandler để assert JSON lỗi & HTTP status chuẩn
 */
@WebMvcTest(PhongKhachSanController.class)
@Import(GlobalExceptionHandler.class)
class PhongControllerTest {

    @Autowired
    MockMvc mvc;

    // ====== GET /api/phong (list) ======
    @Test
    void list_ok() throws Exception {
        PhongKhachSan p = new PhongKhachSan("P101", RoomType.DOI, 800000, RoomStatus.TRONG, 2, "TV,Wifi");
        try (MockedStatic<PhongKhachSan> mocked = Mockito.mockStatic(PhongKhachSan.class)) {
            mocked.when(PhongKhachSan::findAll).thenReturn(List.of(p));

            mvc.perform(get("/api/phong"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].maPhong").value("P101"))
               .andExpect(jsonPath("$[0].loaiPhong").value("DOI"))
               .andExpect(jsonPath("$[0].tinhTrang").value("TRONG"));
        }
    }

    // ====== GET /api/phong/{maPhong} ======
    @Test
    void get_one_found_and_notfound() throws Exception {
        try (MockedStatic<PhongKhachSan> mocked = Mockito.mockStatic(PhongKhachSan.class)) {
            mocked.when(() -> PhongKhachSan.findById("P101"))
                  .thenReturn(new PhongKhachSan("P101", RoomType.DON, 500000, RoomStatus.TRONG, 1, "Wifi"));

            mvc.perform(get("/api/phong/P101"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.maPhong").value("P101"));

            mocked.when(() -> PhongKhachSan.findById("PX")).thenReturn(null);
            mvc.perform(get("/api/phong/PX"))
               .andExpect(status().isNotFound());
        }
    }

    // ====== POST /api/phong (create) ======
    @Test
    void create_validate_and_conflict() throws Exception {
        // thiếu maPhong
        mvc.perform(post("/api/phong")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"loaiPhong\":\"DON\",\"giaMoiDem\":500000,\"soNguoiToiDa\":1}"))
           .andExpect(status().isBadRequest());

        // thiếu loaiPhong
        mvc.perform(post("/api/phong")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"maPhong\":\"P202\",\"giaMoiDem\":500000,\"soNguoiToiDa\":1}"))
           .andExpect(status().isBadRequest());

        // giaMoiDem < 0
        mvc.perform(post("/api/phong")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"maPhong\":\"P202\",\"loaiPhong\":\"DON\",\"giaMoiDem\":-1,\"soNguoiToiDa\":1}"))
           .andExpect(status().isBadRequest());

        // soNguoiToiDa <= 0
        mvc.perform(post("/api/phong")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"maPhong\":\"P202\",\"loaiPhong\":\"DON\",\"giaMoiDem\":500000,\"soNguoiToiDa\":0}"))
           .andExpect(status().isBadRequest());

        // conflict khi đã tồn tại
        try (MockedStatic<PhongKhachSan> mocked = Mockito.mockStatic(PhongKhachSan.class)) {
            mocked.when(() -> PhongKhachSan.findById("P202"))
                  .thenReturn(new PhongKhachSan());

            mvc.perform(post("/api/phong")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"maPhong\":\"P202\",\"loaiPhong\":\"DON\",\"giaMoiDem\":500000,\"soNguoiToiDa\":1}"))
               .andExpect(status().isConflict());
        }
    }

    @Test
    void create_ok_201() throws Exception {
        try (MockedStatic<PhongKhachSan> mocked = Mockito.mockStatic(PhongKhachSan.class)) {
            mocked.when(() -> PhongKhachSan.findById("P202")).thenReturn(null);
            mocked.when(() -> PhongKhachSan.insert(any(PhongKhachSan.class))).thenReturn(true);

            mvc.perform(post("/api/phong")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"maPhong\":\"P202\",\"loaiPhong\":\"DON\",\"giaMoiDem\":500000," +
                             "\"tinhTrang\":\"TRONG\",\"soNguoiToiDa\":1,\"tienNghiKemTheo\":\"Wifi\"}"))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.maPhong").value("P202"))
               .andExpect(jsonPath("$.tinhTrang").value("TRONG"));
        }
    }

    // ====== PUT /api/phong/{maPhong} (update) ======
    @Test
    void update_notfound_404() throws Exception {
        try (MockedStatic<PhongKhachSan> mocked = Mockito.mockStatic(PhongKhachSan.class)) {
            mocked.when(() -> PhongKhachSan.findById("PX")).thenReturn(null);

            mvc.perform(put("/api/phong/PX")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"giaMoiDem\":550000,\"soNguoiToiDa\":2}"))
               .andExpect(status().isNotFound());
        }
    }

    @Test
    void update_ok_200() throws Exception {
        try (MockedStatic<PhongKhachSan> mocked = Mockito.mockStatic(PhongKhachSan.class)) {
            PhongKhachSan old = new PhongKhachSan("P202", RoomType.DON, 500000, RoomStatus.TRONG, 1, "Wifi");
            mocked.when(() -> PhongKhachSan.findById("P202")).thenReturn(old);
            mocked.when(() -> PhongKhachSan.update(any(PhongKhachSan.class))).thenReturn(true);

            mvc.perform(put("/api/phong/P202")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"giaMoiDem\":550000,\"soNguoiToiDa\":2,\"tienNghiKemTheo\":\"Wifi,TV\"}"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.giaMoiDem").value(550000.0))
               .andExpect(jsonPath("$.soNguoiToiDa").value(2))
               .andExpect(jsonPath("$.tienNghiKemTheo").value("Wifi,TV"));
        }
    }

    // ====== PATCH /api/phong/{ma}/status/{status} ======
    @Test
    void updateStatus_invalid_400_and_ok_200() throws Exception {
        // invalid enum
        mvc.perform(patch("/api/phong/P101/status/SAI_TRANG_THAI"))
           .andExpect(status().isBadRequest());

        // ok
        try (MockedStatic<PhongKhachSan> mocked = Mockito.mockStatic(PhongKhachSan.class)) {
            mocked.when(() -> PhongKhachSan.updateStatus("P101", RoomStatus.BAO_TRI)).thenReturn(true);

            mvc.perform(patch("/api/phong/P101/status/BAO_TRI"))
               .andExpect(status().isOk());

            mocked.verify(() -> PhongKhachSan.updateStatus("P101", RoomStatus.BAO_TRI), times(1));
        }
    }

    // ====== DELETE /api/phong/{maPhong} ======
    @Test
    void delete_noContent_204() throws Exception {
        try (MockedStatic<PhongKhachSan> mocked = Mockito.mockStatic(PhongKhachSan.class)) {
            mocked.when(() -> PhongKhachSan.deleteById("P101")).thenReturn(true);

            mvc.perform(delete("/api/phong/P101"))
               .andExpect(status().isNoContent());
        }
    }

    // ====== GET /api/phong/tim-trong ======
    @Test
    void timTrong_validate_and_ok() throws Exception {
        // ngày sai định dạng
        mvc.perform(get("/api/phong/tim-trong")
                .param("nhan", "2025/08/20")
                .param("tra", "2025-08-22"))
           .andExpect(status().isBadRequest());

        // tra <= nhan
        mvc.perform(get("/api/phong/tim-trong")
                .param("nhan", "2025-08-22")
                .param("tra", "2025-08-22"))
           .andExpect(status().isBadRequest());

        // loại sai
        mvc.perform(get("/api/phong/tim-trong")
                .param("nhan", "2025-08-20")
                .param("tra", "2025-08-22")
                .param("loai", "SAI"))
           .andExpect(status().isBadRequest());

        // ok
        PhongKhachSan p = new PhongKhachSan("P303", RoomType.SUITE, 1500000, RoomStatus.TRONG, 3, "Bath,TV");
        try (MockedStatic<PhongKhachSan> mocked = Mockito.mockStatic(PhongKhachSan.class)) {
            mocked.when(() -> PhongKhachSan.timPhongTrong(
                    java.time.LocalDate.parse("2025-08-20"),
                    java.time.LocalDate.parse("2025-08-22"),
                    2,
                    RoomType.DON))
                  .thenReturn(List.of(p));

            mvc.perform(get("/api/phong/tim-trong")
                    .param("nhan", "2025-08-20")
                    .param("tra", "2025-08-22")
                    .param("soNguoi", "2")
                    .param("loai", "DON"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].maPhong").value("P303"));
        }
    }
}
