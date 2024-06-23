package org.example.forum.controllers.Section;

import jakarta.servlet.http.HttpServletRequest;
import org.example.forum.dto.Section.SectionAddDto;
import org.example.forum.dto.Section.SectionEditDto;
import org.example.forum.entities.Sections;
import org.example.forum.services.interfaces.ISectionService;
import org.example.forum.services.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class SectionController {

    @Autowired
    ISectionService SECTION_SERVICE;

    @Autowired
    IUserService USER_SERVICE;

    @GetMapping("/protected/articles/article/{id}/create")
    public String createSectionPageShow(@PathVariable("id") long articleId, Model model) {
        model.addAttribute("articleId", articleId);
        return "section-creator";
    }

    @PostMapping("/protected/articles/article/{articleId}/create")
    public String createSection(@PathVariable("articleId") long articleId,
                                @RequestParam("section_text") String sectionText,
                                HttpServletRequest request,
                                RedirectAttributes redirectAttributes) {
        try {
            int userId = Integer.parseInt(request.getSession().getAttribute("userId").toString());
            SectionAddDto newSection = new SectionAddDto(userId, articleId, sectionText);
            SECTION_SERVICE.addSection(newSection);
            redirectAttributes.addFlashAttribute("message", "Sekcja została pomyślnie dodana.");
            return "redirect:/protected/articles/article/" + articleId;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Wystąpił błąd podczas dodawania sekcji.");
            return "redirect:/protected/articles/article/" + articleId + "/create";
        }
    }

    @GetMapping("/protected/articles/article/{articleId}/edit/{id}")
    public String editSectionPage(@PathVariable("articleId") long articleId,
                                  @PathVariable("id") long sectionId, Model model) {
        Sections section = SECTION_SERVICE.findSectionById(sectionId);
        if (section != null) {
            model.addAttribute("section", section);
            model.addAttribute("articleId", articleId);
            return "edit-section";
        } else {
            return "redirect:/protected/articles/article/" + articleId;
        }
    }

    @PostMapping("/protected/articles/article/{articleId}/edit")
    public String editSection(@PathVariable("articleId") long articleId,
                              @RequestParam("sectionId") long sectionId,
                              @RequestParam("section_text") String sectionText,
                              HttpServletRequest request,
                              RedirectAttributes redirectAttributes) {
        try {
            int userId = Integer.parseInt(request.getSession().getAttribute("userId").toString());

            SectionEditDto sectionEditDto = new SectionEditDto(sectionId, userId, sectionText);

            boolean success = SECTION_SERVICE.editSectionText(sectionEditDto);

            if (success) {
                redirectAttributes.addFlashAttribute("message", "Edycja sekcji zakończona sukcesem.");
            } else {
                redirectAttributes.addFlashAttribute("error", "Edycja sekcji nie powiodła się.");
            }

            return "redirect:/protected/articles/article/" + articleId;
        } catch (NumberFormatException e) {
            redirectAttributes.addFlashAttribute("error", "Niepoprawny format ID użytkownika.");
            return "redirect:/protected/articles/article/" + articleId;
        }
    }
}