package org.example.forum.dto.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class LoginInformationReturned
{
    private int code;
    private String message;
    private int user_id;

    public LoginInformationReturned (int code, String message, int user_id)
    {
        this.code = code;
        this.message = message;
        this.user_id = user_id;
    }
}