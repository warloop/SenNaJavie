package org.example.forum.dao;


import org.example.forum.entity.Login;
import org.example.forum.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    boolean registerUser(User user, Login login);
    void save(User t);
    void update(User t);
    void delete(User t);
    Optional<User> findById(int id);
    List<User> findAll();
    Optional<User> findByLoginAndPass(String login,String password);
}
