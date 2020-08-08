package com.example.demo;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class IndexController {

    @RequestMapping(value= "/", method= RequestMethod.GET)
    ResponseEntity<String> main() {
        return new ResponseEntity<>("hello world!", HttpStatus.OK);
    }

    @RequestMapping(value= "/error", method= RequestMethod.GET) 
    ResponseEntity<String> error() {
    	return new ResponseEntity<>("There is a error!", HttpStatus.OK);
    }
    
}
