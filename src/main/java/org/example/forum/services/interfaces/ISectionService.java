package org.example.forum.services.interfaces;

import org.example.forum.dto.Section.SectionAddDto;
import org.example.forum.dto.Section.SectionEditDto;
import org.example.forum.entities.Sections;

import java.util.List;

public interface ISectionService {
    void addSection(SectionAddDto sectionAddDto);
    boolean editSectionText(SectionEditDto sectionEditDto);
    Sections findSectionById(long id);
    List<Sections> getAllSectionsByArticleId(long articleId);
}

