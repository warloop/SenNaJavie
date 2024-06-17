package org.example.forum.dao.Interfaces;

import org.example.forum.entities.Subjects;

import java.util.Optional;

public interface ISubjectDao {

    Subjects get(long id);

    Optional<Long> add(Subjects subject);

    Boolean update(Subjects subject);

    Boolean delete(long id);

}
