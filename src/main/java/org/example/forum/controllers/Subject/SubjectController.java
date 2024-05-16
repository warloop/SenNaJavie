package org.example.forum.controllers.Subject;

import jakarta.servlet.http.HttpServletRequest;
import org.example.forum.dto.Subject.SubjectAddDto;
import org.example.forum.dto.System.InformationReturned;
import org.example.forum.services.interfaces.ISubjectService;
import org.example.forum.services.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class SubjectController {

    @Autowired
    ISubjectService SUBJECT_SERVICE;

    @Autowired
    IUserService USER_SERVICE;

    @GetMapping("/protected/subject/create")
    public String createSubjectPageShow() { return "subject-creator"; }

    @PostMapping("/protected/subject/create")
    public ResponseEntity createNewSubject(@RequestParam("subjectText") String subjectText, HttpServletRequest request)
    {
        //WALIDACJA PRZEKAZANYCH DANYCH
        if(subjectText == null || subjectText.length() > 128) {
            return ResponseEntity.badRequest().body("Niepoprawna długość tematu, temat nie może być pusty oraz nie może przekraczać 128 znaków.");
        }

        if(request.getSession().getAttribute("userId") == null) {
            return ResponseEntity.badRequest().body("Nie odnaleziono użytkownika w sesji.");
        }

        try{
            int UserId = Integer.parseInt(request.getSession().getAttribute("userId").toString());

            if(!USER_SERVICE.isUserExistsByUserIdAndNotBanned(UserId).get()){
                return ResponseEntity.badRequest().body("Nie odnaleziono użytkownika.");
            }

            SubjectAddDto newSubject = new SubjectAddDto(UserId, subjectText);

            InformationReturned returnedInfo = SUBJECT_SERVICE.addSubject(newSubject);

            if(returnedInfo.getCode() == 201) {
                return ResponseEntity.ok(returnedInfo.getMessage());
            }else{
                return ResponseEntity.badRequest().body(returnedInfo.getMessage());
            }

        }catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Niepoprawny format ID użytkownika");
        }

    }

}
