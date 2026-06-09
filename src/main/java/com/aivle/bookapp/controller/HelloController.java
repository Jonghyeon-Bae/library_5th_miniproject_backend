package com.aivle.bookapp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    // @GetMapping("/hello")
    // public String hello() {
    // return "Hello, SpringBoot!";
    // }

    @GetMapping("/hello/{name}")
    public String hello(@PathVariable("name") String name){
        return "hello" + name;
    }

    @GetMapping("/greet")
    public String greet(@RequestParam(name = "lang", defaultValue = "en") String lang){
        if (lang.equals("ko")){
            return "안녕하세요";
        }else{
            return "Hello";
        }

    }
}
