package org.example.forum.controllers.User;

import org.example.forum.dto.System.InformationReturned;
import org.example.forum.dto.User.UserRegisterDto;
import org.example.forum.services.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class RegisterController {

    @Autowired
    IUserService USER_SERVICE;

    @GetMapping("/register")
    public String registerPage(){
        return "register";
    }

    /**
     * Metoda przekształca otrzymane w request dane rejestracji, wstępnie waliduje dane oraz przekazuje je do serwisu.
     * @param name
     * @param surname
     * @param login
     * @param email
     * @param emailConfirm
     * @param password
     * @param passwordConfirm
     * @return ResponseEntity<String> Zwraca komunikat tekstowy
     * @author Artur Leszczak
     * @version 1.0.0
     */

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestParam String name, @RequestParam String surname, @RequestParam String login,
                                           @RequestParam String email, @RequestParam String emailConfirm, @RequestParam String password,
                                           @RequestParam String passwordConfirm)
    {
        //początkowa walidacja danych
        if(!password.equals(passwordConfirm)){
            return ResponseEntity.badRequest().body("Pola hasło i potwierdź hasło muszą być takie same!");
        }
        if(!email.equals(emailConfirm)){
            return ResponseEntity.badRequest().body("Pola email i potwierdź email muszą być takie same!");
        }

        UserRegisterDto userRegisterData = new UserRegisterDto(name, surname, login, email, password);

        //Pobieranie informacji zwrotnej z wykonanej próby rejestracji użytkownika.
        InformationReturned infoReturned = USER_SERVICE.registerUser(userRegisterData);

        if(infoReturned.getCode() == 200){
            return ResponseEntity.ok(infoReturned.getMessage());
        }

        return ResponseEntity.badRequest().body("Kod błędu : "+infoReturned.getCode()+", Treść błędu : "+infoReturned.getMessage());

    }
}
