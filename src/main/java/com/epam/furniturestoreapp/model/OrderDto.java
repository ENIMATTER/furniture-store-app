package com.epam.furniturestoreapp.model;

import com.epam.furniturestoreapp.entity.OrderItem;
import com.epam.furniturestoreapp.entity.OrderTable;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class OrderDto {
    private Long orderTableID;
    private LocalDateTime orderDate;
    private List<OrderItemDto> orderItems;
    private String street;
    private int house;
    private int floor;
    private int apartment;
    private double totalAmount;

    public OrderDto(OrderTable orderTable) {
        this.orderTableID = orderTable.getOrderTableID();
        this.orderDate = orderTable.getOrderDate();
        this.orderItems = new ArrayList<>();
        for (OrderItem orderItem : orderTable.getOrderItems()) {
            orderItems.add(new OrderItemDto(orderItem));
        }
        this.street = orderTable.getAddress().getStreet();
        this.house = orderTable.getAddress().getHouse();
        this.floor = orderTable.getAddress().getFloor();
        this.apartment = orderTable.getAddress().getApartment();
        this.totalAmount = orderTable.getTotalAmount();
    }
}
