package org.example.forum.services.interfaces;

import org.example.forum.dto.System.InformationReturned;
import org.example.forum.dto.User.UserLoginDto;
import org.example.forum.dto.User.UserRegisterDto;

public interface IUserService {

    InformationReturned registerUser(UserRegisterDto user);

    InformationReturned loginUser(UserLoginDto loginData);

}
