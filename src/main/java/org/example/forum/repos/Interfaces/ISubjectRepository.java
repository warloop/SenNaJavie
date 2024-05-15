package org.example.forum.repos.Interfaces;

import org.example.forum.dto.Subject.SubjectAddDto;
import org.example.forum.dto.Subject.SubjectEditDto;
import org.example.forum.entities.Subjects;
import java.util.Optional;

public interface ISubjectRepository {

    Optional<Subjects> getSubjectById(long subjectId);

    boolean addSubject(SubjectAddDto subject);

    boolean editSubjectText(SubjectEditDto subject);

    boolean setBanValueSubjectById(long subjectId, boolean visibility);

    boolean deleteSubjectById(long subjectId);

}
