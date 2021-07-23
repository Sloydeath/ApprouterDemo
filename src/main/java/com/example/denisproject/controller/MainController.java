package com.example.denisproject.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @GetMapping("/hello")
    public ResponseEntity<?> hello() {
        String hello = "Hello, Denis!";
        return new ResponseEntity<>(hello, HttpStatus.OK);
    }

    @GetMapping("/goodbye")
    public ResponseEntity<?> goodbye() {
        String goodbye = "Goodbye, Denis!";
        return new ResponseEntity<>(goodbye, HttpStatus.OK);
    }
}
