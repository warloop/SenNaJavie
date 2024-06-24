package org.example.forum.controllers.User;

<<<<<<< HEAD
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.example.forum.dao.UserDao;
import org.example.forum.dto.System.InformationReturned;
import org.example.forum.dto.User.UserRegisterDto;
import org.example.forum.entities.Role;
import org.example.forum.entities.User;
import org.example.forum.repositories.RoleRepository;
=======
import org.example.forum.dto.System.InformationReturned;
import org.example.forum.dto.User.UserRegisterDto;
>>>>>>> main
import org.example.forum.services.SecurityService;
import org.example.forum.services.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
<<<<<<< HEAD
import org.springframework.ui.Model;
=======
>>>>>>> main
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

<<<<<<< HEAD
import java.util.List;
import java.util.Optional;
=======
>>>>>>> main

@Controller
public class RegisterController {

    @Autowired
<<<<<<< HEAD
    private UserDao dao;

    @Autowired
    private IUserService USER_SERVICE;

    @Autowired
    private RoleRepository repository;

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }
    @Autowired
    RoleRepository roleRepository;
=======
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
>>>>>>> main

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestParam String name, @RequestParam String surname, @RequestParam String login,
                                           @RequestParam String email, @RequestParam String emailConfirm, @RequestParam String password,
<<<<<<< HEAD
                                           @RequestParam String passwordConfirm, HttpServletRequest request) {
        // List<Role> roles = roleRepository.getAllRoles();
        //Role user = roles.get(3);

        // Początkowa walidacja danych
        if (!(SecurityService.Regex.NAME.getPattern().matcher(name).matches())) {
            return ResponseEntity.badRequest().body("Niepoprawna składnia imienia!");
        }

        if (!(SecurityService.Regex.SURNAME.getPattern().matcher(surname).matches())) {
            return ResponseEntity.badRequest().body("Niepoprawna składnia nazwiska!");
        }

        if (!(SecurityService.Regex.LOGIN.getPattern().matcher(login).matches())) {
            return ResponseEntity.badRequest().body("Niepoprawna składnia loginu!");
        }

        if (!(SecurityService.Regex.PASSWORD.getPattern().matcher(password).matches())) {
            return ResponseEntity.badRequest().body("Niepoprawna składnia hasła!");
        }

        if (!(SecurityService.Regex.EMAIL.getPattern().matcher(email).matches())) {
            return ResponseEntity.badRequest().body("Niepoprawna składnia email!");
        }

        if (!password.equals(passwordConfirm)) {
            return ResponseEntity.badRequest().body("Pola hasło i potwierdź hasło muszą być takie same!");
        }

        if (!email.equals(emailConfirm)) {
            return ResponseEntity.badRequest().body("Pola email i potwierdź email muszą być takie same!");
        }

        // Utworzenie DTO
        UserRegisterDto userRegisterData = new UserRegisterDto(name, surname, login, email, password);

        // Pobieranie informacji zwrotnej z wykonanej próby rejestracji użytkownika.
        InformationReturned infoReturned = USER_SERVICE.registerUser(userRegisterData);

        if (infoReturned.getCode() == 200) {
            String successPage = "<html><body>" +
                    "<h1>Poprawnie zarejestrowano!</h1>" +
                    "<p>Za chwilę zostaniesz przekierowany na stronę logowania...</p>" +
                    "<script type='text/javascript'>" +
                    "setTimeout(function() { window.location.href = '/'; }, 3000);" +
                    "</script>" +
                    "</body></html>";
            return ResponseEntity.ok(successPage);
        }

        return ResponseEntity.badRequest().body("Kod błędu : " + infoReturned.getCode() + ", Treść błędu : " + infoReturned.getMessage());
    }

    @GetMapping("/protected/assign-role")
    public String showAssignRoleForm(Model model) {
        model.addAttribute("users", dao.getAll());
        model.addAttribute("roles", repository.getAllRoles());
        return "assign-role";
    }

    @PostMapping("/protected/assign-role")
    public String assignRoleToUser(@RequestParam Long userId, @RequestParam Long roleId) {
        boolean success = repository.assignRoleToUser(userId, roleId);

        if (success) {
            return "redirect:/protected/mainpage";
        } else {
            return "error"; // Możesz przekierować do odpowiedniej strony błędu
        }
=======
                                           @RequestParam String passwordConfirm)
    {

        //początkowa walidacja danych

        if(!(SecurityService.Regex.NAME.getPattern().matcher(name).matches())){
            return ResponseEntity.badRequest().body("Niepoprawna składnia imienia!");
        }

        if(!(SecurityService.Regex.SURNAME.getPattern().matcher(surname).matches())){
            return ResponseEntity.badRequest().body("Niepoprawna składnia nazwiska!");
        }

        if(!(SecurityService.Regex.LOGIN.getPattern().matcher(login).matches())){
            return ResponseEntity.badRequest().body("Niepoprawna składnia loginu!");
        }

        if(!(SecurityService.Regex.PASSWORD.getPattern().matcher(password).matches())){
            return ResponseEntity.badRequest().body("Niepoprawna składnia hasła!");
        }

        if(!(SecurityService.Regex.EMAIL.getPattern().matcher(email).matches())){
            return ResponseEntity.badRequest().body("Niepoprawna składnia email!");
        }

        if(!password.equals(passwordConfirm)){
            return ResponseEntity.badRequest().body("Pola hasło i potwierdź hasło muszą być takie same!");
        }
        if(!email.equals(emailConfirm)){
            return ResponseEntity.badRequest().body("Pola email i potwierdź email muszą być takie same!");
        }


        //Utworzenie DTO
        UserRegisterDto userRegisterData = new UserRegisterDto(name, surname, login, email, password);

        //Pobieranie informacji zwrotnej z wykonanej próby rejestracji użytkownika.
        InformationReturned infoReturned = USER_SERVICE.registerUser(userRegisterData);

        if(infoReturned.getCode() == 200){
            return ResponseEntity.ok(infoReturned.getMessage());
        }

        return ResponseEntity.badRequest().body("Kod błędu : "+infoReturned.getCode()+", Treść błędu : "+infoReturned.getMessage());

>>>>>>> main
    }
}
