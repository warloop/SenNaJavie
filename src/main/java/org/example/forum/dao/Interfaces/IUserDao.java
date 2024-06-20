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

    Optional<User> getUserBySpecifiedColumn(String columnName, int data);

    Optional<User> getUserBySpecifiedColumn(String columnName, String data);

}
