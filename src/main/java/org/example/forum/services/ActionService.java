package org.example.forum.services;

import org.example.forum.dao.Interfaces.ISubjectActionDao;
import org.example.forum.enums.ActionType;
import org.example.forum.repositories.Interfaces.IActionRepository;
import org.example.forum.services.interfaces.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Klasa odpowiadająca za wewnętrzną logikę biznesową aplikacji w przypadku zarządzania Akcjami wywoływanymi podczas
 * różnych zmian w obiektach tematów, artykułów czy sekcji. Waliduje oraz wykonuje niezbędne obliczenia, pobiera
 * odpowiednie dane z osobnych repozytoriów w celu wykonania bardziej złożonych operacji.
 * @author Artur Leszczak
 * @version 1.0.0
 */

@Service
public class ActionService implements IActionService {

    /**
     * Wstrzykiwanie zależności
     */


    private final IActionRepository ACTION_REPOSITORY;

    @Autowired
    public ActionService(IActionRepository actionRepository) {
        this.ACTION_REPOSITORY = actionRepository;
    }

    /**
     * Metoda wykonuje niezbędne czynności do zapisania w bazie danych o odnotowanych czynnościach
     * podjętych przez użytkownika w stosunku do tematów.
     * @param actionType Enum reprezentujący zdarzenia, typ wykonanej przez użytkownika akcji.
     * @param UserId Identyfikator użytkownika wykonującego akcję.
     * @param SubjectId Identyfikator tematu na którym podjęto akcję.
     * @return Boolean (true/false) W zależności od powodzenia operacji.
     * @author Artur Leszczak
     * @version 1.0.0
     */
    private Boolean subjectAction(ActionType actionType, int UserId, long SubjectId)
    {
            return ACTION_REPOSITORY.addSubjectAction(actionType,UserId,SubjectId);

    }

    /* PROSZĘ PATRZEĆ NA NAZWY FUNKCJI, NAZWA FUNKCJI === PRZEZNACZENIE FUNKCJI */

    @Override
    public Boolean addSubjectAction(int UserId, long SubjectId)
    {
        return this.subjectAction(ActionType.ADD, UserId, SubjectId);
    }

    @Override
    public Boolean changeSubjectAction(int UserId, long SubjectId)
    {
        return this.subjectAction(ActionType.EDIT, UserId, SubjectId);
    }

    @Override
    public Boolean showSubjectAction(int UserId, long SubjectId)
    {
        return this.subjectAction(ActionType.SHOW, UserId, SubjectId);
    }

    @Override
    public Boolean hideSubjectAction(int UserId, long SubjectId)
    {
        return this.subjectAction(ActionType.HIDE, UserId, SubjectId);
    }

    @Override
    public Boolean removeSubjectActionByOwner(int UserId, long SubjectId)
    {
        return this.subjectAction(ActionType.DELETED_BY_OWNER, UserId, SubjectId);
    }

    @Override
    public Boolean banSubjectAction(int UserId, long SubjectId)
    {
        return this.subjectAction(ActionType.BANNED, UserId, SubjectId);
    }

    @Override
    public Boolean unbanSubjectAction(int UserId, long SubjectId)
    {
        return this.subjectAction(ActionType.UNBANNED, UserId, SubjectId);
    }

    @Override
    public Boolean removeSubjectActionByModerator(int UserId, long SubjectId)
    {
        return this.subjectAction(ActionType.DELETED_BY_MODERATOR, UserId, SubjectId);
    }

    /**
     * Metoda wykonuje niezbędne czynności do zapisania w bazie danych o odnotowanych czynnościach
     * podjętych przez użytkownika w stosunku do konkretnego artykułu w temacie.
     * @param actionType Identyfikator zdarzenia, typ wykonanej przez użytkownika akcji.
     * @param UserId Identyfikator użytkownika wykonującego akcję.
     * @param ArticleId Identyfikator artykułu na którym podjęto akcję.
     * @return Boolean (true/false) W zależności od powodzenia operacji odnotowywania.
     * @author Artur Leszczak
     * @version 1.0.0
     */
    private Boolean articleAction(ActionType actionType, int UserId, long ArticleId)
    {
        return ACTION_REPOSITORY.addArticleAction(actionType,UserId,ArticleId);
    }

    /* PROSZĘ PATRZEĆ NA NAZWY FUNKCJI, NAZWA FUNKCJI === PRZEZNACZENIE FUNKCJI */

    @Override
    public Boolean addArticleAction(int UserId, long ArticleId)
    {
        return this.articleAction(ActionType.ADD, UserId, ArticleId);
    }

    @Override
    public Boolean changeArticleAction(int UserId, long ArticleId)
    {
        return this.articleAction(ActionType.EDIT, UserId, ArticleId);
    }

    @Override
    public Boolean showArticleAction(int UserId, long ArticleId)
    {
        return this.articleAction(ActionType.SHOW, UserId, ArticleId);
    }

    @Override
    public Boolean hideArticleAction(int UserId, long ArticleId)
    {
        return this.articleAction(ActionType.HIDE, UserId, ArticleId);
    }

    @Override
    public Boolean removeArticleActionByOwner(int UserId, long ArticleId)
    {
        return this.articleAction(ActionType.DELETED_BY_OWNER, UserId, ArticleId);
    }

    @Override
    public Boolean banArticleAction(int UserId, long ArticleId)
    {
        return this.articleAction(ActionType.BANNED, UserId, ArticleId);
    }

    @Override
    public Boolean unbanArticleAction(int UserId, long ArticleId)
    {
        return this.articleAction(ActionType.UNBANNED, UserId, ArticleId);
    }

    @Override
    public Boolean removeArticleActionByModerator(int UserId, long ArticleId)
    {
        return this.articleAction(ActionType.DELETED_BY_MODERATOR, UserId, ArticleId);
    }

    /**
     * Metoda wykonuje niezbędne czynności do zapisania w bazie danych o odnotowanych czynnościach
     * podjętych przez użytkownika w stosunku do konkretnej seckcji w artykule tematu.
     * @param actionType Enum reprezentujące zdarzenie, typ wykonanej przez użytkownika akcji.
     * @param UserId Identyfikator użytkownika wykonującego akcję.
     * @param SectionId Identyfikator sekcji na którym podjęto akcję.
     * @return Boolean (true/false) W zależności od powodzenia operacji odnotowywania.
     * @author Artur Leszczak
     * @version 1.0.0
     */
    private Boolean sectionAction(ActionType actionType, int UserId, long SectionId)
    {
        return ACTION_REPOSITORY.addSectionAction(actionType,UserId,SectionId);
    }

    /* PROSZĘ PATRZEĆ NA NAZWY FUNKCJI, NAZWA FUNKCJI === PRZEZNACZENIE FUNKCJI */
    @Override
    public Boolean addSectionAction(int UserId, long SectionId)
    {
        return this.sectionAction(ActionType.ADD, UserId, SectionId);
    }

    @Override
    public Boolean changeSectionAction(int UserId, long SectionId)
    {
        return this.sectionAction(ActionType.EDIT, UserId, SectionId);
    }

    @Override
    public Boolean showSectionAction(int UserId, long SectionId)
    {
        return this.sectionAction(ActionType.SHOW, UserId, SectionId);
    }

    @Override
    public Boolean hideSectionAction(int UserId, long SectionId)
    {
        return this.sectionAction(ActionType.HIDE, UserId, SectionId);
    }

    @Override
    public Boolean removeSectionActionByOwner(int UserId, long SectionId)
    {
        return this.sectionAction(ActionType.DELETED_BY_OWNER, UserId, SectionId);
    }

    @Override
    public Boolean banSectionAction(int UserId, long SectionId)
    {
        return this.sectionAction(ActionType.BANNED, UserId, SectionId);
    }

    @Override
    public Boolean unbanSectionAction(int UserId, long SectionId)
    {
        return this.sectionAction(ActionType.UNBANNED, UserId, SectionId);
    }

    @Override
    public Boolean removeSectionActionByModerator(int UserId, long SectionId)
    {
        return this.sectionAction(ActionType.DELETED_BY_MODERATOR, UserId, SectionId);
    }

}
