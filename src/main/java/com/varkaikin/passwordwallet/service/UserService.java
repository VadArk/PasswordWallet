package com.varkaikin.passwordwallet.service;

import com.varkaikin.passwordwallet.dto.ChangeUserPasswordRequest;
import com.varkaikin.passwordwallet.dto.UserDTO;
import com.varkaikin.passwordwallet.dto.UserDTORegister;
import com.varkaikin.passwordwallet.dto.UserResponse;
import com.varkaikin.passwordwallet.exceptions.LoginExistsException;
import com.varkaikin.passwordwallet.model.User;

public interface UserService {

    Boolean changePassword(ChangeUserPasswordRequest changeUserPasswordRequest) throws Exception;
    UserResponse add(UserDTORegister userDTORegister) throws LoginExistsException;
    Boolean login(UserDTO userDTO);
    User findUserByLogin(String login);
    Boolean existsByLogin(String login);
    String getPasswordHashValueByPassword(String password, Boolean isPasswordSavedAsHash, String salt);
}
