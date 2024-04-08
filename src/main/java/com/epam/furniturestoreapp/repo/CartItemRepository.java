package com.epam.furniturestoreapp.repo;

import com.epam.furniturestoreapp.entity.CartItem;
import com.epam.furniturestoreapp.entity.UserTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> getAllByUserTableID(UserTable userTableID);
}
