package com.example.servingwebcontent.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpSession;

@Controller
public class GreetingController {

	@GetMapping("/greeting")
	public String greeting(HttpSession session, Model model) {
		try {
			String name = (String) session.getAttribute("name");
			model.addAttribute("name", name != null ? name : "bạn");
			return "greeting";
		} catch (Exception e) {
			model.addAttribute("error", "❌ Lỗi khi hiển thị lời chào: " + e.getMessage());
			return "error";
		}
	}
}
