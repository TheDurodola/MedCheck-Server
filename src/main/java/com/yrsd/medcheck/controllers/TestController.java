package com.yrsd.medcheck.controllers;

import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {

    @GetMapping("/test/live")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("ok");
    }

}
