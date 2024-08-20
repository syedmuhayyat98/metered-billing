package sam.example.metered_billing.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("api/thymeleaf")
public class ThymeleafController {

    @GetMapping("/success")
    public String successPage() {
        return "success";
    }

    @GetMapping("/cancel")
    public String cancelPage() {
        return "cancel";
    }
}
