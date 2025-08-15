package com.example.servingwebcontent.controller;

import com.example.servingwebcontent.model.BanEntity;
import com.example.servingwebcontent.service.BanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/ban")
public class BanController {
    @Autowired
    private BanService banService;

    @GetMapping
    public List<BanEntity> getAll() {
        return banService.getAll();
    }

    @GetMapping("/{maPhieu}")
    public Optional<BanEntity> getById(@PathVariable String maPhieu) {
        return banService.getById(maPhieu);
    }

    @PostMapping
    public BanEntity create(@RequestBody BanEntity ban) {
        return banService.save(ban);
    }

    @DeleteMapping("/{maPhieu}")
    public void delete(@PathVariable String maPhieu) {
        banService.delete(maPhieu);
    }
}
