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

    @Autowired
    IArticleRepository ARTICLE_REPOSITORY;

    @Autowired
    IUserRepository USER_REPOSITORY;

    @Autowired
    IActionService ACTION_SERVICE;

    /**
     * Metoda waliduje dane zawarte w DTO, wywołuje niezbędne metody Repozytorium w celu dodania noewego artykułu, metoda
     * wywołuje akcję umieszczającą informację o utworzeniu nowego artykułu.
     * @param articleAdd Obiekt DTO zawierający niezbędne dane potrzebne do utworzenia artykułu - id_tematu,
     *                   id_użytkownika_tworzącego, temat_artykułu, widoczność_artykułu
     * @return Zwraca obiekt klasy (InformationReturned) zawierający informację zwrotną składającą się na kod operacji i
     * wiadomość zwrotną.
     * @author Artur Leszczak
     * @version 1.0.0
     */

    @Override
    public InformationReturned addArticle(ArticleAddDto articleAdd)
    {
        try{

            if(articleAdd.getTitle().length() > 256)
            {
                throw new SubjectLengthTooLongException(400, "Treść tematu jest zbyt długa! Limit to 256 znaków!");
            }

            if((articleAdd.getUserAdderId() <= 0) || USER_REPOSITORY.findById(articleAdd.getUserAdderId()).isEmpty()){
                throw new UserIsNotExistsException(400, "Nie znaleziono uzytkownika o podanym ID!");
            }

            Optional<Long> addedArticleId = ARTICLE_REPOSITORY.createArticle(articleAdd);

            if(addedArticleId.isPresent() && addedArticleId.get() > 0)
            {
                //dodanie informacji o utworzeniu nowego tematu
                ACTION_SERVICE.addArticleAction(articleAdd.getUserAdderId(), addedArticleId.get());

                return new InformationReturned(201, "Poprawnie utworzono nowy artykuł w temacie");
            }

            throw new Exception("Nie udało się dodać artykułu w temacie!");

        }catch (SubjectLengthTooLongException e){
            return new InformationReturned(e.getCode(), e.getMessage());
        }catch (UserIsNotExistsException e){
            return new InformationReturned(e.getCode(), e.getMessage());
        }catch (Exception e)
        {
            return new InformationReturned(500, e.getMessage());
        }
    }
    /**
     * Method to report an article.
     *
     * @param article The report data containing articleId, reporterId, reasonId, and reason.
     * @return An InformationReturned object containing the HTTP status code and message.
     * @throws IllegalArgumentException If the input data is invalid or the article does not exist.
     * @throws Exception If any other error occurs during the reporting process.
     * @author  Artur Leszczak
     * @version 1.0.0
     */
    @Override
    public InformationReturned reportArticle(ArticleReportDto article) {
        // Initialize the response object with a default error message
        InformationReturned infoReturned = new InformationReturned(500, "Zgłoszenie nie zostało zarejestrowane!");

        try {
            // Validate the input data
            if (article.getArticleId() <= 0 || article.getReporterId() <= 0 || article.getReasonId() <= 0 || article.getReason().isEmpty()) {
                throw new IllegalArgumentException("Invalid input data.");
            }

            // Check if the article exists in the database
            Optional<ArticleDto> existingArticle = ARTICLE_REPOSITORY.findById(article.getArticleId());
            if (existingArticle.isEmpty()) {
                throw new IllegalArgumentException("Article with the given ID does not exist.");
            }

            // Save the report data to the database or perform any other necessary operations
            // For example, you can create a new Report object and save it to the REPORT_REPOSITORY

            // Update the status of the article to "reported"
           // existingArticle.get().setStatus("reported");
           // ARTICLE_REPOSITORY.save(existingArticle.get());

            // Return a success message
            infoReturned = new InformationReturned(200, "Zgłoszenie zostało zarejestrowane.");
        } catch (IllegalArgumentException e) {
            // Handle invalid input data
            infoReturned = new InformationReturned(400, e.getMessage());
        } catch (Exception e) {
            // Handle any other exceptions that may occur during the reporting process
            infoReturned = new InformationReturned(500, "Wystąpił błąd podczas rejestracji zgłoszenia.");
        }

        return infoReturned;
    }
}
