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
        Subjects_reports report = new Subjects_reports();

        report.setSubject_id(id);
        report.setUser_reporter_id(user_reporter);
        report.setReport_type(report_reason.getValue());

        return subjectReportDao.add(report).isPresent();

    }

    @Override
    public boolean reportArticle(long id, int user_reporter, ReportReasons report_reason)
    {
        Article_reports report = new Article_reports();

        report.setArticle_id(id);
        report.setUser_reporter_id(user_reporter);
        report.setReport_type(report_reason.getValue());

        return articleReportDao.add(report).isPresent();
    }

    @Override
    public boolean reportComment(long id, int user_reporter, ReportReasons report_reason)
    {
        Comments_reports report = new Comments_reports();

        report.setComment_id(id);
        report.setUser_reporter_id(user_reporter);
        report.setReport_type(report_reason.getValue());

        return commentReportDao.add(report).isPresent();
    }

    @Override
    public List<Subjects_reports> getAllSubjectReports()
    {
        return this.subjectReportDao.getAllNotViewed();
    }

    @Override
    public List<Article_reports> getAllArticleReports()
    {
        return this.articleReportDao.getAllNotViewed();
    }

    @Override
    public List<Comments_reports> getAllCommentReports()
    {
        return this.commentReportDao.getAllNotViewed();
    }

    @Override
    public boolean setReportSubjectViewed(long report_id)
    {
        Subjects_reports sb = subjectReportDao.get(report_id);

        sb.set_viewed(true);

        return this.subjectReportDao.update(sb);
    }

    @Override
    public boolean setReportArticleViewed(long report_id)
    {
        Article_reports sb = articleReportDao.get(report_id);

        sb.set_viewed(true);

        return this.articleReportDao.update(sb);
    }

    @Override
    public boolean setReportCommentViewed(long report_id)
    {
        Comments_reports sb = commentReportDao.get(report_id);

        sb.set_viewed(true);

        return this.commentReportDao.update(sb);
    }


}
