package com.epam.furniturestoreapp.model;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserDto {
    @Size(max = 50, message = "Firstname must be less than 50")
    @NotBlank(message = "Firstname is mandatory")
    private String firstname;

    @Size(max = 50, message = "Lastname must be less than 50")
    @NotBlank(message = "Lastname is mandatory")
    private String lastname;

    @Email(message = "Invalid email format")
    @Size(max = 50, message = "Email must be less than 50 characters")
    @NotBlank(message = "Email is mandatory")
    private String email;

    @Size(max = 100, message = "User password must be less than 100")
    @NotBlank(message = "User password is mandatory")
    private String password;

    @Pattern(regexp="\\d{9}", message = "Phone number be 9 digits")
    @NotBlank(message = "Phone number is mandatory")
    private String phone;

    @NotNull(message = "Balance is mandatory")
    private Double balance;

    @Size(max = 50, message = "Roles must be less than 50")
    @NotBlank(message = "Roles is mandatory")
    private String roles;
}
