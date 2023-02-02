package com.example.demo.controller;

import com.example.demo.UDPserverApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.concurrent.CopyOnWriteArrayList;

@Controller
public class index{
    @RequestMapping("/index")
    public String returnindex(){
        return "index";
    }

    @GetMapping("/list")
    @ResponseBody
    public CopyOnWriteArrayList<String> index2(){
        return new CopyOnWriteArrayList<>(UDPserverApplication.userlist.keySet());
    }
}
