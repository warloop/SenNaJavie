package org.example.forum.controllers.Subject;

import org.example.forum.services.interfaces.ISubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ReadController {

    @Autowired
    ISubjectService SUBJECT_SERVICE;

    @GetMapping("/")
    public String loginPage() {
        return "login";
    }

}
