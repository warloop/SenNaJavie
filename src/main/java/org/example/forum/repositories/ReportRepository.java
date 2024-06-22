package org.example.forum.repositories;

import org.example.forum.dao.Interfaces.IArticleReportsDao;
import org.example.forum.dao.Interfaces.ICommentReportDao;
import org.example.forum.dao.Interfaces.ISubjectReportDao;
import org.example.forum.entities.*;
import org.example.forum.enums.ReportReasons;
import org.example.forum.repositories.Interfaces.IReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ReportRepository implements IReportRepository {

    private final ISubjectReportDao subjectReportDao;
    private final IArticleReportsDao articleReportDao;
    private final ICommentReportDao commentReportDao;

    /**
     * Wstrzykiwanie obiektów DAO
     */
    @Autowired
    public ReportRepository(ISubjectReportDao subjectReportDao, IArticleReportsDao articleReportDao, ICommentReportDao commentReportDao)
    {
        this.subjectReportDao = subjectReportDao;
        this.articleReportDao = articleReportDao;
        this.commentReportDao = commentReportDao;

    }

    @Override
    public boolean reportSubject(long id, int user_reporter, ReportReasons report_reason)
    {

    }

    @Override
    public boolean reportArticle(long id, int user_reporter, ReportReasons report_reason)
    {

    }

    @Override
    public boolean reportComment(long id, int user_reporter, ReportReasons report_reason)
    {

    }

    @Override
    public List<Subjects_reports> getAllSubjectReports()
    {

    }

    @Override
    public List<Article_reports> getAllArticleReports()
    {

    }

    @Override
    public List<Comments_reports> getAllCommentReports()
    {

    }

    @Override
    public boolean setReportSubjectViewed(long report_id)
    {

    }

    @Override
    public boolean setReportArticleViewed(long report_id)
    {

    }

    @Override
    public boolean setReportCommentViewed(long report_id)
    {

    }


}
