package org.example.forum.dto.User;

import lombok.Getter;
import lombok.Setter;

/**
 * @brief Klasa DTO usprawniająca komunikację Kontroler <=> Repozytorium
 * @author Artur Leszczak
 */

@Getter
@Setter
public class UserRegisterDto {

    private String name;
    private String surname;
    private String login;
    private String email;
    private String password;

    public UserRegisterDto(String name, String surname, String login, String email, String password) {
        this.name = name;
        this.surname = surname;
        this.login = login;
        this.email = email;
        this.password = password;
    }

}
