package org.example.forum.controllers.User;

import org.example.forum.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class Category {
    @Autowired
    CategoryRepository categoryRepository;
    @GetMapping("/protected/category")
    public String ViewCategories(Model model) {
        model.addAttribute("categories", categoryRepository.getAllCategories());

        return "category";
    }

    @GetMapping("/protected/category/create")
    public String createCategory(){
        return "create_category";
    }
    @PostMapping("/protected/category/create")
    public String createCategoryView(@RequestParam("name") String name ){
        categoryRepository.addCategory(name);
        return "category";
    }

}
