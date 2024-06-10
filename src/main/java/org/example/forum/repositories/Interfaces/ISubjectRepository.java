package org.example.forum.repositories.Interfaces;

import org.example.forum.dto.Subject.SubjectAddDto;
import org.example.forum.dto.Subject.SubjectBanDto;
import org.example.forum.dto.Subject.SubjectEditDto;
import org.example.forum.entities.Subjects;

import java.util.List;
import java.util.Optional;

public interface ISubjectRepository {

    Optional<Subjects> getSubjectById(long subjectId);

    Optional<Long> addSubject(SubjectAddDto subject);

    boolean editSubjectText(SubjectEditDto subject);

    boolean setBanValueSubjectById(SubjectBanDto subjectBan);

    boolean deleteSubjectById(long subjectId, int UserId, boolean by_owner);

    List<Subjects> getAllSubjects();

}
