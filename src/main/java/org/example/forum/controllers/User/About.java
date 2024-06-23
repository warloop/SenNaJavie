package org.example.forum.controllers.User;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class About {
    @GetMapping("/protected/about")
    public String createSubjectPageShow() { return "about"; }
}
