package com.epam.furniturestoreapp.exceptionHandling;

import com.epam.furniturestoreapp.entity.Category;
import com.epam.furniturestoreapp.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

import static com.epam.furniturestoreapp.model.StaticVariables.TH_ACTION_FOR_ALL_PRODUCTS;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final CategoryService categoryService;

    @Autowired
    public GlobalExceptionHandler(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @ExceptionHandler(AccessDeniedException.class)
    public String handleAccessDeniedExceptionForSignUp(Model model) {
        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories", categories);
        model.addAttribute("thAction", TH_ACTION_FOR_ALL_PRODUCTS);
        model.addAttribute("fail", true);
        return "signup";
    }
}