package org.example.forum.repositories;

import org.example.forum.dao.Interfaces.IArticleDao;
import org.example.forum.dto.Article.ArticleAddDto;
import org.example.forum.dto.Article.ArticleDto;
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

    /**
     * This method retrieves an article by its unique identifier.
     * It uses the ArticleDao to fetch the article data from the database.
     * If the article is found, it maps the data to an ArticleDto object and returns it.
     * If the article is not found, it returns an Optional containing null.
     *
     * @param id The unique identifier of the article to retrieve.
     * @return An Optional containing the ArticleDto object if the article is found, or an Optional containing null if not found.
     * @author Artur Leszczak
     * @version 1.0.0
     */
    @Override
    public Optional<ArticleDto> findById(long id) {
        try {

            if (id <= 0) throw new IllegalArgumentException("Article ID must be greater than 0.");

            // Create a new instance of ArticleDto
            ArticleDto articlesDto = new ArticleDto();

            // Use the ArticleDao to fetch the article by its unique identifier
            Optional<Articles> article = ARTICLE_DAO.getById(id);

            // If the article is found, map the data to the ArticleDto object and return it
            if (article.isPresent()) {
                articlesDto.setId(article.get().getId());
                articlesDto.setTitle_id(article.get().getSubject_id());
                articlesDto.set_visible(article.get().isVisible());
                articlesDto.set_banned(article.get().isBanned());
                articlesDto.set_deleted(article.get().isDeleted());
                articlesDto.setUser_adder_id(article.get().getUser_adder_id());

                return Optional.of(articlesDto);
            }

        } catch (Exception e) {
            // If the article is not found, return an Optional containing null
            System.out.println("\u001B[31m" + e.getMessage() + "\u001B[0m");
        }
        return Optional.of(null);
    }

        @Override
        public List<Articles> findByUserAdderId(int userAdderId) {
            return ARTICLE_DAO.findByUserAdderId(userAdderId);
        }

        @Override
        public List<Articles> findBySubjectId(long subjectId) {
            return ARTICLE_DAO.findBySubjectId(subjectId);

        }
}
