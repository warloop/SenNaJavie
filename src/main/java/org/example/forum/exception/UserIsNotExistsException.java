package org.example.forum.exception;

import lombok.Getter;

@Getter
public class UserIsNotExistsException extends Throwable {

    private int code;
    private String message;

    public UserIsNotExistsException(int code, String error_message) {
        super("Code: "+code + " | Message: " + error_message);
        this.code = code;
        this.message = error_message;
    }
}
