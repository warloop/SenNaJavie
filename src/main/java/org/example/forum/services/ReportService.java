package org.example.forum.services;

import org.example.forum.repositories.Interfaces.ISubjectRepository;
import org.example.forum.services.interfaces.IReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportService implements IReportService {

    @Autowired
    ISubjectRepository REPORT_REPOSITORY;

}
