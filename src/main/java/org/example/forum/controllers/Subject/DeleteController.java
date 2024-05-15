package org.example.forum.controllers.Subject;

import org.example.forum.services.interfaces.ISubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class DeleteController {

    @Autowired
    ISubjectService SUBJECT_SERVICE;


}
