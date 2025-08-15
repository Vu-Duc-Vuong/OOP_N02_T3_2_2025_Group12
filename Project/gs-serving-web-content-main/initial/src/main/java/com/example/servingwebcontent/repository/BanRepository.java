package com.example.servingwebcontent.repository;

import com.example.servingwebcontent.model.BanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BanRepository extends JpaRepository<BanEntity, String> {
}
