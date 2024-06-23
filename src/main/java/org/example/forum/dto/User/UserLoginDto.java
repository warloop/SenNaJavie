package org.example.forum.dto.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class UserLoginDto
{
    private String login;
    private String password;

    public UserLoginDto(String login, String password)
    {
        this.login = login;
        this.password = password;
    }
}
