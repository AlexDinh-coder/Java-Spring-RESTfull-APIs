package vn.tuantrung.jobhunter.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.tuantrung.jobhunter.util.error.IdInvalidException;

@RestController

public class HelloController {

    @GetMapping("/")
    
    public String getHelloWorld() throws IdInvalidException {

        return "update Hello World (Hỏi Dân IT & Eric)";
    }
}
