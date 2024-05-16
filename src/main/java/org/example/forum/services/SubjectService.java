package org.example.forum.services;

import org.example.forum.dto.Subject.SubjectAddDto;
import org.example.forum.dto.System.InformationReturned;
import org.example.forum.exception.SubjectLengthTooLongException;
import org.example.forum.exception.UserIsNotExistsException;
import org.example.forum.repos.Interfaces.ISubjectRepository;
import org.example.forum.repos.Interfaces.IUserRepository;
import org.example.forum.services.interfaces.ISubjectService;
import org.example.forum.services.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubjectService implements ISubjectService {

    @Autowired
    ISubjectRepository SUBJECT_REPOSITORY;

    @Autowired
    IUserRepository USER_REPOSITORY;

    /**
     *
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

            if(SUBJECT_REPOSITORY.addSubject(subject))
            {
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
}
