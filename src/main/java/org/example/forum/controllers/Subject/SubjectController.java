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

import java.util.HashMap;
import java.util.Map;
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
    public ResponseEntity<Map<String, String>> createNewSubject(@RequestParam("subjectText") String subjectText, HttpServletRequest request) {
        Map<String, String> response = new HashMap<>();

        // WALIDACJA PRZEKAZANYCH DANYCH
        if (subjectText == null || subjectText.length() > 128) {
            response.put("message", "Niepoprawna długość tematu, temat nie może być pusty oraz nie może przekraczać 128 znaków.");
            return ResponseEntity.badRequest().body(response);
        }

        if (request.getSession().getAttribute("userId") == null) {
            response.put("message", "Nie odnaleziono użytkownika w sesji.");
            return ResponseEntity.badRequest().body(response);
        }

        try {
            int UserId = Integer.parseInt(request.getSession().getAttribute("userId").toString());

            if (!USER_SERVICE.isUserExistsByUserIdAndNotBanned(UserId).get()) {
                response.put("message", "Nie odnaleziono użytkownika.");
                return ResponseEntity.badRequest().body(response);
            }

            SubjectAddDto newSubject = new SubjectAddDto(UserId, subjectText);

            InformationReturned returnedInfo = SUBJECT_SERVICE.addSubject(newSubject);

            if (returnedInfo.getCode() == 201) {
                response.put("message", returnedInfo.getMessage());
                response.put("redirectUrl", "/protected/mainpage"); // Dodanie adresu URL do przekierowania
                return ResponseEntity.ok(response);
            } else {
                response.put("message", returnedInfo.getMessage());
                return ResponseEntity.badRequest().body(response);
            }

        } catch (NumberFormatException e) {
            response.put("message", "Niepoprawny format ID użytkownika");
            return ResponseEntity.badRequest().body(response);
        }
    }
}
