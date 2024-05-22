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

@Repository
public class ActionRepository implements IActionRepository {

    @Autowired
    private IArticleActionDao ARTICLE_DAO;
    @Autowired
    private ISectionActionDao SECTION_DAO;
    @Autowired
    private ISubjectActionDao SUBJECT_DAO;

    public Boolean addSubjectAction(int action_id, int UserId, long Subject_id)
    {
        return SUBJECT_DAO.add(action_id,UserId,Subject_id);
    }

    public Boolean addArticleAction(int action_id, int UserId, long Article_id)
    {
        return ARTICLE_DAO.add(action_id,UserId,Article_id);
    }

    public Boolean addSectionAction(int action_id, int UserId, long Section_id)
    {
        return SECTION_DAO.add(action_id,UserId,Section_id);
    }
}
