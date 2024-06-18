package org.example.forum.dao.Interfaces;

import org.example.forum.entities.User;

import java.util.List;
import java.util.Optional;

public interface IUserDao {

    User get(int id);

    Optional<Integer> add(User user);

    Boolean update(User user);

    Boolean delete(int id);

    List<User> getAll();
}
