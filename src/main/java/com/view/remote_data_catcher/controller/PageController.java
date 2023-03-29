package com.view.remote_data_catcher.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {


    @RequestMapping("/")
    public String login() {
        return "/pages/index.html";
    }


    @RequestMapping("/dataBook")
    public String dataBook() {
        return "/pages/data_book.html";
    }
}
