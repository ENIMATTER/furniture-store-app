package com.epam.furniturestoreapp.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserSignUpDto {
    @NotEmpty(message = "First name is required")
    @Size(max = 50, message = "First name must be less than 50 characters")
    private String firstname;

    @NotEmpty(message = "Last name is required")
    @Size(max = 50, message = "Last name must be less than 50 characters")
    private String lastname;

    @NotEmpty(message = "Email is required")
    @Email(message = "Invalid email format")
    @Size(max = 50, message = "Email must be less than 50 characters")
    private String email;

    @NotEmpty(message = "Password is required")
    @Size(max = 50, message = "Password must be less than 50 characters")
    private String userPassword;

    @NotEmpty(message = "Password confirmation is required")
    @Size(max = 50, message = "Password must be less than 50 characters")
    private String userPasswordAgain;

    @NotEmpty(message = "Phone number is required")
    @Pattern(regexp="\\d{9}", message="Phone number must be 9 digits")
    private String phoneNumber;
}
