package org.example.forum.services.interfaces;

import org.example.forum.dto.Subject.SubjectAddDto;
import org.example.forum.dto.Subject.SubjectEditDto;
import org.example.forum.dto.System.InformationReturned;
import org.example.forum.entities.Subjects;

import java.util.Optional;

public interface ISubjectService {

   InformationReturned addSubject(SubjectAddDto subject);

}
