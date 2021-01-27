package com.varkaikin.passwordwallet.service;

import com.varkaikin.passwordwallet.dto.ChangeUserPasswordRequest;
import com.varkaikin.passwordwallet.dto.UserDTO;
import com.varkaikin.passwordwallet.dto.UserDTORegister;
import com.varkaikin.passwordwallet.dto.UserResponse;
import com.varkaikin.passwordwallet.model.ERole;
import com.varkaikin.passwordwallet.model.Role;
import com.varkaikin.passwordwallet.model.User;
import com.varkaikin.passwordwallet.repository.FunctionRunRepository;
import com.varkaikin.passwordwallet.repository.RoleRepository;
import com.varkaikin.passwordwallet.repository.UserRepository;
import org.mockito.Mock;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.testng.Assert.*;

public class UserServiceImplementationTest {

    @Mock
    private UserRepository mockUserRepository;
    @Mock
    private RoleRepository mockRoleRepository;
    @Mock
    private PasswordService mockPasswordService;
    @Mock
    FunctionRunRepository functionRunRepository;
    @Mock
    private LogService logService;

    private UserServiceImplementation userServiceImplementationUnderTest;


    @BeforeMethod
    public void setUp() {
        initMocks(this);
        userServiceImplementationUnderTest = new
                UserServiceImplementation(mockUserRepository,
                mockRoleRepository,
                mockPasswordService,
                logService, functionRunRepository);
    }

    @Test
    public void testFindUserByLogin() {

        User user = new User();
        user.setLogin("login");
        when(mockUserRepository.findByLogin("login")).thenReturn(user);

        final User result = userServiceImplementationUnderTest.findUserByLogin("login");
        assertEquals(user, result);

    }

    @Test
    public void testExistsByLogin() {

        when(mockUserRepository.findByLogin("login")).thenReturn(new User());

        final Boolean result = userServiceImplementationUnderTest.existsByLogin("login");

        assertTrue(result);
    }

    @Test
    public void testChangePassword() throws Exception {

        final ChangeUserPasswordRequest userRequest = new ChangeUserPasswordRequest();
        userRequest.setLogin("login");
        userRequest.setOldPassword("oldPassword");
        userRequest.setNewPassword("newPassword");
        userRequest.setIsPasswordSavedAsHash(false);
        User user = new User();
        user.setLogin("login");
        Boolean ifMatch = true;
        when(mockUserRepository.findByLogin("login")).thenReturn(user);
        when(mockPasswordService.changeAllUsersPasswords("login", "oldMasterPass", "NewMasterPass")).thenReturn(false);
        //final Boolean result = userServiceImplementationUnderTest.changePassword(userRequest);

    }

    @Test
    public void testLogin() {
        final UserDTO userDTO = new UserDTO();
        userDTO.setLogin("login");
        userDTO.setPassword("password");
        User user = new User();
        user.setLogin("login");

        when(mockUserRepository.findByLogin("login")).thenReturn(user);

        final Boolean result = userServiceImplementationUnderTest.login(userDTO);

        assertNotEquals(result, true);
    }


    @Test
    public void testGetPasswordHashValueByPassword() {

        final String result = userServiceImplementationUnderTest.getPasswordHashValueByPassword(
                "password", false, "salt");
        assertEquals("kmLVxKOA+b6xjSE372mXZOhLTw3NJSzDQ9Io8pUAz3pLcZF4urIirDycAbrl54/yYLLg9ka2yYQ2GKenKwLWgA==",
                result);
    }

    @Test
    public void testAdd() {

        final UserDTORegister userDTORegister = new UserDTORegister();
        userDTORegister.setLogin("login");
        userDTORegister.setPassword("password");
        userDTORegister.setIsPasswordSavedAsHash(false);

        User user = new User();
        Set<Role> roles = new HashSet<>();
        roles.add(new Role(ERole.ROLE_USER));
        user.setRoles(roles);
        user.setLogin("login");
        user.setId(1L);

        when(mockRoleRepository.save(any(Role.class))).thenReturn(new Role(ERole.ROLE_USER));
        when(mockUserRepository.save(any(User.class))).thenReturn(user);

        UserResponse userResp = new UserResponse();
        userResp.setUserLogin(user.getLogin());
        userResp.setUserId(user.getId().toString());



    }


}
