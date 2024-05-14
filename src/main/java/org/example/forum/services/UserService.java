package org.example.forum.services;

import jakarta.persistence.criteria.CriteriaBuilder;
import org.example.forum.dto.System.InformationReturned;
import org.example.forum.dto.User.UserLoginDto;
import org.example.forum.dto.User.UserRegisterDto;
import org.example.forum.entities.User;
import org.example.forum.repos.Interfaces.UserRepository;
import org.example.forum.services.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Serwis odpowiada za powiązanie pomiędzy logiką aplikacji a komunikacją między kontrolerem oraz repozytorium.
 * @author Artur Leszczak
 * @version 1.0.0
 */

@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository USER_REPOSITORY;

    /**
     * Metoda wykonuje niezbędne funkcjonalności oraz logikę odpowiedzialną za rejestrację użytkownika w systemie.
     * @param userData - Obiekt typu DTO zawierający niezbędne dane do wykonania rejestracji.
     * @return InformationReturned DTO - Zwraca obiekt, który posiada komunikat oraz kod operacji.
     * @author tech. inf. Artur Leszczak
     * @version 1.0.0
     */
    @Override
    public InformationReturned registerUser(UserRegisterDto userData)
    {

        try{

            if (USER_REPOSITORY.isUserExistsByLogin(userData.getLogin()).orElse(false)) {
                throw new Exception("Ten login jest już zajęty! Proces rejestracji przerwany!");
            }

            if (USER_REPOSITORY.isUserExistsByEmail(userData.getEmail()).orElse(false)) {
                throw new Exception("Ten email jest już zajęty! Proces rejestracji przerwany!");
            }

            if(!USER_REPOSITORY.registerUser(userData)) throw new Exception("Rejestracja nie powiodła się, spróbuj ponownie!");

            return new InformationReturned(200, "Poprawnie zarejestrowano!");

        }catch (Exception e){
            return new InformationReturned(400, e.getMessage());
        }

    }

    /**
     * Metoda wykonuje niezbędne funkcjonalności oraz logikę odpowiedzialną za logowanie użytkownika w systemie.
     * @param userData - Obiekt typu DTO zawierający niezbędne dane do przeprowadzenia logowania.
     * @return InformationReturned DTO - Zwraca obiekt, który posiada komunikat oraz kod operacji.
     * @author tech. inf. Artur Leszczak
     * @version 1.0.0
     */
    @Override
    public InformationReturned loginUser(UserLoginDto userData)
    {
        try{

            if(USER_REPOSITORY.findByLoginAndPass(userData.getLogin(), userData.getPassword()) == null) throw new Exception("Nie poprawny login lub hasło!");

            return new InformationReturned(200, "Poprawnie zalogowano!");

        }catch(Exception e){
            return new InformationReturned(400,e.getMessage());
        }
    }

}