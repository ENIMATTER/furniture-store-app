package com.epam.furniturestoreapp.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AddressDto {
    @Size(max = 50, message = "Street must be less than 50")
    @NotBlank(message = "Street is mandatory")
    String street;

    @NotNull(message = "House is mandatory")
    Integer house;

    @NotNull(message = "Floor is mandatory")
    Integer floor;

    @NotNull(message = "Apartment is mandatory")
    Integer apartment;

    @Size(max = 300, message = "message must be less than 300")
    String message;
}
