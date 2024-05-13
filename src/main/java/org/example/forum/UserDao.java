package org.example.forum;


import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    void save(User t);
    void update(User t);
    void delete(User t);
    Optional<User> findById(Long id);
    List<User> findAll();
    User findByUsernameAndPassword(String username, String password);
}
