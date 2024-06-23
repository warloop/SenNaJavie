package org.example.forum.repositories.Interfaces;

import org.example.forum.entities.Sections;

import java.util.List;
import java.util.Optional;

public interface ISectionRepository {

    Optional<Sections> getSectionById(long sectionId);

    List<Sections> getAllSectionsByArticleId(long articleId);

    Optional<Long> add(Sections section);

    boolean update(Sections section, int userUpdaterId);

    boolean changeVisibility(long sectionId, boolean is_visible, int userUpdaterId);

    boolean delete(long sectionId, int userDeleter);
}
