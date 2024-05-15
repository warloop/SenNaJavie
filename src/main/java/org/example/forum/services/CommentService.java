package org.example.forum.services;

import org.example.forum.repos.Interfaces.ICommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    @Autowired
    ICommentRepository COMMENT_REPOSITORY;
}
