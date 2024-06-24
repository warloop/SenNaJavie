package org.example.forum.controllers.Article;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.example.forum.dto.Article.ArticleAddDto;
import org.example.forum.dto.System.InformationReturned;
import org.example.forum.entities.*;
import org.example.forum.repositories.Interfaces.ICommentRepository;
import org.example.forum.services.interfaces.IArticleService;
import org.example.forum.services.interfaces.ISectionService;
import org.example.forum.services.interfaces.ISubjectService;
import org.example.forum.services.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Controller
public class ArticleController {

    @Autowired
    IArticleService ARTICLE_SERVICE;

    @Autowired
    ISectionService SECTION_SERVICE;

    @Autowired
    IUserService USER_SERVICE;

    @Autowired
    ISubjectService SUBJECT_SERVICE;

    @Autowired
    ICommentRepository COMMENT_REPOSITORY;

    @GetMapping("/protected/article/create")

    public String createSubjectPageShow(Model model) {
        List<Subjects> subjects = SUBJECT_SERVICE.getAllSubjects();
        model.addAttribute("subjects", subjects);
        return "article-creator";
    }

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

            ArticleAddDto newSubject = new ArticleAddDto(UserId, SubjectId, articleTitile, false, false, false);

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

    @GetMapping("/protected/articles/{subjectId}")
    public String getArticlesBySubjectId(@PathVariable("subjectId") Long subjectId, Model model) {
        List<Articles> articles = ARTICLE_SERVICE.getArticlesBySubjectId(subjectId);
        model.addAttribute("articles", articles);
        return "articles";
    }

    @GetMapping("/protected/articles/article/{articleId}")
    public String getArticleDetails(@PathVariable("articleId") Long articleId, Model model) {
        Articles article = ARTICLE_SERVICE.getArticleById(articleId);
        List<Sections> sections = SECTION_SERVICE.getAllSectionsByArticleId(articleId);
        List<Comments> comments = COMMENT_REPOSITORY.getCommentsByArticleId(articleId);
        if (article == null) {
            return "redirect:/protected/mainpage";
        }
        model.addAttribute("article", article);
        model.addAttribute("sections", sections);
        model.addAttribute("comments", comments);
        return "article-details";
    }

    @GetMapping("/comments/add/{articleId}")
    public String showAddCommentForm(@PathVariable long articleId, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        Optional<User> userOptional = USER_SERVICE.getUserByUsername((String) session.getAttribute("username"));
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            model.addAttribute("username", session.getAttribute("username"));
            model.addAttribute("user_adder_id", user.getId());
        }
        model.addAttribute("articleId", articleId);
        return "add-comment";
    }

}
