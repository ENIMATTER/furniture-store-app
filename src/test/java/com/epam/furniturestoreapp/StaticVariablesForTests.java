package com.epam.furniturestoreapp;

import com.epam.furniturestoreapp.entity.*;
import com.epam.furniturestoreapp.model.*;
import org.springframework.mock.web.MockMultipartFile;

import java.time.LocalDateTime;

public class StaticVariablesForTests {
    public static UserTable getTestUser(){
        UserTable user = new UserTable("firstname", "lastname", "email@text.com", "userPassword",
                "123123123", 0.0, "roles");
        user.setUserTableID(1L);
        return user;
    }

    public static AdminEditUserDto getTestAdminEditUserDto(){
        AdminEditUserDto user = new AdminEditUserDto();
        user.setUserTableID(1L);
        user.setFirstname("firstname");
        user.setLastname("lastname");
        user.setEmail("email@text.com");
        user.setUserPassword("userPassword");
        user.setPhoneNumber("123123123");
        user.setBalance(0.0);
        user.setRoles("roles");
        return user;
    }

    public static UserDto getTestUserDto(){
        UserDto user = new UserDto();
        user.setFirstname("firstname");
        user.setLastname("lastname");
        user.setEmail("email@text.com");
        user.setPassword("userPassword");
        user.setPhone("123123123");
        user.setBalance(0.0);
        user.setRoles("roles");
        return user;
    }

    public static UserSignUpDto getTestUserSignUpDto(){
        UserSignUpDto userSignUpDto = new UserSignUpDto();
        userSignUpDto.setEmail("test@example.com");
        userSignUpDto.setUserPassword("password");
        userSignUpDto.setUserPasswordAgain("password");
        return userSignUpDto;
    }

    public static EditUserDto getTestEditUserDto(){
        EditUserDto editUserDto = new EditUserDto();
        editUserDto.setUserTableID(1L);
        editUserDto.setFirstname("firstname");
        editUserDto.setLastname("lastname");
        editUserDto.setEmail("test@example.com");
        editUserDto.setUserPassword("newPassword");
        editUserDto.setPhoneNumber("123123123");
        return editUserDto;
    }

    public static Product getTestProduct(){
        Product product = new Product("productName", "productDescription",
                getTestCategory(), 100.0, 100, "dimensions", "material",
                "color", 5.0);
        product.setImage(new byte[1]);
        product.setProductID(1L);
        return product;
    }

    public static ProductDto getTestProductDto(){
        ProductDto productDto = new ProductDto("productName", "productDescription",
                100.0, 100, "dimensions", new Material[]{Material.GLASS}, Color.BLUE);
        productDto.setImage(new MockMultipartFile("name", new byte[3]));
        return productDto;
    }

    public static Category getTestCategory(){
        Category testCategory = new Category();
        testCategory.setCategoryID(1L);
        testCategory.setCategoryName("testCategory");
        return testCategory;
    }

    public static Review getTestReview(){
        Review review = new Review();
        review.setReviewID(1L);
        review.setProductID(getTestProduct());
        review.setUserTableID(getTestUser());
        review.setRating(5);
        review.setReviewComment("ReviewComment");
        review.setReviewDate(LocalDateTime.now());
        return review;
    }

    public static ReviewDto getTestReviewDto(){
        ReviewDto reviewDto = new ReviewDto();
        reviewDto.setReviewID(1L);
        reviewDto.setProductID(1L);
        reviewDto.setUserTableID(1L);
        reviewDto.setRating(5);
        reviewDto.setReviewComment("ReviewComment");
        reviewDto.setReviewDate(LocalDateTime.now());
        return reviewDto;
    }

    public static CartItem getTestCartItem(){
        CartItem cartItem = new CartItem();
        cartItem.setUserTableID(getTestUser());
        cartItem.setProductID(getTestProduct());
        cartItem.setQuantity(1);
        return cartItem;
    }

    public static AddressDto getTestAddressDto(){
        AddressDto addressDto = new AddressDto();
        addressDto.setStreet("Test Street");
        addressDto.setHouse(1);
        addressDto.setFloor(2);
        addressDto.setApartment(3);
        addressDto.setMessage("Test message");
        return addressDto;
    }
}
