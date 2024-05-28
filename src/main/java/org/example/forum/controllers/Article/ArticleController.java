package org.example.forum.controllers.Article;

import jakarta.servlet.http.HttpServletRequest;
import org.example.forum.dto.Article.ArticleAddDto;
import org.example.forum.dto.Subject.SubjectAddDto;
import org.example.forum.dto.System.InformationReturned;
import org.example.forum.services.interfaces.IActionService;
import org.example.forum.services.interfaces.IArticleService;
import org.example.forum.services.interfaces.ISubjectService;
import org.example.forum.services.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ArticleController {

    @Autowired
    IArticleService ARTICLE_SERVICE;

    @Autowired
    IUserService USER_SERVICE;

    @Autowired
    ISubjectService SUBJECT_SERVICE;

    @GetMapping("/protected/article/create")
    public String createSubjectPageShow() { return "article-creator"; }

    @PostMapping("/protected/article/create")
    public ResponseEntity createNewSubject(@RequestParam("articleTitle") String articleTitile,
                                           @RequestParam("subjectId") Long SubjectId, HttpServletRequest request)
    {
        //WALIDACJA PRZEKAZANYCH DANYCH
        if(articleTitile == null || articleTitile.length() > 256) {
            return ResponseEntity.badRequest().body("Niepoprawna długość tematu artykułu, " +
                    "temat nie może być pusty oraz nie może przekraczać 256 znaków.");
        }

        if(request.getSession().getAttribute("userId") == null) {
            return ResponseEntity.badRequest().body("Nie odnaleziono użytkownika w sesji.");
        }

        if(SubjectId == null || SubjectId < 1) {
            return ResponseEntity.badRequest().body("Nie istnieje temat, który chcesz zartykułować!");
        }

        if(SUBJECT_SERVICE.isSubjectExists(SubjectId).isEmpty()) return ResponseEntity.badRequest().body(
                                                                        "Nie istnieje temat, " +
                                                                        "który chcesz zartykułować!");

        try{
            int UserId = Integer.parseInt(request.getSession().getAttribute("userId").toString());

            if(!USER_SERVICE.isUserExistsByUserIdAndNotBanned(UserId).get()){
                return ResponseEntity.badRequest().body("Nie odnaleziono użytkownika.");
            }

            ArticleAddDto newSubject = new ArticleAddDto(UserId, SubjectId, articleTitile,
                                                false, false, false);

            InformationReturned returnedInfo = ARTICLE_SERVICE.addArticle(newSubject);

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
