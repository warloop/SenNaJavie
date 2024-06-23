package org.example.forum.services;

import org.example.forum.repositories.Interfaces.ICommentRepository;
import org.example.forum.services.interfaces.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService implements ICommentService {

    @Autowired
    ICommentRepository COMMENT_REPOSITORY;
}
