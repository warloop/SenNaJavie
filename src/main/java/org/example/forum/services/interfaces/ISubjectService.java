package org.example.forum.services.interfaces;

import org.example.forum.dto.Subject.SubjectAddDto;
import org.example.forum.dto.Subject.SubjectEditDto;
import org.example.forum.dto.System.InformationReturned;
import org.example.forum.entities.Subjects;

import java.util.List;
import java.util.Optional;

public interface ISubjectService {

   InformationReturned addSubject(SubjectAddDto subject);

   Optional<Boolean> isSubjectExists(long SubjectId);
   List<Subjects> getAllSubjects();
}
