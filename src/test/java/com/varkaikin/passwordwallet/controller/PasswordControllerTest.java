package com.varkaikin.passwordwallet.controller;

import com.varkaikin.passwordwallet.dto.*;
import com.varkaikin.passwordwallet.service.PasswordService;
import org.mockito.Mock;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.testng.Assert.assertEquals;

public class PasswordControllerTest {

    @Mock
    private PasswordService mockPasswordService;
    private PasswordController passwordControllerUnderTest;

    @BeforeMethod
    public void setUp() {
        initMocks(this);
        passwordControllerUnderTest = new PasswordController(mockPasswordService);
    }

    @Test
    public void testSave() throws Exception {

        final AddPasswordRequest passwordRequest = new AddPasswordRequest();
        passwordRequest.setMasterPassword("masterPassword");
        passwordRequest.setUserLogin("userLogin");
        passwordRequest.setLogin("login");
        passwordRequest.setPassword("password");
        passwordRequest.setWeb_address("web_address");
        passwordRequest.setDescription("description");

        final PasswordRequest passwordRequest1 = new PasswordRequest();
        passwordRequest1.setUserLogin("userLogin");
        passwordRequest1.setLogin("login");
        passwordRequest1.setPassword("password");
        passwordRequest1.setWeb_address("web_address");
        passwordRequest1.setDescription("description");
        when(mockPasswordService.add(any(AddPasswordRequest.class))).thenReturn(passwordRequest1);

        final ResponseEntity<PasswordRequest> actual = passwordControllerUnderTest.save(passwordRequest);
        HttpHeaders headers = new HttpHeaders();
        ResponseEntity<PasswordRequest> expected = new ResponseEntity<>(passwordRequest1, headers, HttpStatus.CREATED);

        assertEquals(actual, expected );
    }


    @Test
    public void testShowPassword() throws Exception {

        final ShowPasswordRequest showPasswordRequest = new ShowPasswordRequest();
        showPasswordRequest.setPasswordId("passwordId");
        showPasswordRequest.setMasterPassword("masterPassword");
        HttpHeaders headers = new HttpHeaders();
        final ResponseEntity<String> expectedResult = new ResponseEntity<>("result",headers,  HttpStatus.OK);

        when(mockPasswordService.showPassword(any(ShowPasswordRequest.class))).thenReturn("result");
        final ResponseEntity<String> result = passwordControllerUnderTest.showPassword(showPasswordRequest);
        assertEquals(result, expectedResult);
    }


    @Test
    public void testGetAllByLogin() {
        final PasswordResponse passwordResponse = new PasswordResponse();
        final PasswordsContent passwordsContent = new PasswordsContent();
        passwordsContent.setId("id");
        passwordsContent.setLogin("login");
        passwordsContent.setPassword("password");
        passwordsContent.setWeb_address("web_address");
        passwordsContent.setDescription("description");
        passwordResponse.setContent(Collections.singletonList(passwordsContent));
        passwordResponse.setTotalPages("tatalPages");
        passwordResponse.setTotalElements("tatalElements");
        passwordResponse.setNumber("number");
        when(mockPasswordService.findAllByUserLogin(eq("login"), any(Pageable.class))).thenReturn(passwordResponse);

        final ResponseEntity<PasswordResponse> result = passwordControllerUnderTest.getAllByLogin(
                "login",
                "1",
                "2");

        HttpHeaders headers = new HttpHeaders();
        final ResponseEntity<PasswordResponse> expectedResult = new ResponseEntity<>(passwordResponse,headers,  HttpStatus.OK);

        assertEquals(result, expectedResult);




    }
}
