package org.example.forum.dao.Interfaces;

public interface IArticleActionDao {

    Boolean add(int action_id, int user_id, long subject_id);
}
