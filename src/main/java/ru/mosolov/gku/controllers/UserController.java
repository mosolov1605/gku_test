package ru.mosolov.gku.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/user")
public class UserController {

    @PostMapping("login")
    public String login(){
        return "login";
    }
}
