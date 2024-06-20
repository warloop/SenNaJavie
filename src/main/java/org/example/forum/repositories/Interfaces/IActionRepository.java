package org.example.forum.repositories.Interfaces;

import org.example.forum.enums.ActionType;

public interface IActionRepository {

    Boolean addSubjectAction(ActionType ACTION_TYPE, int UserId, long Subject_id);

    Boolean addArticleAction(ActionType ACTION_TYPE, int UserId, long Article_id);

    Boolean addSectionAction(ActionType ACTION_TYPE, int UserId, long Section_id);
}
