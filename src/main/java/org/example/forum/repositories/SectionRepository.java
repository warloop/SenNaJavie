package org.example.forum.repositories;

import org.example.forum.dao.Interfaces.ISectionsDao;
import org.example.forum.entities.Sections;
import org.example.forum.repositories.Interfaces.IActionRepository;
import org.example.forum.repositories.Interfaces.ISectionRepository;
import org.example.forum.services.interfaces.IActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
/**
 * Klasa odpowiedzialna za zarządzanie sekcjami w forum.
 * Implementuje interfejs {@link ISectionRepository} i korzysta z {@link ISectionsDao} i {@link IActionRepository} do uzyskiwania dostępu do danych.
 * @author Artur Leszczak
 * @version 1.0.0
 */
@Repository
public class SectionRepository implements ISectionRepository {

    /**
     * Obiekt dostępu do danych dla sekcji.
     */
    private final ISectionsDao sectionsDao;
    /**
     * Repozytorium do zarządzania akcjami związanymi z sekcjami.
     */
    private final IActionService ACTION_SERVICE;

    /**
     * Konstruktor dla klasy SectionRepository.
     * Inicjalizuje zmienne wystąpień {@link #sectionsDao} i {@link #ACTION_SERVICE} z podanych parametrów.
     *
     * @param sectionsDao      Instancja interfejsu {@link ISectionsDao}, używana do uzyskiwania dostępu do danych związanych z sekcjami.
     * @param actionRepository Instancja interfejsu {@link IActionRepository}, używana do zarządzania akcjami związanymi z sekcjami.
     * @author Artur Leszczak
     * @version 1.0.0
     */
    @Autowired
    public SectionRepository(ISectionsDao sectionsDao, IActionService actionRepository) {
        this.sectionsDao = sectionsDao;
        this.ACTION_SERVICE = actionRepository;
    }

    /**
     * Pobiera sekcję z bazy danych na podstawie podanego identyfikatora sekcji.
     *
     * @param sectionId Unikalny identyfikator sekcji do pobrania.
     * @return Obiekt {@link Optional} zawierający obiekt {@link Sections}, jeśli został znaleziony, w przeciwnym razie pusty obiekt {@link Optional}.
     * @author Artur Leszczak
     * @version 1.0.0
     */
    @Override
    public Optional<Sections> getSectionById(long sectionId) {
        return sectionsDao.get(sectionId);
    }

    /**
     * Pobiera wszystkie sekcje związane z określonym artykułem z bazy danych.
     *
     * @param articleId Unikalny identyfikator artykułu, dla którego mają zostać pobrane powiązane sekcje.
     * @return Lista obiektów {@link Sections}, które są powiązane z określonym artykułem.
     * Jeśli nie znaleziono żadnych sekcji, zwracana jest pusta lista.
     * @author Artur Leszczak
     * @version 1.0.0
     */
    @Override
    public List<Sections> getAllSectionsByArticleId(long articleId) {
        return sectionsDao.getSectionsAllInArticle(articleId);
    }

    /**
     * Dodaje nową sekcję do bazy danych.
     *
     * @param section Obiekt sekcji do dodania.
     * @return Wartość {@code true}, jeśli sekcja została pomyślnie dodana, w przeciwnym razie {@code false}.
     * @author Artur Leszczak
     * @version 1.0.0
     */
    @Override
    public Optional<Long> add(Sections section) {

        Optional<Long> createdSectionId = this.sectionsDao.add(section);

        if (createdSectionId.isPresent()) {
            ACTION_SERVICE.addSectionAction(section.getUser_adder_id(),createdSectionId.get());
            return createdSectionId;
        }
        return createdSectionId;
    }

    /**
     * Aktualizuje istniejącą sekcję w bazie danych.
     *
     * @param section       Obiekt sekcji do zaktualizowania.
     * @param userUpdaterId Unikalny identyfikator użytkownika, który aktualizuje sekcję.
     * @return Wartość {@code true}, jeśli sekcja została pomyślnie zaktualizowana, w przeciwnym razie {@code false}.
     * @author Artur Leszczak
     * @version 1.0.0
     */
    @Override
    public boolean update(Sections section, int userUpdaterId) {

        if (sectionsDao.update(section)) {
            ACTION_SERVICE.changeSectionAction(userUpdaterId, section.getId());
            return true;
        }
        return false;
    }

    /**
     * Zmienia widoczność sekcji w bazie danych.
     *
     * @param sectionId     Unikalny identyfikator sekcji, dla której zmieniamy widoczność.
     * @param is_visible    Nowy stan widoczności. Jeśli {@code true}, sekcja będzie widoczna; w przeciwnym razie, będzie ukryta.
     * @param userUpdaterId Unikalny identyfikator użytkownika, który aktualizuje widoczność sekcji.
     * @return Wartość {@code true}, jeśli widoczność sekcji została pomyślnie zmieniona, w przeciwnym razie {@code false}.
     *
     * @author Artur Leszczak
     * @version 1.0.0
     */
    @Override
    public boolean changeVisibility(long sectionId, boolean is_visible, int userUpdaterId)
    {
        Optional<Sections> section = sectionsDao.get(sectionId);

        if(section.isEmpty()) return false;
        Sections changedSection = section.get();

        changedSection.set_visible(is_visible);

        if(sectionsDao.update(changedSection)){
            if(is_visible) ACTION_SERVICE.showSectionAction(userUpdaterId, sectionId);
            else ACTION_SERVICE.hideSectionAction(userUpdaterId, sectionId);

            return true;
        }
        return false;
    }

    /**
     * Usuwa sekcję z bazy danych.
     *
     * @param sectionId Unikalny identyfikator sekcji do usunięcia.
     * @param userDeleter Unikalny identyfikator użytkownika, który usuwa sekcję.
     * @return Wartość {@code true}, jeśli sekcja została pomyślnie usunięta, w przeciwnym razie {@code false}.
     *
     * @author Artur Leszczak
     * @version 1.0.0
     */
    @Override
    public boolean delete(long sectionId, int userDeleter) {
        if(sectionsDao.delete(sectionId)){

            if(sectionsDao.get(sectionId).get().getUser_adder_id() == userDeleter){
                ACTION_SERVICE.removeSectionActionByOwner(userDeleter, sectionId);
            }else{
                ACTION_SERVICE.removeSectionActionByModerator(userDeleter, sectionId);
            }
            return true;
        }
        return false;
    }

}
