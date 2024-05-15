package org.example.forum.services;

import org.example.forum.repos.Interfaces.IArticleRepository;
import org.example.forum.services.interfaces.IArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArticleService implements IArticleService {

    @Autowired
    IArticleRepository ARTICLE_REPOSITORY;
}
