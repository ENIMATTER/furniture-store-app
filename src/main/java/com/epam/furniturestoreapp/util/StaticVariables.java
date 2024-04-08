package com.epam.furniturestoreapp.util;

import org.springframework.security.core.context.SecurityContextHolder;

public class StaticVariables {
    public static final String thActionForAllProducts = "/products";
    public static final String thActionForProductsByCategory = "/products/category/";
    public static final String emailUsername = SecurityContextHolder.getContext().getAuthentication().getName();
}
