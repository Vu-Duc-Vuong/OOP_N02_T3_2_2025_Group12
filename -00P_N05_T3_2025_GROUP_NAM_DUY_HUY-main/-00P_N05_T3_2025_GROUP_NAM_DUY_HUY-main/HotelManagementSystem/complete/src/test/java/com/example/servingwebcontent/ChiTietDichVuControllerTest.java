package com.example.servingwebcontent;

import com.example.servingwebcontent.controller.ChiTietDichVuController;
import com.example.servingwebcontent.model.*;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ChiTietDichVuController.class)
class ChiTietDichVuControllerTest {

    @Autowired
    MockMvc mvc;

    @Test
    void add_bookingNotFound_400() throws Exception {
        try (MockedStatic<DatPhong> mDp = Mockito.mockStatic(DatPhong.class)) {
            mDp.when(() -> DatPhong.findById("DP1")).thenReturn(null);

            mvc.perform(post("/api/ctdv/booking/DP1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"maDichVu\":\"LAUNDRY\",\"soLuong\":2}"))
               .andExpect(status().isBadRequest());
        }
    }

    @Test
    void add_ok_201() throws Exception {
        try (MockedStatic<DatPhong> mDp = Mockito.mockStatic(DatPhong.class);
             MockedStatic<DichVu> mDv = Mockito.mockStatic(DichVu.class);
             MockedStatic<ChiTietDichVu> mC = Mockito.mockStatic(ChiTietDichVu.class)) {

            mDp.when(() -> DatPhong.findById("DP1")).thenReturn(new DatPhong());
            DichVu dv = new DichVu("LAUNDRY", "Giặt ủi", 50000, ServiceType.THEO_LAN);
            mDv.when(() -> DichVu.findById("LAUNDRY")).thenReturn(dv);
            mC.when(() -> ChiTietDichVu.insert(Mockito.any(ChiTietDichVu.class))).thenReturn(1L);

            mvc.perform(post("/api/ctdv/booking/DP1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"maDichVu\":\"LAUNDRY\",\"soLuong\":3}"))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.maDichVu").value("LAUNDRY"))
               .andExpect(jsonPath("$.soLuong").value(3))
               .andExpect(jsonPath("$.thanhTien").value(150000.0));
        }
    }

    @Test
    void sum_ok() throws Exception {
        try (MockedStatic<DatPhong> mDp = Mockito.mockStatic(DatPhong.class);
             MockedStatic<ChiTietDichVu> mC = Mockito.mockStatic(ChiTietDichVu.class)) {

            mDp.when(() -> DatPhong.findById("DP1")).thenReturn(new DatPhong());
            mC.when(() -> ChiTietDichVu.sumThanhTienByBooking("DP1")).thenReturn(150000.0);

            mvc.perform(get("/api/ctdv/booking/DP1/tong"))
               .andExpect(status().isOk())
               .andExpect(content().string("150000.0"));
        }
    }
}
