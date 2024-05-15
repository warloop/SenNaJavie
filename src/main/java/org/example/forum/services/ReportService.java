package org.example.forum.services;

import org.example.forum.repos.Interfaces.ISubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportService {

    @Autowired
    ISubjectRepository REPORT_REPOSITORY;
}
