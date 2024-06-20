package org.example.forum.services;

import org.example.forum.dto.Section.SectionAddDto;
import org.example.forum.dto.Section.SectionEditDto;
import org.example.forum.entities.Sections;
import org.example.forum.repositories.Interfaces.ISectionRepository;
import org.example.forum.services.interfaces.ISectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SectionService implements ISectionService {

    @Autowired
    ISectionRepository SECTION_REPOSITORY;
    @Override
    public void addSection(SectionAddDto sectionAddDto) {
        Sections section = new Sections(
                sectionAddDto.getUser_adder_id(),
                sectionAddDto.getArticle_id(),
                sectionAddDto.getSection_text()
        );
        SECTION_REPOSITORY.addSection(section);
    }

    @Override
    public boolean editSectionText(SectionEditDto sectionEditDto) {
        Sections section = SECTION_REPOSITORY.getSectionById(sectionEditDto.getId()).orElse(null);
        if (section != null) {
            section.setSection_text(sectionEditDto.getSection_text());
            SECTION_REPOSITORY.editSection(section);
            return true;
        }
        return false;
    }

    @Override
    public Sections findSectionById(long id) {
        return SECTION_REPOSITORY.getSectionById(id).orElse(null);
    }

    @Override
    public List<Sections> getAllSectionsByArticleId(long articleId) {
        return SECTION_REPOSITORY.getAllSectionsByArticleId(articleId);
    }
}
