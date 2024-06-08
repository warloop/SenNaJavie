package org.example.forum.repositories;

import org.example.forum.dao.Interfaces.IArticleDao;
import org.example.forum.dto.Article.ArticleAddDto;
import org.example.forum.entities.Articles;
import org.example.forum.repositories.Interfaces.IArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Klasa zawiera metody odwołujące się do metod obiektów DAO.
 * @author Artur Leszczak
 * @version 1.0.0
 */

@Repository
public class ArticleRepository implements IArticleRepository {

    @Autowired
    private IArticleDao ARTICLE_DAO;

    /**
     * Metoda zarządza DAO, wykonuje niezbędne czynności w celu utworzenia artykułu w temacie.
     * @param newArticle Obiekt DTO zawierajcy niezbędne informacje
     * @return True / False w zależności, czy dodawanie powiodło się.
     * @author Artur Leszczak
     * @version 1.0.0
     */
    @Override
    public Optional<Long> createArticle(ArticleAddDto newArticle)
    {
        return ARTICLE_DAO.add(newArticle);
    }

    @Override
    public List<Articles> findByUserAdderId(int userAdderId) {
        return ARTICLE_DAO.findByUserAdderId(userAdderId);
    }
}
