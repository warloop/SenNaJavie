package org.example.forum.repositories.Interfaces;

import org.example.forum.entities.Article_reports;
import org.example.forum.entities.Comments_reports;
import org.example.forum.entities.Subjects_reports;
import org.example.forum.enums.ReportReasons;

import java.util.List;

public interface IReportRepository {

    boolean reportSubject(long id, int user_reporter, ReportReasons report_reason);

    boolean reportArticle(long id, int user_reporter, ReportReasons report_reason);

    boolean reportComment(long id, int user_reporter, ReportReasons report_reason);

    List<Subjects_reports> getAllSubjectReports();

    List<Article_reports> getAllArticleReports();

    List<Comments_reports> getAllCommentReports();

    boolean setReportSubjectViewed(long report_id);

    boolean setReportArticleViewed(long report_id);

    boolean setReportCommentViewed(long report_id);

}