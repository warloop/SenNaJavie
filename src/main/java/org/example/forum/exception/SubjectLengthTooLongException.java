package org.example.forum.exception;

import lombok.Getter;

@Getter
public class SubjectLengthTooLongException extends Throwable {

    private int code;
    private String message;

    public SubjectLengthTooLongException(int code, String error_message) {
        this.code = code;
        this.message = error_message;
    }
}
