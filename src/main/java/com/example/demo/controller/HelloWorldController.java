package com.example.demo.controller;

import org.springframework.stereotype.Controller;

@Controller
public class HelloWorldController {

  public String hello() {
    return "Hello World";
  }
}
