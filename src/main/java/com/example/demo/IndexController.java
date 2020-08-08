package com.example.demo;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController

public class IndexController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<String> type(@RequestBody String req) {
        return new ResponseEntity<>("hello world!", HttpStatus.OK);
    }

}
