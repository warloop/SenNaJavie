package org.example.forum.controllers.Subject;

import jakarta.servlet.http.HttpServletRequest;
import org.example.forum.dto.Subject.SubjectAddDto;
import org.example.forum.dto.Subject.SubjectEditDto;
import org.example.forum.dto.System.InformationReturned;
import org.example.forum.entities.Subjects;
import org.example.forum.repositories.Interfaces.ISubjectRepository;
import org.example.forum.services.interfaces.ISubjectService;
import org.example.forum.services.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Controller
public class SubjectController {

    @Autowired
    ISubjectService SUBJECT_SERVICE;

    @Autowired
    IUserService USER_SERVICE;

    @Autowired
    ISubjectRepository SUBJECT_REPOSITORY;

    @GetMapping("/protected/subject/create")
    public String createSubjectPageShow() { return "subject-creator"; }

    @PostMapping("/protected/subject/create")
    public String createSubject(@RequestParam("subjectText") String subjectText, HttpServletRequest request) {
        int userId = Integer.parseInt(request.getSession().getAttribute("userId").toString());
        SubjectAddDto newSubject = new SubjectAddDto(userId, subjectText);
        SUBJECT_SERVICE.addSubject(newSubject);
        return "redirect:/protected/mainpage";
    }

    @GetMapping("/protected/subject/edit/{id}")
    public String editSubjectPage(@PathVariable("id") long id, Model model) {
        Subjects subject = SUBJECT_SERVICE.findSubjectById(id);
        if (subject != null) {
            model.addAttribute("subject", subject);
            return "edit-subject";
        } else {
            return "redirect:/protected/mainpage";
        }
    }

    @PostMapping("/protected/subject/edit")
    public String editSubject(@RequestParam("subjectId") long subjectId,
                              @RequestParam("subjectText") String subjectText,
                              HttpServletRequest request,
                              RedirectAttributes redirectAttributes) {
        try {
            int userId = Integer.parseInt(request.getSession().getAttribute("userId").toString());

            SubjectEditDto subjectEditDto = new SubjectEditDto(subjectId, userId, subjectText);

            boolean success = SUBJECT_SERVICE.editSubjectText(subjectEditDto);

            if (success) {
                redirectAttributes.addFlashAttribute("message", "Edycja tematu zakończona sukcesem.");
            } else {
                redirectAttributes.addFlashAttribute("error", "Edycja tematu nie powiodła się.");
            }

            return "redirect:/protected/mainpage";
        } catch (NumberFormatException e) {
            redirectAttributes.addFlashAttribute("error", "Niepoprawny format ID użytkownika.");
            return "redirect:/protected/mainpage";
        }
    }
}
