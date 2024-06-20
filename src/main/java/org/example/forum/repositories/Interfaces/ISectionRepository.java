package org.example.forum.repositories.Interfaces;

import org.example.forum.entities.Sections;

import java.util.List;
import java.util.Optional;

public interface ISectionRepository {

    Optional<Sections> getSectionById(long id);
    Optional<Long> addSection(Sections section);
    boolean editSection(Sections section);
    List<Sections> getAllSectionsByArticleId(long articleId);
}
