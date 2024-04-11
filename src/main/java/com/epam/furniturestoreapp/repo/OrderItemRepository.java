package com.epam.furniturestoreapp.repo;

import com.epam.furniturestoreapp.entity.OrderItem;
import com.epam.furniturestoreapp.entity.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> getAllByOrderTableID(OrderTable orderTableID);
}
