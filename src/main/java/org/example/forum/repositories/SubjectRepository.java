package org.example.forum.repositories;

import org.example.forum.dto.Subject.SubjectAddDto;
import org.example.forum.dto.Subject.SubjectBanDto;
import org.example.forum.dto.Subject.SubjectEditDto;
import org.example.forum.entities.Subjects;
import org.example.forum.exception.DataAccessException;
import org.example.forum.repositories.Interfaces.ISubjectRepository;
import org.example.forum.services.interfaces.IActionService;
import org.example.forum.util.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Repozytorium odpowiedzialne za wykonywanie zadań CRUD z bazą danych w celu zarządzania tematami w forum aplikacji.
 * @author Artur Leszczak
 * @version 1.0.0
 */

@Repository
public class SubjectRepository implements ISubjectRepository {


    @Autowired
    private IActionService ACTION_SERVICE;

    /**
     * Metoda zwraca obiekt typu Subject lub wartość null jeżeli obiekt o określonym w argumencie ID nie istnieje.
     * @param subjectId - ID reprezentujące temat
     * @return Subject | Null w zależności czy temat o okreslonym id istnieje w bazie.
     * @author Artur Leszczak
     * @version 1.0.0
     */
    public Optional<Subjects> getSubjectById(long subjectId)
    {
        final String SQL = "SELECT * FROM subjects WHERE id = ? AND is_banned = 0 AND is_deleted = 0";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement statement = conn.prepareStatement(SQL)) {
            statement.setLong(1, subjectId);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);

                    if(count > 0)
                    {
                        Subjects subject = new Subjects();

                        subject.setId(rs.getLong("id"));
                        subject.setUser_adder_id(rs.getInt("user_adder_id"));
                        subject.setSubject_text(rs.getString("subject_text"));
                        subject.set_banned(rs.getBoolean("is_banned"));
                        subject.set_deleted(rs.getBoolean("is_deleted"));

                        return Optional.of(subject);
                    }


                    return Optional.of(null);
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        }
        return Optional.of(null);
    }

    /**
     * Metoda odpowiedzialna za dodawanie nowego tematu do bazy danych.
     * @param subject Obiekt DTO (SubjectAddDto) zawierający informację o id_użytkownika_dodającego oraz treści_tematu.
     * @return Boolean - metoda zwraca true / false w zależności czy dodawanie tematu powiodło się.
     * @author Artur Leszczak
     * @version 1.0.0
     */
    public Optional<Long> addSubject(SubjectAddDto subject) {
        final String SQL = "INSERT INTO subjects (user_adder_id, subject_text) VALUES (?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement statement = conn.prepareStatement(SQL, PreparedStatement.RETURN_GENERATED_KEYS)) {  // Użycie RETURN_GENERATED_KEYS

            conn.setAutoCommit(false);

            statement.setInt(1, subject.getUser_adder_id());
            statement.setString(2, subject.getSubject_text());

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                conn.rollback();
                throw new SQLException("Nie udało się dodać użytkownika, proces przerwany!");
            }

            try (ResultSet rs = statement.getGeneratedKeys()) {  // Pobieranie wygenerowanych kluczy
                if (rs.next()) {
                    conn.commit();
                    long addedSubjectId = rs.getLong(1);  // Pobieranie pierwszej kolumny (wygenerowane ID)
                    return Optional.of(addedSubjectId);
                } else {
                    conn.rollback();
                    return Optional.empty();
                }
            } catch (SQLException e) {
                conn.rollback();
                throw new DataAccessException(e);
            }

        } catch (SQLException ex) {
            return Optional.empty();
        }
    }

    /**
     * Metoda odpowiedzialna za zmienianie treści tematu.
     * @param subjectEditDto Obiekt DTO (SubjectEditDto) zawierające informację o id_tematu , id_osoby_zmieniającej, nowej_treści_tematu.
     * @return Boolean - metoda zwraca true / false w zależności czy edycja parametru tematu powiodła się.
     * @author Artur Leszczak
     * @version 1.0.0
     */
    public boolean editSubjectText(SubjectEditDto subjectEditDto)
    {
        final String SQL = "UPDATE subjects SET subject_text = ? WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement statement = conn.prepareStatement(SQL)) {

            conn.setAutoCommit(false);

            statement.setString(1, subjectEditDto.getSubject_new_text());
            statement.setLong(2, subjectEditDto.getId());

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                conn.rollback();
                throw new SQLException("Nie udało się zmienić treści tematu dla subjectId ("+subjectEditDto.getId()+")!");
            }

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    conn.commit();

                    //odnotowanie zmiany treści tematu
                    ACTION_SERVICE.changeSubjectAction(subjectEditDto.getUser_changer_id(), subjectEditDto.getId());

                    return true;
                }
            }catch (Exception e){
                conn.rollback();
                throw new DataAccessException(e);
            }
        }catch (SQLException ex) {

            throw new DataAccessException(ex);
        }

        return false;
    }

    /**
     * Metoda odpowiedzialna za ukrywanie (banowanie) określonych tematów w serwisie.
     * @param subjectBan Obiekt DTO Zawiera wartość typu bool określającą, czy nałożyć blokadę, id_tematu którego
     * dotyczy, id_użytkownika nakładającego blokadę (Administrator lub Moderator)
     * @return Boolean - metoda zwraca true / false w zależności czy edycja parametru tematu powiodła się.
     * @author Artur Leszczak
     * @version 1.0.0
     */
    public boolean setBanValueSubjectById(SubjectBanDto subjectBan)
    {
        final String SQL = "UPDATE subjects SET is_banned = ? WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement statement = conn.prepareStatement(SQL)) {

            conn.setAutoCommit(false);

            statement.setBoolean(1, subjectBan.isBan_value());
            statement.setLong(2, subjectBan.getSubjectId());

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                conn.rollback();
                throw new SQLException("Nie udało się zmienić wartości blokady dla subjectId ("+ subjectBan.getSubjectId()+")!");
            }

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    conn.commit();

                    //odnotowanie zablokowania / odblokowania tematu

                    if(subjectBan.isBan_value())
                    {
                        ACTION_SERVICE.banSubjectAction(subjectBan.getUser_id(), subjectBan.getSubjectId());
                    }
                    else
                    {
                        ACTION_SERVICE.unbanSubjectAction(subjectBan.getUser_id(), subjectBan.getSubjectId());
                    }

                    return true;
                }
            }catch (Exception e){
                conn.rollback();
                throw new DataAccessException(e);
            }
        }catch (SQLException ex) {

            throw new DataAccessException(ex);
        }

        return false;

    }

    /**
     * Metoda odpowiedzialna za usuwanie tematów (permamentne ukrywanie) w systemie.
     * @param subjectId Wartość long określająca ID tematu, który powinien zostać permamentnie ukryty.
     * @return Boolean - metoda zwraca true / false w zależności czy edycja parametru tematu powiodła się.
     * @author Artur Leszczak
     * @version 1.0.0
     */
    public boolean deleteSubjectById(long subjectId, int user_id, boolean by_owner)
    {
        final String SQL = "UPDATE subjects SET is_deleted = 1 WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement statement = conn.prepareStatement(SQL)) {

            conn.setAutoCommit(false);

            statement.setLong(1, subjectId);

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                conn.rollback();
                throw new SQLException("Nie udało się usunąć subjectId ("+subjectId+")!");
            }

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    conn.commit();

                    if(by_owner) ACTION_SERVICE.removeSubjectActionByOwner(user_id, subjectId);
                    else ACTION_SERVICE.removeSubjectActionByModerator(user_id, subjectId);

                    return true;
                }
            }catch (Exception e){
                conn.rollback();
                throw new DataAccessException(e);
            }
        }catch (SQLException ex) {

            throw new DataAccessException(ex);
        }

        return false;
    }

    @Override
    public List<Subjects> getAllSubjects() {
        final String SQL = "SELECT * FROM subjects";
        List<Subjects> result = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement statement = conn.prepareStatement(SQL)) {

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                        Subjects subject = new Subjects();
                        subject.setId(rs.getLong("id"));
                        subject.setUser_adder_id(rs.getInt("user_adder_id"));
                        subject.setSubject_text(rs.getString("subject_text"));
                        subject.set_banned(rs.getBoolean("is_banned"));
                        subject.set_deleted(rs.getBoolean("is_deleted"));
                        result.add(subject);
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        }
        return result;
    }

}
