package org.example.forum.services;

import org.example.forum.repos.Interfaces.ISubjectRepository;
import org.example.forum.services.interfaces.ISubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubjectService implements ISubjectService {

    @Autowired
    ISubjectRepository SUBJECT_REPOSITORY;


}
