package com.example.demo;

import org.springframework.web.bind.annotation.*;

@RestController

public class IndexController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String type(@RequestBody String req) {
        return "hello world!";
    }

}
