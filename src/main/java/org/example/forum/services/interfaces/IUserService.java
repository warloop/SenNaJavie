package org.example.forum.services.interfaces;

import org.example.forum.dto.System.InformationReturned;
import org.example.forum.dto.User.LoginInformationReturned;
import org.example.forum.dto.User.UserLoginDto;
import org.example.forum.dto.User.UserRegisterDto;
import org.example.forum.entities.User;

import java.util.Optional;

public interface IUserService {

    InformationReturned registerUser(UserRegisterDto user);

    LoginInformationReturned loginUser(UserLoginDto loginData);

    Optional<Boolean> isUserExistsByUserIdAndNotBanned(int UserId);

    Optional<User> getUserByUsername(String username);
}
