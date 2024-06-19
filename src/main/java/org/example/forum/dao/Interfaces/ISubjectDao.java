package org.example.forum.dao.Interfaces;

import org.example.forum.dto.Subject.SubjectAddDto;
import org.example.forum.entities.Subjects;

import java.util.List;
import java.util.Optional;

public interface ISubjectDao {

    Subjects get(long id);

    Optional<Subjects> getSubjectBySpecifiedColumn(String column, int data);

    Optional<Subjects> getSubjectBySpecifiedColumn(String column, String data);

    Optional<Long> add(SubjectAddDto subjectAddData);

    Boolean update(Subjects subject);

    Boolean delete(long id);

    List<Subjects> getAll();

}
