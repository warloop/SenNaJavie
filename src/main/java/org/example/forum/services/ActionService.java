package org.example.forum.services;

import org.example.forum.repos.Interfaces.IActionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActionService {

    @Autowired
    IActionRepository ACTION_REPOSITORY;
}
