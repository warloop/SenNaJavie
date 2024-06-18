package org.example.forum.services;

import java.util.Optional;
import org.example.forum.dto.Article.ArticleAddDto;
import org.example.forum.dto.Article.ArticleDto;
import org.example.forum.dto.Article.ArticleReportDto;
import org.example.forum.dto.System.InformationReturned;
import org.example.forum.entities.Articles;
import org.example.forum.exception.SubjectLengthTooLongException;
import org.example.forum.exception.UserIsNotExistsException;
import org.example.forum.repositories.Interfaces.IArticleRepository;
import org.example.forum.repositories.Interfaces.IUserRepository;
import org.example.forum.services.interfaces.IActionService;
import org.example.forum.services.interfaces.IArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Klasa odpowiadająca za wewnętrzną logikę biznesową aplikacji w przypadku zarządzania Artykułami. Waliduje oraz wykonuje
 * niezbędne obliczenia oraz pobiera odpowiednie dane z osobnych repozytoriów w celu wykonania bardziej złożonych operacji.
 * @author Artur Leszczak
 * @version 1.0.0
 */

@Service
public class ArticleService implements IArticleService {

    /**
     * Wstrzykiwane zależności
     */

    private IArticleRepository ARTICLE_REPOSITORY;
    private IUserRepository USER_REPOSITORY;
    private IActionService ACTION_SERVICE;

    @Autowired
    public ArticleService(IActionService actionService, IArticleRepository articleRepository, IUserRepository userRepository) {
        this.ACTION_SERVICE = actionService;
        this.ARTICLE_REPOSITORY = articleRepository;
        this.USER_REPOSITORY = userRepository;
    }


    /**
     * Metoda waliduje dane zawarte w DTO, wywołuje niezbędne metody Repozytorium w celu dodania noewego artykułu, metoda
     * wywołuje akcję umieszczającą informację o utworzeniu nowego artykułu.
     *
     * @param articleAdd Obiekt DTO zawierający niezbędne dane potrzebne do utworzenia artykułu - id_tematu,
     *                   id_użytkownika_tworzącego, temat_artykułu, widoczność_artykułu
     * @return Zwraca obiekt klasy (InformationReturned) zawierający informację zwrotną składającą się na kod operacji i
     * wiadomość zwrotną.
     * @author Artur Leszczak
     * @version 1.0.0
     */
    @Override
    public InformationReturned addArticle(ArticleAddDto articleAdd) {
        try {

            if (articleAdd.getTitle().length() > 256) {
                throw new SubjectLengthTooLongException(400, "Treść tematu jest zbyt długa! Limit to 256 znaków!");
            }

            if ((articleAdd.getUserAdderId() <= 0) || USER_REPOSITORY.findById(articleAdd.getUserAdderId()).isEmpty()) {
                throw new UserIsNotExistsException(400, "Nie znaleziono uzytkownika o podanym ID!");
            }

            Optional<Long> addedArticleId = ARTICLE_REPOSITORY.createArticle(articleAdd);

            if (addedArticleId.isPresent() && addedArticleId.get() > 0) {
                //dodanie informacji o utworzeniu nowego tematu
                ACTION_SERVICE.addArticleAction(articleAdd.getUserAdderId(), addedArticleId.get());

                return new InformationReturned(201, "Poprawnie utworzono nowy artykuł w temacie", "/article");
            }

            throw new Exception("Nie udało się dodać artykułu w temacie!");

        } catch (SubjectLengthTooLongException e) {
            return new InformationReturned(e.getCode(), e.getMessage(), "/article");
        } catch (UserIsNotExistsException e) {
            return new InformationReturned(e.getCode(), e.getMessage(), "/article");
        } catch (Exception e) {
            return new InformationReturned(500, e.getMessage(), "/article");
        }
    }

    /**
     * Method to report an article.
     *
     * @param article The report data containing articleId, reporterId, reasonId, and reason.
     * @return An InformationReturned object containing the HTTP status code and message.
     * @throws IllegalArgumentException If the input data is invalid or the article does not exist.
     * @throws Exception                If any other error occurs during the reporting process.
     * @author Artur Leszczak
     * @version 1.0.0
     */
    @Override
    public InformationReturned reportArticle(ArticleReportDto article) {
        InformationReturned infoReturned = new InformationReturned(500, "Zgłoszenie nie zostało zarejestrowane!", "article/report");

        try {

            if (article.getArticleId() <= 0 || article.getReporterId() <= 0 || article.getReasonId() <= 0 || article.getReason().isEmpty()) {
                throw new IllegalArgumentException("Invalid input data.");
            }

            Optional<ArticleDto> existingArticle = ARTICLE_REPOSITORY.findById(article.getArticleId());
            if (existingArticle.isEmpty()) {
                throw new IllegalArgumentException("Article with the given ID does not exist.");
            }

            infoReturned = new InformationReturned(200, "Zgłoszenie zostało zarejestrowane.", "/article/" + article.getArticleId() + "/report");
        } catch (IllegalArgumentException e) {
            infoReturned = new InformationReturned(400, e.getMessage(), "/article/" + article.getArticleId() + "/report");
        } catch (Exception e) {
            infoReturned = new InformationReturned(500, "Wystąpił błąd podczas rejestracji zgłoszenia.", "/article/" + article.getArticleId() + "/report");
        }

        return infoReturned;

    }

    @Override
    public List<Articles> findByUserId(int userId) {
        return ARTICLE_REPOSITORY.findByUserAdderId(userId);
    }

    @Override
    public List<Articles> getArticlesBySubjectId(Long subjectId) {
        return ARTICLE_REPOSITORY.findBySubjectId(subjectId);

    }
}
