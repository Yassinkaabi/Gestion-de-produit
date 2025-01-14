package com.poly.blogmanagment.Controllers;

import com.poly.blogmanagment.entities.Category;
import com.poly.blogmanagment.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    // Afficher la liste des catégories
    @GetMapping
    public String getAllCategories(Model model) {
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        return "categories/list";
    }

    // Afficher le formulaire pour ajouter une nouvelle catégorie
    @GetMapping("/new")
    public String showCreateCategoryForm(Model model) {
        model.addAttribute("category", new Category());
        return "categories/new";
    }

    // Ajouter une nouvelle catégorie
    @PostMapping
    public String createCategory(@ModelAttribute Category category) {
        categoryService.createCategory(category);
        return "redirect:/categories";
    }

    // Afficher le formulaire pour mettre à jour une catégorie
    @GetMapping("/{id}/edit")
    public String showEditCategoryForm(@PathVariable Long id, Model model) {
        Category category = categoryService.getCategoryById(id);
        model.addAttribute("category", category);
        return "categories/edit";
    }

    // Mettre à jour une catégorie existante
    @PostMapping("/{id}/update")
    public String updateCategory(@PathVariable Long id, @ModelAttribute Category updatedCategory) {
        categoryService.updateCategory(id, updatedCategory);
        return "redirect:/categories";
    }

    // Supprimer une catégorie
    @GetMapping("/{id}/delete")
    public String deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return "redirect:/categories";
    }
}
