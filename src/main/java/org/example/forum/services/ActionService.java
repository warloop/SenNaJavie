package org.example.forum.services;

import org.example.forum.repos.Interfaces.IActionRepository;
import org.example.forum.services.interfaces.IActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActionService implements IActionService {

    @Autowired
    IActionRepository ACTION_REPOSITORY;

}
