package com.varkaikin.passwordwallet.controller;

import com.varkaikin.passwordwallet.dto.*;
import com.varkaikin.passwordwallet.model.Log;
import com.varkaikin.passwordwallet.security.JwtUtils;
import com.varkaikin.passwordwallet.service.LogService;
import com.varkaikin.passwordwallet.service.UserService;
import org.mockito.Mock;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.testng.Assert.assertEquals;

public class UserControllerTest {

    @Mock
    private UserService mockUserService;
    @Mock
    private AuthenticationManager mockAuthenticationManager;
    @Mock
    private JwtUtils mockJwtUtils;
    @Mock
    private LogService mockLogService;

    private UserController userControllerUnderTest;

    @BeforeMethod
    public void setUp() {
        initMocks(this);
        userControllerUnderTest = new UserController(mockUserService, mockAuthenticationManager, mockJwtUtils, mockLogService);
    }



    @Test
    public void testSaveNewUser() {

        final UserDTORegister userDTORegister = new UserDTORegister();
        userDTORegister.setLogin("login");
        userDTORegister.setPassword("password");
        userDTORegister.setIsPasswordSavedAsHash(false);
        when(mockUserService.existsByLogin("login")).thenReturn(false);

        final UserResponse userResponse = new UserResponse();
        userResponse.setUserId("1");
        userResponse.setUserLogin("userLogin");
        when(mockUserService.add(any(UserDTORegister.class))).thenReturn(userResponse);

        HttpHeaders headers = new HttpHeaders();
        assertEquals(userControllerUnderTest.save(userDTORegister),
                new ResponseEntity<>(userResponse,headers,HttpStatus.CREATED) );
    }

    @Test
    public void testLogin() {

        final UserDTO userDTO = new UserDTO();
        userDTO.setLogin("login");
        userDTO.setPassword("password");

        final HttpServletRequest request = new MockHttpServletRequest();
        when(mockUserService.existsByLogin("login")).thenReturn(true);
        when(mockUserService.login(any(UserDTO.class))).thenReturn(true);
        when(mockLogService.getLoginStatus("login", false, "remoteAddr")).thenReturn(0);

        final Log log = new Log();
        log.setId(0L);
        log.setAttempt(0);
        log.setIpAddress("ipAddress");
        log.setLogin("login");
        log.setTime(LocalDateTime.of(2020, 1, 1, 0, 0, 0));
        when(mockLogService.findLastSuccessful("ipAddress", "login")).thenReturn(log);

        final Log log1 = new Log();
        log1.setId(0L);
        log1.setAttempt(0);
        log1.setIpAddress("ipAddress");
        log1.setLogin("login");
        log1.setTime(LocalDateTime.of(2020, 1, 1, 0, 0, 0));
        when(mockLogService.findLastUnSuccessful("ipAddress", "login")).thenReturn(log1);

        final ResponseEntity<LoginResponse> result = userControllerUnderTest.login(userDTO, request);
        HttpStatus resultStatusCode = result.getStatusCode();

        assertEquals(resultStatusCode.toString(), "200 OK");
    }

    @Test
    public void testChangePassword() throws Exception {

        final ChangeUserPasswordRequest uRequest = new ChangeUserPasswordRequest();
        uRequest.setLogin("login");
        uRequest.setOldPassword("oldPassword");
        uRequest.setNewPassword("newPassword");
        uRequest.setIsPasswordSavedAsHash(false);

        final ResponseEntity<Boolean> expectedResult = new ResponseEntity<>(false, HttpStatus.OK);
        when(mockUserService.changePassword(any(ChangeUserPasswordRequest.class))).thenReturn(false);

        final ResponseEntity<Boolean> result = userControllerUnderTest.changePassword(uRequest);

        assertEquals(expectedResult, result);
    }

    @Test(expectedExceptions = {Exception.class})
    public void testChangePasswordThrowsException() throws Exception {

        final ChangeUserPasswordRequest uRequest = new ChangeUserPasswordRequest();
        uRequest.setLogin("login");
        uRequest.setOldPassword("oldPassword");
        uRequest.setNewPassword("newPassword");
        uRequest.setIsPasswordSavedAsHash(false);

        when(mockUserService.changePassword(any(ChangeUserPasswordRequest.class))).thenThrow(Exception.class);

        userControllerUnderTest.changePassword(uRequest);
    }
}
