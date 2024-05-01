package com.epam.furniturestoreapp.model;

import com.epam.furniturestoreapp.entity.CartItem;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Base64;

@Data
@NoArgsConstructor
public class CartItemDto {
    private Long cartItemID;
    private Long productID;
    private String imageString;
    private String productName;
    private String color;
    private double price;
    private int quantity;
    private BigDecimal totals;

    public CartItemDto(CartItem cartItem) {
        this.cartItemID = cartItem.getCartItemID();
        this.productID = cartItem.getProductID().getProductID();
        this.imageString = Base64.getEncoder().encodeToString(cartItem.getProductID().getImage());
        this.productName = cartItem.getProductID().getProductName();
        this.color = cartItem.getProductID().getColor();
        this.price = cartItem.getProductID().getPrice();
        this.quantity = cartItem.getQuantity();
        this.totals = BigDecimal.valueOf(cartItem.getQuantity() * cartItem.getProductID().getPrice())
                .setScale(2, RoundingMode.HALF_UP);
    }
}
