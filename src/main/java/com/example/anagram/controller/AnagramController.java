package com.example.anagram.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/")
public class AnagramController {

    @GetMapping("name")
    public ResponseEntity<String> getName(){
        return ResponseEntity.ok("Solomon");
    }

}
