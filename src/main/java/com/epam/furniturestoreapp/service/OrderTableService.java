package com.epam.furniturestoreapp.service;

import com.epam.furniturestoreapp.entity.OrderTable;
import com.epam.furniturestoreapp.entity.UserTable;
import com.epam.furniturestoreapp.repo.OrderTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderTableService {
    private final OrderTableRepository orderTableRepository;

    @Autowired
    public OrderTableService(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    public void addOrder(OrderTable orderTable) {
        orderTableRepository.save(orderTable);
    }

    public List<OrderTable> getAllByUser(UserTable user) {
        return orderTableRepository.getAllByUserTableID(user);
    }
}
