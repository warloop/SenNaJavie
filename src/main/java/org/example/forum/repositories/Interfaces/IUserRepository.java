package org.example.forum.repositories.Interfaces;

import org.example.forum.dto.User.UserRegisterDto;
import org.example.forum.entities.User;

import java.util.List;
import java.util.Optional;

public interface IUserRepository {

    void save(User t);
    void update(User t);
    void delete(User t);
    Optional<User> findById(int id);
    List<User> findAll();
    Optional<User> findByLoginAndPass(String login,String password);

    boolean registerUser(UserRegisterDto userData);

    Optional<Boolean> isUserExistsByLogin(String login);

    Optional<Boolean> isUserExistsByEmail(String email);

    Optional<Integer> findUserIdByLogin(String login);
}
