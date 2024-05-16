package org.example.forum.repos;

import jakarta.annotation.Nullable;
import org.example.forum.dto.Subject.SubjectAddDto;
import org.example.forum.dto.Subject.SubjectEditDto;
import org.example.forum.entities.Subjects;
import org.example.forum.exception.DataAccessException;
import org.example.forum.repos.Interfaces.ISubjectRepository;
import org.example.forum.util.ConnectionFactory;
import org.springframework.stereotype.Repository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Repozytorium odpowiedzialne za wykonywanie zadań CRUD z bazą danych w celu zarządzania tematami w forum aplikacji.
 * @author Artur Leszczak
 * @version 1.0.0
 */

@Repository
public class SubjectRepository implements ISubjectRepository {

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
                        subject.setAdd_date(rs.getTimestamp("add_date").toLocalDateTime());
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
    public boolean addSubject(SubjectAddDto subject)
    {
        final String SQL = "INSERT INTO subjects (user_adder_id, subject_text) VALUES (?,?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement statement = conn.prepareStatement(SQL)) {

            conn.setAutoCommit(false);

            statement.setInt(1, subject.getUser_adder_id());
            statement.setString(2, subject.getSubject_text());

            int affectedRows = statement.executeUpdate();

            if (affectedRows > 0) {
                conn.commit();
                return true;
            } else {
                conn.rollback();
                throw new SQLException("Nie udało się dodać użytkownika, proces przerwany!");
            }

        } catch (SQLException ex) {
            throw new DataAccessException(ex);
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
     * @param subjectId Wartość long określająca ID tematu, który powinien zostać ukryty bądź wyświetlony.
     * @param ban_value Wartość boolean określająca, czy temat ma być zbanowany (true) , czy odblokowany (false).
     * @return Boolean - metoda zwraca true / false w zależności czy edycja parametru tematu powiodła się.
     * @author Artur Leszczak
     * @version 1.0.0
     */
    public boolean setBanValueSubjectById(long subjectId, boolean ban_value)
    {
        final String SQL = "UPDATE subjects SET is_banned = ? WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement statement = conn.prepareStatement(SQL)) {

            conn.setAutoCommit(false);

            statement.setBoolean(1, ban_value);
            statement.setLong(2, subjectId);

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                conn.rollback();
                throw new SQLException("Nie udało się zmienić wartości blokady dla subjectId ("+subjectId+")!");
            }

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    conn.commit();
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
    public boolean deleteSubjectById(long subjectId)
    {
        final String SQL = "UPDATE subjects SET is_deleted = ? WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement statement = conn.prepareStatement(SQL)) {

            conn.setAutoCommit(false);

            statement.setBoolean(1, true);
            statement.setLong(2, subjectId);

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                conn.rollback();
                throw new SQLException("Nie udało się usunąć subjectId ("+subjectId+")!");
            }

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    conn.commit();
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

}
