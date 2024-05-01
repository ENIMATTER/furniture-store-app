package com.epam.furniturestoreapp.controller;

import com.epam.furniturestoreapp.entity.UserTable;
import com.epam.furniturestoreapp.model.CardDto;
import com.epam.furniturestoreapp.model.UserSignUpDto;
import com.epam.furniturestoreapp.service.CategoryService;
import com.epam.furniturestoreapp.service.UserTableService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountControllerTest {

    @Mock
    private Model model;

    @Mock
    private UserTableService userTableService;

    @Mock
    private CategoryService categoryService;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AccountController accountController;

    @Test
    void testGetSignup() {
        String result = accountController.getSignup(model);

        verify(model, times(1)).addAttribute(eq("categories"), any());
        verify(model, times(1)).addAttribute(eq("thAction"), any());

        assertEquals("signup", result);
    }

    @Test
    void testPostSignup_Success() {
        UserSignUpDto userSignUpDto = new UserSignUpDto();
        userSignUpDto.setEmail("test@example.com");
        userSignUpDto.setUserPassword("password");
        userSignUpDto.setUserPasswordAgain("password");

        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);

        String result = accountController.postSignup(userSignUpDto, bindingResult, model);

        verify(userTableService, times(1)).addUser(any(UserTable.class));
        verify(model, times(1)).addAttribute(eq("categories"), any());
        verify(model, times(1)).addAttribute(eq("thAction"), any());

        assertEquals("login", result);
    }

    @Test
    void testPostSignup_FailValidation() {
        UserSignUpDto userSignUpDto = new UserSignUpDto();

        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);

        String result = accountController.postSignup(userSignUpDto, bindingResult, model);

        verify(userTableService, never()).addUser(any(UserTable.class));
        verify(model, times(1)).addAttribute(eq("fail"), eq(true));

        assertEquals("signup", result);
    }

    @Test
    void testGetLogin() {
        String result = accountController.getLogin(model);

        verify(model, times(1)).addAttribute(eq("categories"), any());
        verify(model, times(1)).addAttribute(eq("thAction"), any());

        assertEquals("login", result);
    }

    @Test
    void testCustomLogout() {
        SecurityContextHolder.setContext(securityContext);

        when(securityContext.getAuthentication()).thenReturn(authentication);

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        String result = accountController.customLogout(request, response, model);

        verify(securityContext).setAuthentication(null);

        assertEquals("login", result);
    }

    @Test
    void testAccount() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(authentication.getName()).thenReturn("testUser");

        UserTable user = new UserTable();
        user.setBalance(0.0);
        when(userTableService.getUserByEmail(anyString())).thenReturn(user);

        String result = accountController.account(model);

        verify(model, times(1)).addAttribute(eq("balance"), anyDouble());
        verify(model, times(1)).addAttribute(eq("categories"), any());
        verify(model, times(1)).addAttribute(eq("thAction"), any());

        assertEquals("account", result);
    }

    @Test
    void testDeleteAccount() {
        SecurityContextHolder.setContext(securityContext);

        String emailUsername = "test@example.com";
        UserTable user = new UserTable();
        user.setEmail(emailUsername);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(emailUsername);
        when(userTableService.getUserByEmail(emailUsername)).thenReturn(user);

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        String result = accountController.deleteAccount(request, response, model);

        verify(userTableService).deleteUser(user);

        assertEquals("index", result);
    }

    @Test
    void testGetTopUp() {
        String result = accountController.getTopUp(model);

        verify(model, times(1)).addAttribute(eq("min"), anyString());
        verify(model, times(1)).addAttribute(eq("categories"), any());
        verify(model, times(1)).addAttribute(eq("thAction"), any());

        assertEquals("top-up", result);
    }

    @Test
    void testPostTopUp_Success() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(authentication.getName()).thenReturn("testUser");

        UserTable user = new UserTable();
        user.setBalance(0.0);
        when(userTableService.getUserByEmail(anyString())).thenReturn(user);

        String result = accountController.postTopUp(100.0, model, new CardDto(), mock(BindingResult.class));

        verify(userTableService, times(1)).editUser(any(UserTable.class));
        verify(model, times(1)).addAttribute(eq("balance"), anyDouble());
        verify(model, times(1)).addAttribute(eq("categories"), any());

        assertEquals("account", result);
    }

    @Test
    void testPostTopUp_FailValidation() {
        Model model = mock(Model.class);
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);

        String result = accountController.postTopUp(100.0, model, new CardDto(), bindingResult);

        verify(userTableService, never()).editUser(any(UserTable.class));
        verify(model, times(1)).addAttribute(eq("fail"), eq(true));

        assertEquals("top-up", result);
    }
}