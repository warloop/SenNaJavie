package org.example.forum.dto.System;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

/**
 * Klasa DTO odpowiedzialna za wymianę informacji pomiędzy kontrolerem a serwisem
 * @author Artur Leszczak
 * @version 1.0.0
 */
public class InformationReturned {

    private int code;
    private String message;

    public InformationReturned(int code, String message) {
        this.code = code;
        this.message = message;
    }

}
