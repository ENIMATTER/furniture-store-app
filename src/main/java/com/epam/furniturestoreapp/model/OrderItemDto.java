package com.epam.furniturestoreapp.model;

import com.epam.furniturestoreapp.entity.OrderItem;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Base64;

@Data
@NoArgsConstructor
public class OrderItemDto {
    private Long productID;
    private String imageString;
    private String productName;
    private String color;
    private double price;
    private int quantity;

    public OrderItemDto(OrderItem orderItem) {
        this.productID = orderItem.getProductID().getProductID();
        this.imageString = Base64.getEncoder().encodeToString(orderItem.getProductID().getImage());
        this.productName = orderItem.getProductID().getProductName();
        this.color = orderItem.getProductID().getColor();
        this.price = orderItem.getProductID().getPrice();
        this.quantity = orderItem.getQuantity();
    }
}
