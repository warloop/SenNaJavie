package org.example.forum.dao.Interfaces;

import org.example.forum.entities.Subjects_reports;

import java.util.Optional;

public interface ISubjectReportDao {

    Subjects_reports get(int id);

    Optional<Integer> add(Subjects_reports articleReport);

    Boolean update(Subjects_reports articleReport);

    Boolean delete(int id);

}
