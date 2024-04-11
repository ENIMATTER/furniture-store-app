package com.epam.furniturestoreapp.service;

import com.epam.furniturestoreapp.entity.OrderItem;
import com.epam.furniturestoreapp.entity.OrderTable;
import com.epam.furniturestoreapp.repo.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderItemService {
    private final OrderItemRepository orderItemRepository;

    @Autowired
    public OrderItemService(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    public void addAllOrderItems(List<OrderItem> orderItems) {
        orderItemRepository.saveAll(orderItems);
    }

    public List<OrderItem> getAllByOrderTable(OrderTable orderTable) {
        return orderItemRepository.getAllByOrderTableID(orderTable);
    }
}
