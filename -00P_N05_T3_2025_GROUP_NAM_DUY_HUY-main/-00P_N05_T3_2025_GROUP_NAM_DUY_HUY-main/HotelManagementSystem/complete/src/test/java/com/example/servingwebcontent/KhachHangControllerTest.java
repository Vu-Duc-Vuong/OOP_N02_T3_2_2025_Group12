package com.example.servingwebcontent;

import com.example.servingwebcontent.controller.KhachHangController;
import com.example.servingwebcontent.model.KhachHang;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(KhachHangController.class)
class KhachHangControllerTest {

    @Autowired
    MockMvc mvc;

    @Test
    void getAll_ok() throws Exception {
        KhachHang a = new KhachHang();
        a.setDinhDanh("ID1");
        a.setHoTen("Nguyen A");

        try (MockedStatic<KhachHang> mocked = Mockito.mockStatic(KhachHang.class)) {
            mocked.when(KhachHang::findAll).thenReturn(List.of(a));

            mvc.perform(get("/api/khach"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].dinhDanh").value("ID1"))
               .andExpect(jsonPath("$[0].hoTen").value("Nguyen A"));
        }
    }

    @Test
    void create_missingFields_400() throws Exception {
        mvc.perform(post("/api/khach")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
           .andExpect(status().isBadRequest());
    }

    @Test
    void create_conflict_409_whenExists() throws Exception {
        try (MockedStatic<KhachHang> mocked = Mockito.mockStatic(KhachHang.class)) {
            mocked.when(() -> KhachHang.findById("0123")).thenReturn(new KhachHang());

            mvc.perform(post("/api/khach")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"dinhDanh\":\"0123\",\"hoTen\":\"A\"}"))
               .andExpect(status().isConflict());
        }
    }

    @Test
    void create_ok_201() throws Exception {
        try (MockedStatic<KhachHang> mocked = Mockito.mockStatic(KhachHang.class)) {
            mocked.when(() -> KhachHang.findById("0123")).thenReturn(null);
            mocked.when(() -> KhachHang.insert(Mockito.any(KhachHang.class))).thenReturn(true);

            mvc.perform(post("/api/khach")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"dinhDanh\":\"0123\",\"hoTen\":\"A\"}"))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.dinhDanh").value("0123"))
               .andExpect(jsonPath("$.hoTen").value("A"));
        }
    }

    @Test
    void create_serverError_500_whenInsertThrows() throws Exception {
        try (MockedStatic<KhachHang> mocked = Mockito.mockStatic(KhachHang.class)) {
            mocked.when(() -> KhachHang.findById("e1")).thenReturn(null);
            mocked.when(() -> KhachHang.insert(Mockito.any(KhachHang.class)))
                  .thenThrow(new RuntimeException("boom"));

            mvc.perform(post("/api/khach")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"dinhDanh\":\"e1\",\"hoTen\":\"Err\"}"))
               .andExpect(status().isInternalServerError());
               // nếu đã thêm GlobalExceptionHandler như mình gửi trước đó,
               // có thể assert JSON:
               // .andExpect(jsonPath("$.error").value("Lỗi hệ thống"));
        }
    }
}
