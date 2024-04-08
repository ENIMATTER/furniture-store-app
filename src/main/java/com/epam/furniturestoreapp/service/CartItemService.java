package com.epam.furniturestoreapp.service;

import com.epam.furniturestoreapp.entity.CartItem;
import com.epam.furniturestoreapp.entity.UserTable;
import com.epam.furniturestoreapp.repo.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartItemService {
    private final CartItemRepository cartItemRepository;

    @Autowired
    public CartItemService(CartItemRepository cartItemRepository) {
        this.cartItemRepository = cartItemRepository;
    }

    public List<CartItem> getAllItemsByUser(UserTable user) {
        return cartItemRepository.getAllByUserTableID(user);
    }
}
