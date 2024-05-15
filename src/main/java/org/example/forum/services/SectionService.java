package org.example.forum.services;

import org.example.forum.repos.Interfaces.ISectionRepository;
import org.example.forum.repos.Interfaces.ISubjectRepository;
import org.example.forum.services.interfaces.ISectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SectionService implements ISectionService {

    @Autowired
    ISectionRepository SECTION_REPOSITORY;
}
