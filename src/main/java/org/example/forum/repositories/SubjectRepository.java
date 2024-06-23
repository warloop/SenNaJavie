package org.example.forum.repositories;

import jakarta.transaction.Transactional;
import org.example.forum.dao.Interfaces.ISubjectDao;
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

    private ISubjectDao subjectDao;

    @Autowired
    public SubjectRepository(ISubjectDao subjectDAO){
        this.subjectDao = subjectDAO;
    }


    /**
     * Metoda zwraca obiekt typu Subject lub wartość null jeżeli obiekt o określonym w argumencie ID nie istnieje.
     * @param subjectId - ID reprezentujące temat
     * @return Subject | Null w zależności czy temat o okreslonym id istnieje w bazie.
     * @author Artur Leszczak
     * @version 1.0.0
     */
    @Override
    public Optional<Subjects> getSubjectById(long subjectId)
    {
       Subjects subject = this.subjectDao.get(subjectId);

       if(subject!= null) return Optional.of(subject);

       return Optional.empty();
    }

    /**
     * Metoda odpowiedzialna za dodawanie nowego tematu do bazy danych.
     * @param subject Obiekt DTO (SubjectAddDto) zawierający informację o id_użytkownika_dodającego oraz treści_tematu.
     * @return Boolean - metoda zwraca true / false w zależności czy dodawanie tematu powiodło się.
     * @author Artur Leszczak
     * @version 1.0.0
     */
    @Override
    @Transactional
    public Optional<Long> addSubject(SubjectAddDto subject) {
        return this.subjectDao.add(subject);
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
        Subjects sub = this.subjectDao.get(subjectEditDto.getId());


        sub.setSubject_text(subjectEditDto.getSubject_new_text());
        if(sub.getId() == subjectEditDto.getId()){
            if(this.subjectDao.update(sub)){
                this.ACTION_SERVICE.changeSubjectAction(subjectEditDto.getUser_changer_id(),sub.getId());
                return true;
            }
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
    @Override
    @Transactional
    public boolean setBanValueSubjectById(SubjectBanDto subjectBan)
    {
        Subjects subject = this.subjectDao.get(subjectBan.getSubjectId());

        if(subject == null) return false;

        subject.set_banned(subjectBan.isBan_value());

        this.subjectDao.update(subject);

        if(subjectBan.isBan_value())  this.ACTION_SERVICE.banSubjectAction(subjectBan.getUser_id(), subject.getId());
        else this.ACTION_SERVICE.unbanSubjectAction(subjectBan.getUser_id(), subject.getId());

        return true;
    }

    /**
     * Metoda odpowiedzialna za usuwanie tematów (permamentne ukrywanie) w systemie.
     * @param subjectId Wartość long określająca ID tematu, który powinien zostać permamentnie ukryty.
     * @return Boolean - metoda zwraca true / false w zależności czy edycja parametru tematu powiodła się.
     * @author Artur Leszczak
     * @version 1.0.0
     */
    @Override
    @Transactional
    public boolean deleteSubjectById(long subjectId, int user_id, boolean by_owner)
    {
       Subjects sub = this.subjectDao.get(subjectId);

       if(sub.is_deleted()) return false;

       sub.set_deleted(true);

       if(this.subjectDao.update(sub))
       {
           if(by_owner){
             this.ACTION_SERVICE.removeSubjectActionByOwner(user_id, sub.getId());
           }else{
               this.ACTION_SERVICE.removeSubjectActionByModerator(user_id, sub.getId());
           }
       }
        return false;

    }

    /**
     * Metoda pobierająca wszystkie tematy z bazy danych.
     *
     * @return Lista obiektów typu Subjects. Jeśli nie znaleziono żadnych tematów, zwracana jest pusta lista.
     *
     * @author Artur Leszczak
     * @version 1.0.0
     */
    @Override
    public List<Subjects> getAllSubjects() {
        return this.subjectDao.getAll();
    }

}
