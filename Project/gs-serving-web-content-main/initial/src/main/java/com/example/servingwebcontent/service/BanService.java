package com.example.servingwebcontent.service;

import com.example.servingwebcontent.model.BanEntity;
import com.example.servingwebcontent.repository.BanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BanService {
    @Autowired
    private BanRepository banRepository;

    public List<BanEntity> getAll() {
        return banRepository.findAll();
    }

    public BanEntity save(BanEntity ban) {
        return banRepository.save(ban);
    }

    public Optional<BanEntity> getById(String maPhieu) {
        return banRepository.findById(maPhieu);
    }

    public void delete(String maPhieu) {
        banRepository.deleteById(maPhieu);
    }
}
