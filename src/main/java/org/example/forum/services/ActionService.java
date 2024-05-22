package org.example.forum.services;

import org.example.forum.dao.Interfaces.ISubjectActionDao;
import org.example.forum.repositories.Interfaces.IActionRepository;
import org.example.forum.services.interfaces.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ActionService implements IActionService {

    @Autowired
    private IActionRepository ACTION_REPOSITORY;

    /**
     * Metoda wykonuje niezbędne czynności do zapisania w bazie danych o odnotowanych czynnościach
     * podjętych przez użytkownika w stosunku do tematów.
     * @param ReportTypeId Identyfikator zdarzenia, typ wykonanej przez użytkownika akcji.
     * @param UserId Identyfikator użytkownika wykonującego akcję.
     * @param SubjectId Identyfikator tematu na którym podjęto akcję.
     * @return Boolean (true/false) W zależności od powodzenia operacji.
     * @author Artur Leszczak
     * @version 1.0.0
     */
    private Boolean subjectAction(int ReportTypeId, int UserId, long SubjectId)
    {
            return ACTION_REPOSITORY.addSubjectAction(ReportTypeId,UserId,SubjectId);

    }

    /* PROSZĘ PATRZEĆ NA NAZWY FUNKCJI, NAZWA FUNKCJI === PRZEZNACZENIE FUNKCJI */

    @Override
    public Boolean addSubjectAction(int UserId, long SubjectId)
    {
        return this.subjectAction(1, UserId, SubjectId);
    }

    @Override
    public Boolean changeSubjectAction(int UserId, long SubjectId)
    {
        return this.subjectAction(2, UserId, SubjectId);
    }

    @Override
    public Boolean showSubjectAction(int UserId, long SubjectId)
    {
        return this.subjectAction(3, UserId, SubjectId);
    }

    @Override
    public Boolean hideSubjectAction(int UserId, long SubjectId)
    {
        return this.subjectAction(4, UserId, SubjectId);
    }

    @Override
    public Boolean removeSubjectActionByOwner(int UserId, long SubjectId)
    {
        return this.subjectAction(5, UserId, SubjectId);
    }

    @Override
    public Boolean banSubjectAction(int UserId, long SubjectId)
    {
        return this.subjectAction(6, UserId, SubjectId);
    }

    @Override
    public Boolean unbanSubjectAction(int UserId, long SubjectId)
    {
        return this.subjectAction(7, UserId, SubjectId);
    }

    @Override
    public Boolean removeSubjectActionByModerator(int UserId, long SubjectId)
    {
        return this.subjectAction(8, UserId, SubjectId);
    }

    /**
     * Metoda wykonuje niezbędne czynności do zapisania w bazie danych o odnotowanych czynnościach
     * podjętych przez użytkownika w stosunku do konkretnego artykułu w temacie.
     * @param ReportTypeId Identyfikator zdarzenia, typ wykonanej przez użytkownika akcji.
     * @param UserId Identyfikator użytkownika wykonującego akcję.
     * @param ArticleId Identyfikator artykułu na którym podjęto akcję.
     * @return Boolean (true/false) W zależności od powodzenia operacji odnotowywania.
     * @author Artur Leszczak
     * @version 1.0.0
     */
    private Boolean articleAction(int ReportTypeId, int UserId, long ArticleId)
    {
        return ACTION_REPOSITORY.addArticleAction(ReportTypeId,UserId,ArticleId);
    }

    /* PROSZĘ PATRZEĆ NA NAZWY FUNKCJI, NAZWA FUNKCJI === PRZEZNACZENIE FUNKCJI */

    @Override
    public Boolean addArticleAction(int UserId, long ArticleId)
    {
        return this.articleAction(1, UserId, ArticleId);
    }

    @Override
    public Boolean changeArticleAction(int UserId, long ArticleId)
    {
        return this.articleAction(2, UserId, ArticleId);
    }

    @Override
    public Boolean showArticleAction(int UserId, long ArticleId)
    {
        return this.articleAction(3, UserId, ArticleId);
    }

    @Override
    public Boolean hideArticleAction(int UserId, long ArticleId)
    {
        return this.articleAction(4, UserId, ArticleId);
    }

    @Override
    public Boolean removeArticleActionByOwner(int UserId, long ArticleId)
    {
        return this.articleAction(5, UserId, ArticleId);
    }

    @Override
    public Boolean banArticleAction(int UserId, long ArticleId)
    {
        return this.articleAction(6, UserId, ArticleId);
    }

    @Override
    public Boolean unbanArticleAction(int UserId, long ArticleId)
    {
        return this.articleAction(7, UserId, ArticleId);
    }

    @Override
    public Boolean removeArticleActionByModerator(int UserId, long ArticleId)
    {
        return this.articleAction(8, UserId, ArticleId);
    }

    /**
     * Metoda wykonuje niezbędne czynności do zapisania w bazie danych o odnotowanych czynnościach
     * podjętych przez użytkownika w stosunku do konkretnej seckcji w artykule tematu.
     * @param ReportTypeId Identyfikator zdarzenia, typ wykonanej przez użytkownika akcji.
     * @param UserId Identyfikator użytkownika wykonującego akcję.
     * @param SectionId Identyfikator sekcji na którym podjęto akcję.
     * @return Boolean (true/false) W zależności od powodzenia operacji odnotowywania.
     * @author Artur Leszczak
     * @version 1.0.0
     */
    private Boolean sectionAction(int ReportTypeId, int UserId, long SectionId)
    {
        return ACTION_REPOSITORY.addSectionAction(ReportTypeId,UserId,SectionId);
    }

    /* PROSZĘ PATRZEĆ NA NAZWY FUNKCJI, NAZWA FUNKCJI === PRZEZNACZENIE FUNKCJI */
    @Override
    public Boolean addSectionAction(int UserId, long SectionId)
    {
        return this.sectionAction(1, UserId, SectionId);
    }

    @Override
    public Boolean changeSectionAction(int UserId, long SectionId)
    {
        return this.sectionAction(2, UserId, SectionId);
    }

    @Override
    public Boolean showSectionAction(int UserId, long SectionId)
    {
        return this.sectionAction(3, UserId, SectionId);
    }

    @Override
    public Boolean hideSectionAction(int UserId, long SectionId)
    {
        return this.sectionAction(4, UserId, SectionId);
    }

    @Override
    public Boolean removeSectionActionByOwner(int UserId, long SectionId)
    {
        return this.sectionAction(5, UserId, SectionId);
    }

    @Override
    public Boolean banSectionAction(int UserId, long SectionId)
    {
        return this.sectionAction(6, UserId, SectionId);
    }

    @Override
    public Boolean unbanSectionAction(int UserId, long SectionId)
    {
        return this.sectionAction(7, UserId, SectionId);
    }

    @Override
    public Boolean removeSectionActionByModerator(int UserId, long SectionId)
    {
        return this.sectionAction(8, UserId, SectionId);
    }

}
