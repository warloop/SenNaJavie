package org.example.forum.controllers.Subject;

import org.example.forum.services.interfaces.ISubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class HideController {

    @Autowired
    ISubjectService SUBJECT_SERVICE;

}
