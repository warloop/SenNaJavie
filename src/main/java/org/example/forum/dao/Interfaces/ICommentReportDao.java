package org.example.forum.dao.Interfaces;

import org.example.forum.entities.Comments_reports;

import java.util.Optional;

public interface ICommentReportDao {

    Comments_reports get(long id);

    Optional<Long> add(Comments_reports commentReports);

    Boolean update(Comments_reports commentReports);

    Boolean delete(long id);

}
