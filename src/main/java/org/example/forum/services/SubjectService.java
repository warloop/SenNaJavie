package org.example.forum.services;

import org.example.forum.dto.Subject.SubjectAddDto;
import org.example.forum.dto.System.InformationReturned;
import org.example.forum.entities.Subjects;
import org.example.forum.exception.SubjectLengthTooLongException;
import org.example.forum.exception.UserIsNotExistsException;
import org.example.forum.repositories.Interfaces.ISubjectRepository;
import org.example.forum.repositories.Interfaces.IUserRepository;
import org.example.forum.services.interfaces.IActionService;
import org.example.forum.services.interfaces.ISubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Klasa odpowiadająca za wewnętrzną logikę biznesową aplikacji w przypadku zarządzania Tematami. Waliduje oraz wykonuje
 * niezbędne obliczenia oraz pobiera odpowiednie dane z osobnych repozytoriów w celu wykonania bardziej złożonych operacji.
 * @author Artur Leszczak
 * @version 1.0.0
 */

@Service
public class SubjectService implements ISubjectService {

    /**
     * Wstrzykiwanie zależności
     */

    @Autowired
    ISubjectRepository SUBJECT_REPOSITORY;

    @Autowired
    IUserRepository USER_REPOSITORY;

    @Autowired
    IActionService ACTION_SERVICE;
    /**
     * Metoda waliduje oraz przekazuje zlecenie dodania nowego tematu dla repozytorium.
     * @param subject Obiekt typu SubjectAddDto zawierający informacje o temacie oraz id_użytkownika_dodającego
     * @return InformationReturned - Zwraca kod i komunikat informujący o powodzeniu lub błędzie.
     * @author Artur Leszczak
     * @version 1.0.0
     */
    @Override
    public InformationReturned addSubject(SubjectAddDto subject) {

        try{

            if(subject.getSubject_text().length() > 128)
            {
                throw new SubjectLengthTooLongException(400, "Treść tematu jest zbyt długa! Limit to 128 znaków!");
            }

            if((subject.getUser_adder_id() <= 0) || USER_REPOSITORY.findById(subject.getUser_adder_id()).isEmpty()){
                throw new UserIsNotExistsException(400, "Nie znaleziono uzytkownika o podanym ID!");
            }
            Optional<Long> addedSubjectId = SUBJECT_REPOSITORY.addSubject(subject);
            if(addedSubjectId.isPresent() && addedSubjectId.get() > 0)
            {
                //dodanie informacji o utworzeniu nowego tematu
                ACTION_SERVICE.addSubjectAction(subject.getUser_adder_id(), addedSubjectId.get());

                return new InformationReturned(201, "Poprawnie utworzono nowy temat");
            }

            throw new Exception("Nie udało się dodać tematu!");

        }catch (SubjectLengthTooLongException e){
            return new InformationReturned(e.getCode(), e.getMessage());
        }catch (UserIsNotExistsException e){
            return new InformationReturned(e.getCode(), e.getMessage());
        }catch (Exception e)
        {
            return new InformationReturned(500, e.getMessage());
        }

    }

    /**
     * Metoda sprawdza, czy temat istnieje.
     * @param SubjectId Wartość identyfikatora tematu.
     * @return Obiekt typu Optional może przyjąć wartości null / true / false
     * @author Artur Leszczak
     * @version 1.0.0
     */
    @Override
    public Optional<Boolean> isSubjectExists(long SubjectId)
    {
        if(SubjectId > 0)
        {
            Optional<Subjects> getSubject = SUBJECT_REPOSITORY.getSubjectById(SubjectId);
            if(getSubject.isPresent())
            {
                return Optional.of(true);
            }
            return Optional.of(false);
        }

        return Optional.empty();
    }
    public List<Subjects> getAllSubjects(){
        return SUBJECT_REPOSITORY.getAllSubjects();
    }
}
