package com.example.servingwebcontent.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import com.example.servingwebcontent.database.userAiven;
import com.example.servingwebcontent.model.User;

import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

    @Autowired
    private userAiven ua;

    // Trang chủ
    @GetMapping("/")
    public String home() {
        return "home";
    }

    // Hiển thị form đăng ký
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    // Xử lý đăng ký
    @PostMapping("/register")
    public String processRegister(@ModelAttribute("user") User user, Model model) {
        try {
            boolean success = ua.register(user);
            if (!success) {
                model.addAttribute("error", "Email đã được sử dụng.");
                return "register";
            }
            return "redirect:/login";
        } catch (Exception e) {
            model.addAttribute("error", "❌ Lỗi khi đăng ký: " + e.getMessage());
            return "register";
        }
    }

    // Hiển thị form đăng nhập
    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

    // Xử lý đăng nhập
    @PostMapping("/login")
    public String processLogin(@ModelAttribute("user") User user, Model model, HttpSession session) {
        try {
            User loggedIn = ua.login(user.getEmail(), user.getPassword());
            if (loggedIn == null) {
                model.addAttribute("error", "Email hoặc mật khẩu không đúng.");
                return "login";
            }
            session.setAttribute("name", loggedIn.getName());
            return "redirect:/greeting";
        } catch (Exception e) {
            model.addAttribute("error", "❌ Lỗi khi đăng nhập: " + e.getMessage());
            return "login";
        }
    }
}
