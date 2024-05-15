package org.example.forum.controllers.User;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.example.forum.dto.System.InformationReturned;
import org.example.forum.dto.User.UserLoginDto;
import org.example.forum.services.SecurityService;
import org.example.forum.services.interfaces.ISecurityService;
import org.example.forum.services.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.regex.Pattern;

@Controller
public class LoginController {

    @Autowired
    IUserService USER_SERVICE;
    SecurityService SECURITY_SERVICE;

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
    @GetMapping("/protected/mainpage")
    public String mainPage() {
        return "mainpage";
    }

    /**
     * Metoda przekazuje dane logowania do serwisu odpowiedzialnego za logowanie użytkowników.
     * @param login
     * @param password
     * @return HTML Page
     * @author Artur Leszczak
     * @version 1.0.0
     */
    @PostMapping("/login")
    public String login(@RequestParam String login, @RequestParam String password, HttpServletRequest request) {

        try{

            if(!(SecurityService.Regex.LOGIN.getPattern().matcher(login).matches())){
                throw new Exception("Niepoprawna składnia loginu!");
            }

            if(!(SecurityService.Regex.PASSWORD.getPattern().matcher(password).matches())){
                throw new Exception("Niepoprawna składnia hasła!");
            }

            UserLoginDto userLoginDto = new UserLoginDto(login, password);

            InformationReturned informationReturned = USER_SERVICE.loginUser(userLoginDto);

            if(informationReturned.getCode() == 200) {

                HttpSession session = request.getSession();
                session.setAttribute("isLogged", "true");

                return "redirect:/protected/mainpage";
            }

        }catch (Exception e) {
            return "login";
        }

        return "login";
    }

    /**
     * Metoda wylogowuje użytkownika, usuwa wartośc sesji.
     * @param request
     * @return Redirect to page => login
     * @author Artur Leszczak
     * @version 1.0.0
     */
    @GetMapping("/logout")
    public String logout(HttpServletRequest request)
    {
        HttpSession session = request.getSession();
        if(session != null)
        {
            session.removeAttribute("isLogged");
            session.invalidate();
        }

        return "redirect:/login";
    }
}