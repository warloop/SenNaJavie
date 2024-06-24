package org.example.forum.dao.Interfaces;

import org.example.forum.entities.Subjects_reports;

import java.util.List;
import java.util.Optional;

public interface ISubjectReportDao {

    Subjects_reports get(long id);

    List<Subjects_reports> getAllNotViewed();

    List<Subjects_reports> getAll();

    Optional<Long> add(Subjects_reports articleReport);

    Boolean update(Subjects_reports articleReport);

    Boolean delete(long id);

}