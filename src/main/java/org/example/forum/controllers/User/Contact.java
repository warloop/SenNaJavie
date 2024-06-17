package org.example.forum.controllers.User;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Contact {

    @GetMapping("/protected/contact")
    public String createSubjectPageShow() { return "contact"; }
}
