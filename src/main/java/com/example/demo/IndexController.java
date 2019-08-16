package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Controller
public class IndexController {

    static String confirmationResponse = "960cb2e7";

    @PostMapping(value = "/")
    public String index(@RequestBody Request request) {
        if(request.getType().equals("confirmation"))
            return confirmationResponse;
        return null;
    }
}
