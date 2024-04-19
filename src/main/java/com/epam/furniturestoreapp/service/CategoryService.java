package com.epam.furniturestoreapp.service;

import com.epam.furniturestoreapp.entity.Category;
import com.epam.furniturestoreapp.repo.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository){
        this.categoryRepository = categoryRepository;
    }

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Category findById(long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    public void save(Category category) {
        categoryRepository.save(category);
    }

    public void deleteByID(Long categoryID) {
        categoryRepository.deleteById(categoryID);
    }

    public Category findByName(String categoryName) {
        return categoryRepository.findByCategoryName(categoryName);
    }

    public boolean existByName(String categoryName) {
        return categoryRepository.existsByCategoryName(categoryName);
    }
}
