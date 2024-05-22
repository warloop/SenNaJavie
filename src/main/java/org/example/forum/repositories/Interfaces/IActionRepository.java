package org.example.forum.repositories.Interfaces;

public interface IActionRepository {

    Boolean addSubjectAction(int action_id, int UserId, long Subject_id);

    Boolean addArticleAction(int action_id, int UserId, long Article_id);

    Boolean addSectionAction(int action_id, int UserId, long Section_id);
}
