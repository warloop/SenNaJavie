package org.example.forum.repositories;

import jakarta.transaction.Transactional;
import org.example.forum.dao.Interfaces.ILoginDao;
import org.example.forum.dao.Interfaces.IUserDao;
import org.example.forum.dto.User.UserRegisterDto;
import org.example.forum.entities.AccountType;
import org.example.forum.entities.Login;
import org.example.forum.entities.User;
import org.example.forum.exception.DataAccessException;
import org.example.forum.exception.UserIsNotExistsException;
import org.example.forum.repositories.Interfaces.IUserRepository;
import org.example.forum.util.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository implements IUserRepository {

    private IUserDao userDao;
    private ILoginDao loginDao;
    private final PlatformTransactionManager transactionManager;

    /**
     * Ta klasa jest odpowiedzialna za zarządzanie danymi użytkowników w bazie danych.
     * Implementuje interfejs IUserRepository i udostępnia metody rejestracji,
     * zapisywania, aktualizowania, usuwania, wyszukiwania według identyfikatora,
     * wyszukiwania wszystkich użytkowników, wyszukiwania identyfikatora użytkownika według loginu,
     * sprawdzania, czy użytkownik istnieje według loginu, oraz sprawdzania, czy użytkownik istnieje według adresu email.
     *
     * @author Artur Leszczak
     * @version 1.0.1
     */
    @Autowired
    public UserRepository(IUserDao UserDao, ILoginDao LoginDao, PlatformTransactionManager transactionManager) {
        this.userDao = UserDao;
        this.loginDao = LoginDao;
        this.transactionManager = transactionManager;
    }

    /**
     * Metoda rejestrująca nowego użytkownika w systemie.
     *
     * @param postData Obiekt DTO z danymi rejestracji użytkownika.
     * @return Wartość logiczna, która jest równa true, jeśli rejestracja przebiegła pomyślnie, w przeciwnym razie false.
     * @throws DataAccessException Wyjątek występuje, gdy wystąpi błąd podczas wykonywania operacji związanych z bazą danych.
     *
     * @author Artur Leszczak
     * @version 1.0.0
     */
    @Override
    @Transactional
    public boolean registerUser(UserRegisterDto postData) {

        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("registerUserTransaction");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(def);

        try{
            User user = new User();
            user.setName(postData.getName());
            user.setSurname(postData.getSurname());
            user.setEmail(postData.getEmail());

            Optional<Integer> returnedVal = this.userDao.add(user);

            if (returnedVal.isEmpty()) {
                transactionManager.rollback(status);
                return false;
            }

            Login login = new Login();
            login.setLogin(postData.getLogin());
            login.setUser_id(user);
            login.setPassword(postData.getPassword());

            Optional<Integer> returnedLogin = this.loginDao.add(login);

            if (returnedLogin.isEmpty()) {
                transactionManager.rollback(status);
                return false;
            }

            transactionManager.commit(status);
            return true;
        }catch (Exception ex){
            transactionManager.rollback(status);
            throw new DataAccessException(ex);
        }

    }

    /**
     *
     * @author Mateusz Fiedosiuk
     * @version 1.0.0
     */
    @Override
    public Optional<User> findById(int id){

        try{
            User user = this.userDao.get(id);

            if(user == null){
                throw new UserIsNotExistsException(404,"Nie znaleziono użytkownika o podanym id");
            }

            return Optional.of(user);
        }catch (UserIsNotExistsException e)
        {
            return Optional.empty();
        }
    }

    /**
     *
     * @author Mateusz Fiedosiuk
     * @version 1.0.0
     */
    @Override
    public List<User> findAll() {
        List<User> userList = new ArrayList<User>();

        userList = this.userDao.getAll();

        return userList;

    }

    /**
     * Metoda wyszukuje unikalny identyfikator użytkownika na podstawie podanego loginu.
     *
     * @param login Login użytkownika, dla którego szukany jest identyfikator.
     * @return Obiekt Optional zawierający unikalny identyfikator użytkownika, jeśli został znaleziony.
     *         Jeśli użytkownik nie został znaleziony, zwracany jest pusty Optional.
     * @throws UserIsNotExistsException Wyjątek występuje, gdy użytkownik o podanym loginie nie istnieje.
     *
     * @author Artur Leszczak
     * @version 1.0.0
     */
    @Override
    public Optional<Integer> findUserIdByLogin(String login) {
        try{
            Optional<Login> takenLoginData = this.loginDao.getLoginObjectByLogin(login);

            if(takenLoginData.isEmpty()){
                throw new UserIsNotExistsException(404,"Nie znaleziono użytkownika o takim loginie ("+login+")");
            }

            Login takenLoginDataEntity = takenLoginData.get();

            return Optional.of(takenLoginData.get().getUser_id().getId());
        }catch(UserIsNotExistsException e){
            return Optional.empty();
        }
    }

    /**
     *
     * @param login - Wartość tekstowa reprezentująca login
     * @param password - Wartość tekstowa reprezentująca hasło
     * @return  User | null - Zwraca użytkownika lub null w przypadku niepowodzenia
     * @author Artur Leszczak
     * @version 1.0.1
     */
     @Override
     public Optional<User> findByLoginAndPass(String login, String password)
     {
        try{
            //pobranie danych użytkownika z bazy danych po loginie
            Optional<Login> takenLoginData = this.loginDao.getLoginObjectByLogin(login);

            if(takenLoginData.isEmpty()){
                throw new UserIsNotExistsException(404,"Nie znaleziono użytkownika");
            }

            Login takenLoginDataEntity = takenLoginData.get();

            return Optional.of(takenLoginData.get().getUser_id());

        }catch (UserIsNotExistsException e){

                return Optional.empty();
        }

     }

    /**
     * Metoda sprawdza czy istnieje użytkownik o przekazanym w parametrze loginie.
     * @param login Login szukanego użytkownika.
     * @return Optional<boolean> Zwraca wynik true jeżeli uzytkownik istnieje oraz false jeżeli nie istnieje, dodatkowo zwraca null w przypadku niepowodzenia w sprawdzeniu.
     * @author Artur Leszczak
     * @version 1.0.0
     */
    @Override
    public Optional<Boolean> isUserExistsByLogin(String login)
    {
        try{
            Optional<Login> takenLoginData = this.loginDao.getLoginObjectByLogin(login);

            if(takenLoginData.isEmpty()){
                throw new UserIsNotExistsException(404,"Nie znaleziono użytkownika");
            }

            return Optional.of(true);

        }catch (UserIsNotExistsException e){
            return Optional.of(false);
        }catch (Exception e){
            return Optional.empty();
        }
    }

    /**
     * Metoda sprawdza czy istnieje użytkownik o przekazanym w parametrze adresie email.
     * @param email Adres email szukanego użytkownika.
     * @author Artur Leszczak
     * @version 1.0.0
     */
    @Override
    public Optional<Boolean> isUserExistsByEmail(String email)
    {
        final String SQL = "SELECT COUNT(id) FROM users WHERE email = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement statement = conn.prepareStatement(SQL)) {
            statement.setString(1, email);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);

                    if(count == 0)
                    {
                        return Optional.of(false);
                    }
                    return Optional.of(true);
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        }
        return Optional.of(null);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        final String SQL = "SELECT u.* FROM users u INNER JOIN login l ON u.id = l.user_id WHERE l.login = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement statement = conn.prepareStatement(SQL)) {
            statement.setString(1, username);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setName(rs.getString("name"));
                    user.setSurname(rs.getString("surname"));

                    AccountType accountType = new AccountType();
                    accountType.setId(rs.getInt("account_type_id"));
                    accountType.setName("Użytkownik");

                    user.setAccountType(accountType);
                    user.setEmail(rs.getString("email"));
                    user.setRegister_date(rs.getTimestamp("register_date").toLocalDateTime());
                    return Optional.of(user);
                }
            } catch (SQLException ex) {
                throw new DataAccessException(ex);
            }
        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        }
        return Optional.empty();
    }

}
