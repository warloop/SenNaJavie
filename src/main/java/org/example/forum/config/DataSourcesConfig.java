package org.example.forum.config;

import org.example.forum.dao.AccountTypeDao;
import org.example.forum.dao.LoginDao;
import org.example.forum.dao.SubjectDao;
import org.example.forum.dao.ActionTypesDao;
import org.example.forum.dao.ArticleDao;
import org.example.forum.dao.SectionsDao;
import org.example.forum.dao.SubjectActionDao;
import org.example.forum.dao.ArticleActionDao;
import org.example.forum.dao.SectionActionDao;
import org.example.forum.dao.CommentDao;
import org.example.forum.dao.CommentDislikesDao;
import org.example.forum.dao.CommentLikesDao;
import org.example.forum.dao.ReportTypesDao;
import org.example.forum.dao.ArticleReportsDao;
import org.example.forum.dao.SubjectReportDao;
import org.example.forum.dao.CommentReportDao;

import org.example.forum.dao.UserDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Klasa konfiguracji zależności, w przypdaku utworzenia nowego obiektu DAO, aby móc posługiwać się nim poprzez
 * automatyczne wstryzkiwanie zależności (@Autowired) należy zarejestrować takową klasę DAO w tym pliku.
 */

@Configuration
public class DataSourcesConfig {

    //\/\/\/\/\/\/\/\/\/\/\/\/\/\/ USERS \/\/\/\/\/\/\/\/\/\/\/\/\/\///

    @Bean
    public AccountTypeDao accountTypeDao() { return new AccountTypeDao(); }

    @Bean
    public UserDao userDao() { return new UserDao(this.accountTypeDao()); }

    @Bean
    public LoginDao loginDao() { return new LoginDao(this.userDao()); }

    //\/\/\/\/\/\/\/\/\/\/\/\/\/\/ CONTENT \/\/\/\/\/\/\/\/\/\/\/\/\/\///

    @Bean
    public SubjectDao subjectDao() { return new SubjectDao(); }

    @Bean
    public ArticleDao articleDao() { return new ArticleDao(); }

    @Bean
    public SectionsDao sectionDao() { return new SectionsDao();}

    @Bean
    public ActionTypesDao actionTypesDao() { return new ActionTypesDao();}

    @Bean
    public SubjectActionDao subjectActionDao() {
        return new SubjectActionDao();
    }

    @Bean
    public ArticleActionDao articleActionDao() {
        return new ArticleActionDao();
    }

    @Bean
    public SectionActionDao sectionActionDao() {
        return new SectionActionDao();
    }

    //\/\/\/\/\/\/\/\/\/\/\/\/\/\/ COMMENTS \/\/\/\/\/\/\/\/\/\/\/\/\/\///

    @Bean
    public CommentDao commentDao() { return new CommentDao(); }

    @Bean
    public CommentLikesDao commentLikesDao() {return new CommentLikesDao();}

    @Bean
    public CommentDislikesDao commentDislikesDao() {return new CommentDislikesDao();}

    //\/\/\/\/\/\/\/\/\/\/\/\/\/\/ REPORTS \/\/\/\/\/\/\/\/\/\/\/\/\/\///

    @Bean
    public ReportTypesDao reportTypeDao() { return new ReportTypesDao();}

    @Bean
    public ArticleReportsDao articleReportDao() { return new ArticleReportsDao();}

    @Bean
    public SubjectReportDao subjectReportDao() { return new SubjectReportDao();}

    @Bean
    public CommentReportDao commentReportDao() { return new CommentReportDao();}

}
