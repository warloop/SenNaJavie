package org.example.forum.dao.Interfaces;

import org.example.forum.entities.Sections;

import java.util.List;
import java.util.Optional;

public interface ISectionsDao {

    Optional<Sections> get(long sectionId);

    List<Sections> getSectionsAllInArticle(long articleId);

    Optional<Long> add(Sections section);

    Boolean update(Sections section);

    Boolean delete(long sectionId);

}
