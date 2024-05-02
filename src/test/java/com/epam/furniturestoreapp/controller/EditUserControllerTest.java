package com.epam.furniturestoreapp.controller;

import com.epam.furniturestoreapp.entity.UserTable;
import com.epam.furniturestoreapp.model.EditUserDto;
import com.epam.furniturestoreapp.service.CategoryService;
import com.epam.furniturestoreapp.service.UserTableService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;

import static com.epam.furniturestoreapp.StaticVariablesForTests.getTestEditUserDto;
import static com.epam.furniturestoreapp.StaticVariablesForTests.getTestUser;
import static com.epam.furniturestoreapp.model.StaticVariables.TH_ACTION_FOR_ALL_PRODUCTS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EditUserControllerTest {

    @Mock
    UserTableService userTableService;

    @Mock
    CategoryService categoryService;

    @InjectMocks
    EditUserController editUserController;

    @Mock
    Model model;

    @Mock
    HttpServletRequest request;

    @Mock
    HttpServletResponse response;

    @Mock
    BindingResult bindingResult;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @Test
    public void testGetEditUser() {
        SecurityContextHolder.setContext(securityContext);
        UserTable user = getTestUser();

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(categoryService.findAll()).thenReturn(new ArrayList<>());
        when(userTableService.getUserByEmail(any())).thenReturn(user);

        String result = editUserController.getEditUser(model);

        verifyModel();

        assertEquals("edit-user", result);
    }

    @Test
    public void testPutEditUser_Success() {
        SecurityContextHolder.setContext(securityContext);
        EditUserDto userDto = getTestEditUserDto();
        UserTable user = getTestUser();

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(userTableService.getUserByEmail(any())).thenReturn(user);
        when(bindingResult.hasErrors()).thenReturn(false);
        when(userTableService.existsByEmail(any())).thenReturn(false);
        when(userTableService.getUserById(anyLong())).thenReturn(user);

        String result = editUserController.putEditUser(request, response, userDto, bindingResult, model);

        verify(userTableService, times(1)).editUser(any(UserTable.class));
        verify(securityContext, times(1)).setAuthentication(null);
        verifyModel();

        assertEquals("login", result);
    }

    @Test
    public void testPutEditUser_ExistingEmail() {
        SecurityContextHolder.setContext(securityContext);
        EditUserDto userDto = getTestEditUserDto();

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(bindingResult.hasErrors()).thenReturn(false);
        when(userTableService.existsByEmail(any())).thenReturn(true);

        String result = editUserController.putEditUser(request, response, userDto, bindingResult, model);

        verify(userTableService, never()).editUser(any(UserTable.class));
        verify(model, times(1)).addAttribute(eq("exist"), eq(true));
        verifyModel();

        assertEquals("edit-user", result);
    }

    @Test
    public void testPutEditUser_ValidationFailure() {
        SecurityContextHolder.setContext(securityContext);
        EditUserDto userDto = getTestEditUserDto();

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(bindingResult.hasErrors()).thenReturn(true);

        String viewName = editUserController.putEditUser(request, response, userDto, bindingResult, model);

        verify(userTableService, never()).editUser(any(UserTable.class));
        verify(model, times(1)).addAttribute(eq("fail"), eq(true));
        verifyModel();

        assertEquals("edit-user", viewName);
    }

    private void verifyModel(){
        verify(model, times(1)).addAttribute(eq("user"), any());
        verify(model, times(1)).addAttribute(eq("categories"), anyList());
        verify(model, times(1)).addAttribute(eq("thAction"), eq(TH_ACTION_FOR_ALL_PRODUCTS));
    }
}