package com.epam.furniturestoreapp.controller;

import com.epam.furniturestoreapp.entity.UserTable;
import com.epam.furniturestoreapp.model.AdminEditUserDto;
import com.epam.furniturestoreapp.model.UserDto;
import com.epam.furniturestoreapp.service.UserTableService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import static com.epam.furniturestoreapp.StaticVariablesForTests.*;
import static com.epam.furniturestoreapp.model.StaticVariables.TH_ACTION_FOR_ALL_PRODUCTS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class AdminUserControllerTest {
    @InjectMocks
    private AdminUserController adminUserController;

    @Mock
    private UserTableService userTableService;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    @Test
    public void testGetUsersAdmin() {
        List<UserTable> mockUsers = Arrays.asList(new UserTable(), new UserTable());

        when(userTableService.getAll()).thenReturn(mockUsers);

        String viewName = adminUserController.getUsersAdmin(model);

        verify(userTableService, times(1)).getAll();
        verifyModel(mockUsers);

        assertEquals("users-admin", viewName);
    }

    @Test
    public void testAddUserAdmin_Success() {
        List<UserTable> mockUsers = new ArrayList<>();
        UserDto userDto = getTestUserDto();

        when(bindingResult.hasErrors()).thenReturn(false);

        String viewName = adminUserController.addUserAdmin(userDto, bindingResult, model);

        verify(userTableService, times(1)).addUser(any(UserTable.class));
        verifyModel(mockUsers);

        assertEquals("users-admin", viewName);
    }

    @Test
    public void testAddUserAdmin_Failure() {
        List<UserTable> mockUsers = new ArrayList<>();
        UserDto userDto = getTestUserDto();

        when(bindingResult.hasErrors()).thenReturn(true);

        String viewName = adminUserController.addUserAdmin(userDto, bindingResult, model);

        verify(userTableService, never()).addUser(any(UserTable.class));
        verify(model, times(1)).addAttribute("fail", true);
        verifyModel(mockUsers);

        assertEquals("users-admin", viewName);
    }

    @Test
    public void testEditUserAdmin_Success() {
        List<UserTable> mockUsers = new ArrayList<>();
        AdminEditUserDto user = getTestAdminEditUserDto();
        UserTable existingUser = getTestUser();

        when(bindingResult.hasErrors()).thenReturn(false);
        when(userTableService.getUserById(user.getUserTableID())).thenReturn(existingUser);
        when(userTableService.existsByEmail(anyString())).thenReturn(false);

        String viewName = adminUserController.editUserAdmin(user, bindingResult, model);

        verify(userTableService, times(1)).editUser(existingUser);
        verifyModel(mockUsers);

        assertEquals("users-admin", viewName);
    }

    @Test
    public void testEditUserAdmin_Failure() {
        List<UserTable> mockUsers = new ArrayList<>();
        AdminEditUserDto user = getTestAdminEditUserDto();

        when(bindingResult.hasErrors()).thenReturn(true);

        String viewName = adminUserController.editUserAdmin(user, bindingResult, model);

        verify(userTableService, never()).editUser(any(UserTable.class));
        verify(model, times(1)).addAttribute("fail", true);
        verifyModel(mockUsers);

        assertEquals("users-admin", viewName);
    }

    @Test
    public void testDeleteUserAdmin() {
        List<UserTable> mockUsers = new ArrayList<>();
        Long userId = 1L;

        when(userTableService.existsById(userId)).thenReturn(true);

        String viewName = adminUserController.deleteUserAdmin(userId, model);

        verify(userTableService, times(1)).deleteUserById(userId);
        verifyModel(mockUsers);

        assertEquals("users-admin", viewName);
    }

    @Test
    public void testDeleteUserAdmin_UserNotFound() {
        List<UserTable> mockUsers = new ArrayList<>();
        Long userId = 1L;

        when(userTableService.existsById(userId)).thenReturn(false);

        String viewName = adminUserController.deleteUserAdmin(userId, model);

        verify(userTableService, never()).deleteUserById(userId);
        verifyModel(mockUsers);

        assertEquals("users-admin", viewName);
    }

    private void verifyModel(List<UserTable> users) {
        verify(model, times(1)).addAttribute("users", users);
        verify(model, times(1)).addAttribute("thAction", TH_ACTION_FOR_ALL_PRODUCTS);
    }
}
