package org.example.forum.dao.Interfaces;

import org.example.forum.entities.Article_actions;

public interface IArticleActionDao {

    Boolean add(int action_id, int user_id, long subject_id);

    Article_actions get(long id);

    Boolean update(Article_actions article_actions);

    Boolean delete(long id);
}
