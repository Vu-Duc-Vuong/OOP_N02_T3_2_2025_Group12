package com.example.servingwebcontent.controller;

import com.example.servingwebcontent.database.userAiven;
import com.example.servingwebcontent.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Controller
public class UserListController {

    @Autowired
    private userAiven ua;

    @GetMapping("/userlist")
    public String userlist(Model model) {
        try {
            ArrayList<User> listOfArray = ua.userAivenList();
            model.addAttribute("listOfArray", listOfArray);
        } catch (Exception e) {
            model.addAttribute("error", "Không thể tải danh sách người dùng: " + e.getMessage());
        }
        return "userlist";
    }

    @GetMapping("/userlist/edit/{id}")
    public String editUserForm(@PathVariable("id") String id, Model model) {
        try {
            User user = ua.getUserById(id);
            if (user != null) {
                model.addAttribute("user", user);
                return "edituser";
            } else {
                model.addAttribute("error", "Không tìm thấy người dùng với ID: " + id);
                return "redirect:/userlist";
            }
        } catch (Exception e) {
            model.addAttribute("error", "Lỗi khi tải thông tin người dùng: " + e.getMessage());
            return "redirect:/userlist";
        }
    }

    @PostMapping("/userlist/edit")
    public String updateUser(@ModelAttribute("user") User user, Model model) {
        try {
            ua.updateUser(user);
        } catch (Exception e) {
            model.addAttribute("error", "Cập nhật người dùng thất bại: " + e.getMessage());
        }
        return "redirect:/userlist";
    }

    @GetMapping("/userlist/delete/{id}")
    public String deleteUser(@PathVariable("id") String id, Model model) {
        try {
            ua.deleteUser(id);
        } catch (Exception e) {
            model.addAttribute("error", "Xóa người dùng thất bại: " + e.getMessage());
        }
        return "redirect:/userlist";
    }

    @GetMapping("/userlist/add")
    public String addUserForm(Model model) {
        model.addAttribute("user", new User());
        return "adduser";
    }

    @PostMapping("/userlist/add")
    public String addUser(@ModelAttribute("user") User user, Model model) {
        try {
            ua.addUser(user);
        } catch (Exception e) {
            model.addAttribute("error", "Thêm người dùng thất bại: " + e.getMessage());
            return "adduser"; // quay lại form nếu lỗi
        }
        return "redirect:/userlist";
    }
}
