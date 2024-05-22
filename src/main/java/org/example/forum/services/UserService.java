package org.example.forum.services;

import org.example.forum.dto.System.InformationReturned;
import org.example.forum.dto.User.LoginInformationReturned;
import org.example.forum.dto.User.UserLoginDto;
import org.example.forum.dto.User.UserRegisterDto;
import org.example.forum.entities.User;
import org.example.forum.repositories.Interfaces.IUserRepository;
import org.example.forum.services.interfaces.ISecurityService;
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
    private IUserRepository USER_REPOSITORY;

    @Autowired
    private ISecurityService SECURITY_SERVICE;
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

            //HASHOWANIE HASŁA PRZEKAZANEGO W PROCESIE REJESTRACJI
            userData.setPassword(SECURITY_SERVICE.hashDataUsingSHA256(userData.getPassword()));

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
    public LoginInformationReturned loginUser(UserLoginDto userData)
    {
        try{

            if(USER_REPOSITORY.findByLoginAndPass(userData.getLogin(),  SECURITY_SERVICE.hashDataUsingSHA256(userData.getPassword())) == null) throw new Exception("Nie poprawny login lub hasło!");

            try{
                Optional<Integer> userOptionalId = USER_REPOSITORY.findUserIdByLogin(userData.getLogin());

                int UserId = -1;
                if(userOptionalId.isPresent())
                {
                    UserId = userOptionalId.get();
                }

                if(UserId > 0){
                    return new LoginInformationReturned(200, "Poprawnie zalogowano!", UserId);
                }

            }catch (NumberFormatException e){
                return new LoginInformationReturned(400,e.getMessage(), -1);
            }

        }catch(Exception e){
            return new LoginInformationReturned(400,e.getMessage(), -1);
        }
        return new LoginInformationReturned(500,"Nie udało się zalogować użytkownika.", -1);
    }

    /**
     * Metoda sprawdza czy użytkownik istnieje w bazie i nie jest zablokowany.
     * @param UserId - Id użytkownika
     * @return Boolean - True / false w zależności czy użytkownik został odnaleziony czy nie.
     * @author Artur Leszczak
     * @version 1.0.0
     */
    @Override
    public Optional<Boolean> isUserExistsByUserIdAndNotBanned(int UserId)
    {
        try{
            Optional<User> findUser = USER_REPOSITORY.findById(UserId);
            if(findUser.isPresent())
            {
                User findedUser = findUser.get();

                if(findedUser.getIs_deleted())
                {
                    return Optional.of(false);
                }

                return Optional.of(true);
            }
        }catch (Exception e){
            return Optional.of(null);
        }

        return Optional.of(null);
    }


}
