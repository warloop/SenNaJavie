package org.example.forum.controllers.Page;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.example.forum.entities.Articles;
import org.example.forum.entities.Comments;
import org.example.forum.entities.Role;
import org.example.forum.entities.User;
import org.example.forum.repositories.Interfaces.ICommentRepository;
import org.example.forum.repositories.RoleRepository;
import org.example.forum.services.interfaces.IArticleService;
import org.example.forum.services.interfaces.ISubjectService;
import org.example.forum.services.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.swing.text.View;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class PageController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IArticleService articleService;
    @Autowired
    private ICommentRepository commentRepository;

    @Autowired
    RoleRepository roleRepository;
    @Autowired
    private ISubjectService subjectService;

    @GetMapping("/mainpage")
    public String home(HttpServletRequest request, Model model, Principal principal) {
        User user1 = userService.getUserByUsername(principal.getName()).orElse(null);
        if (user1 != null) {
            List<Role> roles = roleRepository.getRolesByUserId(user1.getId());
            model.addAttribute("username", principal.getName());
            model.addAttribute("roles", roles);

        }
        HttpSession session = request.getSession(false);
        if (session != null && "true".equals(session.getAttribute("isLogged"))) {
            Optional<User> userOptional = userService.getUserByUsername((String) session.getAttribute("username"));
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                model.addAttribute("username", session.getAttribute("username"));
                model.addAttribute("name", user.getName());
                model.addAttribute("surname", user.getSurname());
                model.addAttribute("email", user.getEmail());
                model.addAttribute("accountType", user.getAccountType());
                model.addAttribute("registerDate", user.getRegister_date());
                model.addAttribute("isDeleted", user.getIs_deleted());
                model.addAttribute("deleteDate", user.getDelete_date());
            } else {
                model.addAttribute("username", null);
                model.addAttribute("name", null);
                model.addAttribute("surname", null);
                model.addAttribute("email", null);
                model.addAttribute("accountType", null);
                model.addAttribute("registerDate", null);
                model.addAttribute("isDeleted", null);
                model.addAttribute("deleteDate", null);
            }
        } else {
            model.addAttribute("username", null);
            model.addAttribute("name", null);
            model.addAttribute("surname", null);
            model.addAttribute("email", null);
            model.addAttribute("accountType", null);
            model.addAttribute("registerDate", null);
            model.addAttribute("isDeleted", null);
            model.addAttribute("deleteDate", null);
        }
        return "mainpage";
    }


    @GetMapping("/my-profile")
    public String myProfile(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        if (session != null && "true".equals(session.getAttribute("isLogged"))) {
            Optional<User> userOptional = userService.getUserByUsername((String) session.getAttribute("username"));
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                model.addAttribute("username", session.getAttribute("username"));
                model.addAttribute("name", user.getName());
                model.addAttribute("surname", user.getSurname());
                model.addAttribute("email", user.getEmail());
                model.addAttribute("accountType", user.getAccountType());
                model.addAttribute("registerDate", user.getRegister_date());
                model.addAttribute("isDeleted", user.getIs_deleted());
                model.addAttribute("deleteDate", user.getDelete_date());

                List<Articles> articles = articleService.findByUserId(user.getId());

                model.addAttribute("articles", articles);

                List<Comments> comments = commentRepository.getCommentsByUserId(user.getId());
                model.addAttribute("comments", comments);

                return "my-profile";
            } else {
                return "redirect:/login";
            }
        } else {
            return "redirect:/login";
        }
    }

}
