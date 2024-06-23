package org.example.forum.repositories.Interfaces;


import org.example.forum.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IRoleRepository{

    public boolean assignRoleToUser(Long userId, Long roleId);
}
