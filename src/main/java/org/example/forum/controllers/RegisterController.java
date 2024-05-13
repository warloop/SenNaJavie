package org.example.forum.controllers;

import org.example.forum.dao.UserDao;
import org.example.forum.entity.Login;
import org.example.forum.entity.User;
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

        User user = new User();
        Login loginObj = new Login();

        user.setName(name);
        user.setSurname(surname);
        user.setEmail(email);

        loginObj.setLogin(login);
        loginObj.setPassword(password);

        if(dao.registerUser(user, loginObj)){
            return ResponseEntity.ok("Zarejestrowano pomyślnie");
        }

        return ResponseEntity.badRequest().body("Nie udało się zarejestrowac ! Spróbuj ponownie!");

    }
}
