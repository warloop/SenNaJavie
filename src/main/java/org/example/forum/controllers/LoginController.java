package org.example.forum.controllers;

import org.example.forum.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @Autowired
    UserDao dao;


    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
    @GetMapping("/mainpage")
    public String mainPage() {
        return "mainpage";
    }
    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password) {
        boolean checkLogin = false;
        if (dao.findByUsernameAndPassword(username, password) != null) {
            checkLogin = true;
        }
        if (checkLogin) {
            return "mainpage";
        }

        return "login";
    }
}
