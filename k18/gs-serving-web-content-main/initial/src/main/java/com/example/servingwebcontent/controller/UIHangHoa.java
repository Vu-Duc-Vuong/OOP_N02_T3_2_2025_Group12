package com.example.servingwebcontent.controller;

import com.example.servingwebcontent.model.HangHoa;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/uihanghoa")
public class UIHangHoa {

    @GetMapping
    public String redirectToNewList() {
        return "redirect:/hanghoa";
    }

    @GetMapping("/list")
    public String redirectList() {
        return "redirect:/hanghoa";
    }
}
