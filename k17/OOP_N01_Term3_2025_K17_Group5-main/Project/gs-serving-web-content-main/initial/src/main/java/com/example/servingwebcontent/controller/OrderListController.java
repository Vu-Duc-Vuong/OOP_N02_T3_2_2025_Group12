package com.example.servingwebcontent.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.servingwebcontent.database.orderAiven;
import com.example.servingwebcontent.model.Order;
import com.example.servingwebcontent.model.OrderPayment;

import java.util.ArrayList;

@Controller
public class OrderListController {

    @Autowired
    private orderAiven oa;

    @GetMapping("/orderlist")
    public String orderlist(Model model) {
        try {
            ArrayList<Order> listOfOrder = oa.orderAivenList();
            model.addAttribute("listOfOrder", listOfOrder);
        } catch (Exception e) {
            model.addAttribute("error", "❌ Không thể tải danh sách đơn hàng: " + e.getMessage());
        }
        return "orderlist";
    }

    @GetMapping("/ordersearch")
    public String orderSearch(@RequestParam(name = "userId", required = false) String userId, Model model) {
        ArrayList<OrderPayment> orderSearch = new ArrayList<>();

        try {
            System.out.println("Received userId: " + userId);
            
            if (userId != null && !userId.isEmpty()) {
                // Kiểm tra nếu userId không phải số
                if (!userId.matches("\\d+")) {
                    model.addAttribute("error", "⚠️ Vui lòng nhập số nguyên hợp lệ cho User ID.");
                    model.addAttribute("enteredUserId", userId);
                    return "ordersearch";
                }

                orderSearch = oa.orderListByUserId(userId);
            }

            model.addAttribute("orderSearch", orderSearch);
            model.addAttribute("enteredUserId", userId); // giữ lại giá trị userId đã nhập

        } catch (Exception e) {
            model.addAttribute("error", "❌ Đã xảy ra lỗi khi tìm kiếm đơn hàng: " + e.getMessage());
            model.addAttribute("enteredUserId", userId); // vẫn giữ lại nếu người dùng nhập sai
        }
        
        return "ordersearch";
    }
}
