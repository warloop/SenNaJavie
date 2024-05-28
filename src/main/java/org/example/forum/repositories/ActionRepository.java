package org.example.forum.repositories;

import org.example.forum.dao.ArticleActionDao;
import org.example.forum.dao.Interfaces.IArticleActionDao;
import org.example.forum.dao.Interfaces.ISectionActionDao;
import org.example.forum.dao.Interfaces.ISubjectActionDao;
import org.example.forum.dao.SectionActionDao;
import org.example.forum.dao.SubjectActionDao;
import org.example.forum.repositories.Interfaces.IActionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Klasa zawiera metody odwołujące się do metod obiektów DAO.
 * @author Artur Leszczak
 * @version 1.0.0
 */

@Repository
public class ActionRepository implements IActionRepository {

    /**
     * Powiązania z niezbędnymi klasami DAO (wstrzykiwanie zależności)
     */

    @Autowired
    private IArticleActionDao ARTICLE_DAO;
    @Autowired
    private ISectionActionDao SECTION_DAO;
    @Autowired
    private ISubjectActionDao SUBJECT_DAO;

    /**
     * Metoda wywołuje funkcję dodającą akcję przeprowadzoną na temacie, wykorzystuje DAO (SubjectActionDao)
     * @param action_id - Id akcji
     * @param UserId - Id użytkownika
     * @param Subject_id - Id tematu
     * @return Wynikiem jest true / false w zależności czy DAO dodał informację o zdarzeniu w temacie czy nie.
     * @version 1.0.0
     */
    @Override
    public Boolean addSubjectAction(int action_id, int UserId, long Subject_id)
    {
        return SUBJECT_DAO.add(action_id,UserId,Subject_id);
    }

    /**
     * Metoda wywołuje funkcję dodającą akcję przeprowadzoną na artykule, wykorzystuje DAO (ArticleActionDao)
     * @param action_id - Id akcji
     * @param UserId - Id użytkownika
     * @param Article_id - Id artykułu
     * @return Wynikiem jest true / false w zależności czy DAO dodał informację o zdarzeniu w artykule czy nie.
     * @version 1.0.0
     */
    @Override
    public Boolean addArticleAction(int action_id, int UserId, long Article_id)
    {
        return ARTICLE_DAO.add(action_id,UserId,Article_id);
    }

    /**
     * Metoda wywołuje funkcję dodającą akcję przeprowadzoną na sekcji, wykorzystuje DAO (SectionActionDao)
     * @param action_id - Id akcji
     * @param UserId - Id użytkownika
     * @param Section_id - Id sekcji
     * @return Wynikiem jest true / false w zależności czy DAO dodał informację o zmianie w sekcji czy nie.
     * @version 1.0.0
     */
    @Override
    public Boolean addSectionAction(int action_id, int UserId, long Section_id)
    {
        return SECTION_DAO.add(action_id,UserId,Section_id);
    }
}
