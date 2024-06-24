package org.example.forum.controllers.User;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.example.forum.dto.User.LoginInformationReturned;
import org.example.forum.dto.User.UserLoginDto;
import org.example.forum.repositories.RoleRepository;
import org.example.forum.services.SecurityService;
import org.example.forum.services.SubjectService;
import org.example.forum.services.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @Autowired
    IUserService USER_SERVICE;

    @Autowired
    SubjectService subjectService;

    @Autowired
    SecurityService SECURITY_SERVICE;
    @Autowired
    RoleRepository roleRepository;

    @GetMapping("/")
    public String loginPage() {
        return "login";
    }
    @GetMapping("/protected/mainpage")
    public String mainPage(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        if (session != null && "true".equals(session.getAttribute("isLogged"))) {
            model.addAttribute("username", session.getAttribute("username"));
        } else {
            model.addAttribute("username", null);
        }
        model.addAttribute("subjects", subjectService.getAllSubjects());
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

        try {
            if (!SecurityService.Regex.LOGIN.getPattern().matcher(login).matches()) {
                throw new Exception("Niepoprawna składnia loginu!");
            }

            if (!SecurityService.Regex.PASSWORD.getPattern().matcher(password).matches()) {
                throw new Exception("Niepoprawna składnia hasła!");
            }

            UserLoginDto userLoginDto = new UserLoginDto(login, password);
            LoginInformationReturned informationReturned = USER_SERVICE.loginUser(userLoginDto);

            int userId = informationReturned.getUser_id();
            if (informationReturned.getCode() == 200 && userId > 0) {
                HttpSession session = request.getSession();
                session.setAttribute("isLogged", "true");
                session.setAttribute("userId", userId);
                session.setAttribute("username", login);
                boolean admin = roleRepository.isAdmin((long) userId);
                session.setAttribute("isAdmin", admin);

                return "redirect:/protected/mainpage";
            }
        } catch (Exception e) {
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

        return "redirect:/";

    }
}
