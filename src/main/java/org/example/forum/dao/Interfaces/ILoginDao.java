package org.example.forum.dao.Interfaces;

import org.example.forum.entities.Login;

import java.util.Optional;

public interface ILoginDao {

    Login get(int id);

    Optional<Integer> add(Login login);

    Boolean update(Login login);

    Boolean delete(int id);

    Optional<Login> getLoginObjectByLogin(String login);

}
