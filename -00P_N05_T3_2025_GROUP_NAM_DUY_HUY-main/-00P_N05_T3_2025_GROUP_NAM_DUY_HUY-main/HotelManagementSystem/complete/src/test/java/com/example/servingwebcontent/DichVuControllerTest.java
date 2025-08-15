package com.example.servingwebcontent;

import com.example.servingwebcontent.config.GlobalExceptionHandler;
import com.example.servingwebcontent.controller.DichVuController;
import com.example.servingwebcontent.model.DichVu;
import com.example.servingwebcontent.model.ServiceType;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DichVuController.class)
@Import(GlobalExceptionHandler.class)
class DichVuControllerTest {

    @Autowired MockMvc mvc;

    @Test
    void list_ok() throws Exception {
        DichVu dv = new DichVu("FOOD", "Ăn uống", 120000, ServiceType.THEO_LAN);
        try (MockedStatic<DichVu> m = Mockito.mockStatic(DichVu.class)) {
            m.when(DichVu::findAll).thenReturn(List.of(dv));
            mvc.perform(get("/api/dich-vu"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].maDichVu").value("FOOD"));
        }
    }

    @Test
    void create_ok_201() throws Exception {
        try (MockedStatic<DichVu> m = Mockito.mockStatic(DichVu.class)) {
            m.when(() -> DichVu.findById("SPA")).thenReturn(null);
            m.when(() -> DichVu.insert(any(DichVu.class))).thenReturn(true);
            mvc.perform(post("/api/dich-vu").contentType(MediaType.APPLICATION_JSON)
               .content("{\"maDichVu\":\"SPA\",\"tenDichVu\":\"Spa\",\"gia\":300000,\"loai\":\"THEO_GIO\"}"))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.maDichVu").value("SPA"));
        }
    }

    @Test
    void update_ok_200() throws Exception {
        try (MockedStatic<DichVu> m = Mockito.mockStatic(DichVu.class)) {
            m.when(() -> DichVu.findById("SPA")).thenReturn(new DichVu("SPA","Spa",300000,ServiceType.THEO_GIO));
            m.when(() -> DichVu.update(any(DichVu.class))).thenReturn(true);
            mvc.perform(put("/api/dich-vu/SPA").contentType(MediaType.APPLICATION_JSON)
               .content("{\"tenDichVu\":\"Spa VIP\",\"gia\":350000,\"loai\":\"THEO_LAN\"}"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.tenDichVu").value("Spa VIP"));
        }
    }

    @Test
    void delete_ok_204() throws Exception {
        try (MockedStatic<DichVu> m = Mockito.mockStatic(DichVu.class)) {
            m.when(() -> DichVu.deleteById("SPA")).thenReturn(true);
            mvc.perform(delete("/api/dich-vu/SPA")).andExpect(status().isNoContent());
        }
    }
}
