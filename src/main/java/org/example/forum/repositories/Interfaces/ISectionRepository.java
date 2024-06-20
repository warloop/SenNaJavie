package org.example.forum.repositories.Interfaces;

import org.example.forum.entities.Sections;

import java.util.List;
import java.util.Optional;

public interface ISectionRepository {

    Optional<Sections> get(long sectionId);

    List<Sections> getSectionsAllInArticle(long articleId);

    boolean add(Sections section);

    boolean update(Sections section, int userUpdaterId);

    boolean changeVisibility(long sectionId, boolean is_visible, int userUpdaterId);

    boolean delete(long sectionId, int userDeleter);
}
