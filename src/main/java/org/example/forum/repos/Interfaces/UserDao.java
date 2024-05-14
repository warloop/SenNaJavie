package org.example.forum.repos.Interfaces;


import org.example.forum.dto.User.UserRegisterDto;
import org.example.forum.entities.Login;
import org.example.forum.entities.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    boolean registerUser(UserRegisterDto userData);
    void save(User t);
    void update(User t);
    void delete(User t);
    Optional<User> findById(int id);
    List<User> findAll();
    Optional<User> findByLoginAndPass(String login,String password);
}
