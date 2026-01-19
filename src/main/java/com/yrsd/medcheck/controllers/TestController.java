package com.yrsd.medcheck.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {

    @GetMapping("/test/live")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("We are fucking live");
    }

    @GetMapping("/test/live/consumer")
    public ResponseEntity<String> testConsumer() {
        return ResponseEntity.ok("You are a consumer and you are active yami");
    }


    @GetMapping("/test/live/wholesaler")
    public ResponseEntity<String> testWholesaler() {
        return ResponseEntity.ok("We are fucking live");
    }

    @GetMapping("/test/live/investigator")
    public ResponseEntity<String> testInvestigator() {
        return ResponseEntity.ok("We are fucking live");
    }


}
