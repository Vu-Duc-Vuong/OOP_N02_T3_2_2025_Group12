package com.example.servingwebcontent;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CoreFlowUIController {

    // Trang giao diện chính
    @GetMapping({"/", "/ui/core-flow"})
    public String coreFlow() {
        return "coreflow"; // -> templates/coreflow.html
    }
}
