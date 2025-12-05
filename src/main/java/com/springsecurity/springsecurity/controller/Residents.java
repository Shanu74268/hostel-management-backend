package com.springsecurity.springsecurity.controller;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/residents")
public class Residents {
    private static final Logger log = LoggerFactory.getLogger(Residents.class);

    @GetMapping("/sayhello")
    public String hello(){
        log.info("trying");
        return "helllooooo";
    }
}
