package com.epam.furniturestoreapp.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CardDto {
    @NotEmpty(message = "Card number is required")
    @Pattern(regexp="\\d{16}", message = "Card number must be 16 digits")
    private String cardNumber;

    @NotEmpty(message = "CVV is required")
    @Pattern(regexp="\\d{3}", message = "CVV must be 3 digits")
    private String cvv;
}
