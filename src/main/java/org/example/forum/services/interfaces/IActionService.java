package org.example.forum.services.interfaces;

public interface IActionService {

    //subjects
    Boolean addSubjectAction(int UserId, long SubjectId);

    Boolean changeSubjectAction(int UserId, long SubjectId);

    Boolean showSubjectAction(int UserId, long SubjectId);

    Boolean hideSubjectAction(int UserId, long SubjectId);

    Boolean removeSubjectActionByOwner(int UserId, long SubjectId);

    Boolean banSubjectAction(int UserId, long SubjectId);

    Boolean unbanSubjectAction(int UserId, long SubjectId);

    Boolean removeSubjectActionByModerator(int UserId, long SubjectId);

    //articles
    Boolean addArticleAction(int UserId, long ArticleId);

    Boolean changeArticleAction(int UserId, long ArticleId);

    Boolean showArticleAction(int UserId, long ArticleId);

    Boolean hideArticleAction(int UserId, long ArticleId);

    Boolean removeArticleActionByOwner(int UserId, long ArticleId);

    Boolean banArticleAction(int UserId, long ArticleId);

    Boolean unbanArticleAction(int UserId, long ArticleId);

    Boolean removeArticleActionByModerator(int UserId, long ArticleId);

    //sections
    Boolean addSectionAction(int UserId, long SectionId);

    Boolean changeSectionAction(int UserId, long SectionId);

    Boolean showSectionAction(int UserId, long SectionId);

    Boolean hideSectionAction(int UserId, long SectionId);

    Boolean removeSectionActionByOwner(int UserId, long SectionId);

    Boolean banSectionAction(int UserId, long SectionId);

    Boolean unbanSectionAction(int UserId, long SectionId);

    Boolean removeSectionActionByModerator(int UserId, long SectionId);

}
