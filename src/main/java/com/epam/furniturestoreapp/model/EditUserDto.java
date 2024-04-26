package com.epam.furniturestoreapp.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EditUserDto {
    private Long userTableID;

    @Size(max = 50, message = "Firstname must be less than 50")
    @NotBlank(message = "Firstname is mandatory")
    private String firstname;

    @Size(max = 50, message = "Lastname must be less than 50")
    @NotBlank(message = "Lastname is mandatory")
    private String lastname;

    @Size(max = 50, message = "Email must be less than 50")
    @NotBlank(message = "Email is mandatory")
    private String email;

    @Size(max = 100, message = "User password must be less than 100")
    private String userPassword;

    @Pattern(regexp="\\d{9}", message = "Phone number be 9 digits")
    @NotBlank(message = "Phone number is mandatory")
    private String phoneNumber;
}
