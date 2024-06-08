package org.example.forum.services;

import org.example.forum.dto.Article.ArticleAddDto;
import org.example.forum.dto.System.InformationReturned;
import org.example.forum.entities.Articles;
import org.example.forum.exception.SubjectLengthTooLongException;
import org.example.forum.exception.UserIsNotExistsException;
import org.example.forum.repositories.Interfaces.IArticleRepository;
import org.example.forum.repositories.Interfaces.IUserRepository;
import org.example.forum.services.interfaces.IActionService;
import org.example.forum.services.interfaces.IArticleService;
import org.example.forum.services.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    @Override
    public List<Articles> findByUserId(int userId) {
        return ARTICLE_REPOSITORY.findByUserAdderId(userId);
    }

}
