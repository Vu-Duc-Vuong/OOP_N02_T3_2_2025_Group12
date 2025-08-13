package com.example.servingwebcontent.repository;

import com.example.servingwebcontent.model.NhapEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface NhapRepository extends JpaRepository<NhapEntity, Long> {
    List<NhapEntity> findByNgayNhap(LocalDate date);
}
