package org.example.forum.repositories;

import jakarta.transaction.Transactional;
import org.example.forum.dao.Interfaces.IArticleDao;
import org.example.forum.dto.Article.ArticleAddDto;
import org.example.forum.dto.Article.ArticleEditDto;
import org.example.forum.entities.Articles;
import org.example.forum.exception.DataAccessException;
import org.example.forum.repositories.Interfaces.IArticleRepository;
import org.example.forum.services.interfaces.IActionService;
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

    @Autowired
    private IActionService ACTION_SERVICE;
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

        Optional<Long> articleId = ARTICLE_DAO.add(newArticle);

        if(articleId.isPresent()){

            ACTION_SERVICE.addArticleAction(newArticle.getUserAdderId(), articleId.get());

            return articleId;
        }

        return Optional.empty();
    }

    /**
     * This method retrieves an article by its unique identifier.
     * It uses the ArticleDao to fetch the article data from the database.
     * If the article is found, it maps the data to an ArticleDto object and returns it.
     * If the article is not found, it returns an Optional containing null.
     *
     * @param articleId The unique identifier of the article to retrieve.
     * @return An Optional containing the ArticleDto object if the article is found, or an Optional containing null if not found.
     * @author Artur Leszczak
     * @version 1.0.0
     */
    @Override
    public Optional<Articles> findById(long articleId)
    {
        Optional<Articles> article = this.ARTICLE_DAO.getById(articleId);

        if(article!= null) return article;

        return Optional.empty();

    }

    @Override
    public List<Articles> findByUserAdderId(int userAdderId) {
            return ARTICLE_DAO.findByUserAdderId(userAdderId);
        }

    @Override
    public List<Articles> findBySubjectId(long subjectId) {
            return ARTICLE_DAO.findBySubjectId(subjectId);
        }

    /**
     * Metoda pobiera artykuł po jego unikalnych identyfikatorze.
     * Używa ArticleDao do pobrania danych artykułu z bazy danych.
     *
     * @param articleId Unikalny identyfikator artykułu do pobrania.
     * @return Obiekt Articles jeśli artykuł został znaleziony, lub zgłasza wyjątek jeśli nie został znaleziony.
     * @throws DataAccessException Jeśli wystąpi błąd podczas uzyskiwania dostępu do bazy danych.
     * @author Artur Leszczak
     * @version 1.0.0
     */
    @Override
    public Articles getArticleById(Long articleId) {

        return ARTICLE_DAO.getById(articleId).get();

    }

    public boolean editArticleText(ArticleEditDto articleEditDto)
    {
        Articles art = this.ARTICLE_DAO.getById(articleEditDto.getId()).get();

        art.setArticle_title(articleEditDto.getArticle_new_text());
        if(art.getId() == articleEditDto.getId()){
            if(this.ARTICLE_DAO.update(art.getId(), art)){
                this.ACTION_SERVICE.changeArticleAction(articleEditDto.getUser_changer_id(),art.getId());
                return true;
            }
        }
        return false;
    }

    /**
     * Deletes an article from the database.
     *
     * @param articleId The ID of the article to delete.
     * @return True if the deletion was successful, false otherwise.
     */
    @Override
    @Transactional
    public boolean deleteArticle(long articleId, int user_id, boolean by_owner)
    {
        Articles art = this.ARTICLE_DAO.getById(articleId).get();

        if(art.isDeleted()) return false;

        art.setDeleted(true);

        if(this.ARTICLE_DAO.update(art.getId(),art))
        {
            if(by_owner){
                this.ACTION_SERVICE.removeArticleActionByOwner(user_id, art.getId());
            }else{
                this.ACTION_SERVICE.removeArticleActionByModerator(user_id, art.getId());
            }
        }
        return false;
    }
}
