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

import static com.epam.furniturestoreapp.StaticVariablesForTests.getTestUser;
import static com.epam.furniturestoreapp.StaticVariablesForTests.getTestUserSignUpDto;
import static com.epam.furniturestoreapp.model.StaticVariables.TH_ACTION_FOR_ALL_PRODUCTS;
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

    @Mock
    BindingResult bindingResult;

    @InjectMocks
    private AccountController accountController;

    @Test
    void testGetSignup() {
        String result = accountController.getSignup(model);

        verifyModel();

        assertEquals("signup", result);
    }

    @Test
    void testPostSignup_Success() {
        UserSignUpDto userSignUpDto = getTestUserSignUpDto();

        when(bindingResult.hasErrors()).thenReturn(false);

        String result = accountController.postSignup(userSignUpDto, bindingResult, model);

        verify(userTableService, times(1)).addUser(any(UserTable.class));
        verifyModel();

        assertEquals("login", result);
    }

    @Test
    void testPostSignup_FailValidation() {
        UserSignUpDto userSignUpDto = getTestUserSignUpDto();

        when(bindingResult.hasErrors()).thenReturn(true);

        String result = accountController.postSignup(userSignUpDto, bindingResult, model);

        verify(userTableService, never()).addUser(any(UserTable.class));
        verify(model, times(1)).addAttribute(eq("fail"), eq(true));
        verifyModel();

        assertEquals("signup", result);
    }

    @Test
    void testGetLogin() {
        String result = accountController.getLogin(model);

        verifyModel();

        assertEquals("login", result);
    }

    @Test
    void testCustomLogout() {
        SecurityContextHolder.setContext(securityContext);

        when(securityContext.getAuthentication()).thenReturn(authentication);

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        String result = accountController.customLogout(request, response, model);

        verify(securityContext, times(1)).setAuthentication(null);
        verifyModel();

        assertEquals("login", result);
    }

    @Test
    void testAccount() {
        SecurityContextHolder.setContext(securityContext);
        UserTable user = getTestUser();

        when(userTableService.getUserByEmail(anyString())).thenReturn(user);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testUser");

        String result = accountController.account(model);

        verify(model, times(1)).addAttribute(eq("balance"), anyDouble());
        verifyModel();

        assertEquals("account", result);
    }

    @Test
    void testDeleteAccount() {
        SecurityContextHolder.setContext(securityContext);

        UserTable user = getTestUser();

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(user.getEmail());
        when(userTableService.getUserByEmail(user.getEmail())).thenReturn(user);

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        String result = accountController.deleteAccount(request, response, model);

        verify(userTableService, times(1)).deleteUser(user);
        verifyModel();

        assertEquals("index", result);
    }

    @Test
    void testGetTopUp() {
        String result = accountController.getTopUp(model);

        verify(model, times(1)).addAttribute(eq("min"), anyString());
        verifyModel();

        assertEquals("top-up", result);
    }

    @Test
    void testPostTopUp_Success() {
        SecurityContextHolder.setContext(securityContext);
        UserTable user = getTestUser();

        when(userTableService.getUserByEmail(anyString())).thenReturn(user);
        when(authentication.getName()).thenReturn("testUser");
        when(securityContext.getAuthentication()).thenReturn(authentication);

        String result = accountController.postTopUp(100.0, model, new CardDto(), bindingResult);

        verify(userTableService, times(1)).editUser(any(UserTable.class));
        verify(model, times(1)).addAttribute(eq("balance"), anyDouble());
        verifyModel();

        assertEquals("account", result);
    }

    @Test
    void testPostTopUp_FailValidation() {
        when(bindingResult.hasErrors()).thenReturn(true);

        String result = accountController.postTopUp(100.0, model, new CardDto(), bindingResult);

        verify(userTableService, never()).editUser(any(UserTable.class));
        verify(model, times(1)).addAttribute(eq("fail"), eq(true));
        verifyModel();

        assertEquals("top-up", result);
    }

    private void verifyModel() {
        verify(model, times(1)).addAttribute(eq("categories"), anyList());
        verify(model, times(1)).addAttribute(eq("thAction"), eq(TH_ACTION_FOR_ALL_PRODUCTS));
    }
}