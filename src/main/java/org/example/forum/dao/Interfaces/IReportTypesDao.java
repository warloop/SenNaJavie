package org.example.forum.dao.Interfaces;

import org.example.forum.entities.Report_types;

import java.util.Optional;


public interface IReportTypesDao {

    Report_types get(int id);

    Optional<Integer> add(Report_types reportType);

    Boolean update(Report_types reportType);

    Boolean delete(int id);

}
