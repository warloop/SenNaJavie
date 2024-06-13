package org.example.forum.dao.Interfaces;

import org.example.forum.entities.Subject_actions;

public interface ISubjectActionDao {

    Subject_actions get(long id);

    Boolean add(int action_id, int user_id, long subject_id);

    Boolean update(Subject_actions subjectAction);

    Boolean delete(long id);
}
