package org.example.forum.config;

import org.example.forum.dao.ArticleActionDao;
import org.example.forum.dao.ArticleDao;
import org.example.forum.dao.SectionActionDao;
import org.example.forum.dao.SubjectActionDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSourcesConfig {

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

    @Bean
    public ArticleDao articleDao() {
        return new ArticleDao();
    }
}
