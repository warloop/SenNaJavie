package org.example.forum.dao.Interfaces;

import org.example.forum.entities.Comments_reports;
import java.util.List;
import java.util.Optional;

public interface ICommentReportDao {

    Comments_reports get(long id);

    List<Comments_reports> getAllNotViewed();

    List<Comments_reports> getAll();

    Optional<Long> add(Comments_reports commentReports);

    Boolean update(Comments_reports commentReports);

    Boolean delete(long id);

}
