package com.example.servingwebcontent;

import com.example.servingwebcontent.config.GlobalExceptionHandler;
import com.example.servingwebcontent.controller.BaoCaoController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test khớp với BaoCaoController hiện có:
 * - /api/bao-cao/doanh-thu
 * - /api/bao-cao/cong-suat-phong
 * - /api/bao-cao/dich-vu-ban-chay
 * Chỉ assert 4xx khi thiếu tham số (không đụng DB).
 */
@WebMvcTest(controllers = BaoCaoController.class)
@Import(GlobalExceptionHandler.class)
class BaoCaoControllerTest {

    @Autowired MockMvc mvc;

    @Test
    void doanhThu_missingParams_4xx() throws Exception {
        mvc.perform(get("/api/bao-cao/doanh-thu"))
           .andExpect(status().is4xxClientError());
        mvc.perform(get("/api/bao-cao/doanh-thu").param("year","2025"))
           .andExpect(status().is4xxClientError());
        mvc.perform(get("/api/bao-cao/doanh-thu").param("month","8"))
           .andExpect(status().is4xxClientError());
    }

    @Test
    void congSuatPhong_missingParams_4xx() throws Exception {
        mvc.perform(get("/api/bao-cao/cong-suat-phong"))
           .andExpect(status().is4xxClientError());
        mvc.perform(get("/api/bao-cao/cong-suat-phong").param("year","2025"))
           .andExpect(status().is4xxClientError());
        mvc.perform(get("/api/bao-cao/cong-suat-phong").param("month","8"))
           .andExpect(status().is4xxClientError());
    }

    @Test
    void dichVuBanChay_missingParams_4xx() throws Exception {
        mvc.perform(get("/api/bao-cao/dich-vu-ban-chay"))
           .andExpect(status().is4xxClientError());
        mvc.perform(get("/api/bao-cao/dich-vu-ban-chay").param("year","2025"))
           .andExpect(status().is4xxClientError());
        mvc.perform(get("/api/bao-cao/dich-vu-ban-chay").param("month","8"))
           .andExpect(status().is4xxClientError());
    }
}
