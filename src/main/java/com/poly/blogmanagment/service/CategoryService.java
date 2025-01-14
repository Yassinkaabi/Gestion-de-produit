package com.poly.blogmanagment.service;


import com.poly.blogmanagment.entities.Category;

import java.util.List;

public interface CategoryService {
    List<Category> getAllCategories();
    Category getCategoryById(Long id);
    Category createCategory(Category category);
    Category updateCategory(Long id, Category updatedCategory);
    void deleteCategory(Long id);
}
