package org.example.forum.controllers;

import org.example.forum.dto.User.UserRegisterDto;
import org.example.forum.repos.Interfaces.UserDao;
import org.example.forum.entities.Login;
import org.example.forum.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class RegisterController {


    @Autowired
    private UserDao dao;

    @GetMapping("/register")
    public String registerPage(){
        return "register";
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestParam String name, @RequestParam String surname, @RequestParam String login,
                                           @RequestParam String email, @RequestParam String emailConfirm, @RequestParam String password,
                                           @RequestParam String passwordConfirm) {

        if(!password.equals(passwordConfirm)){
            return ResponseEntity.badRequest().body("Pola hasło i potwierdź hasło muszą być takie same!");
        }
        if(!email.equals(emailConfirm)){
            return ResponseEntity.badRequest().body("Pola email i potwierdź email muszą być takie same!");
        }

        UserRegisterDto userRegisterData = new UserRegisterDto(name, surname, login, email, password);

        if(dao.registerUser(userRegisterData)){
            return ResponseEntity.ok("Zarejestrowano pomyślnie");
        }

        return ResponseEntity.badRequest().body("Nie udało się zarejestrowac! Spróbuj ponownie!");

    }
}
