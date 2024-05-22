package org.example.forum.services;

import org.example.forum.dto.Article.ArticleAddDto;
import org.example.forum.dto.System.InformationReturned;
import org.example.forum.exception.SubjectLengthTooLongException;
import org.example.forum.exception.UserIsNotExistsException;
import org.example.forum.repositories.Interfaces.IArticleRepository;
import org.example.forum.repositories.Interfaces.IUserRepository;
import org.example.forum.services.interfaces.IActionService;
import org.example.forum.services.interfaces.IArticleService;
import org.example.forum.services.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ArticleService implements IArticleService {

    @Autowired
    IArticleRepository ARTICLE_REPOSITORY;

    @Autowired
    IUserRepository USER_REPOSITORY;

    @Autowired
    IActionService ACTION_SERVICE;

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
}
