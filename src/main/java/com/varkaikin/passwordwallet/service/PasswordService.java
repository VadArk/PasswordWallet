package com.varkaikin.passwordwallet.service;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.varkaikin.passwordwallet.dto.*;
import org.springframework.data.domain.Pageable;

public interface PasswordService {
    PasswordRequest add(AddPasswordRequest passwordRequest) throws Exception;
    PasswordResponse findAllByUserLogin(String login, Pageable pageable);
    String showPassword(ShowPasswordRequest showPasswordRequest) throws Exception;
    Boolean changeAllUsersPasswords(String login, String oldMasterPass, String NewMasterPass) throws Exception;
    Boolean share(SharePassRequest sharePassRequest) throws Exception;
    Boolean edit(EditRequest editPassRequest) throws Exception;
    Boolean delete(String passwordId, String userLogin );
    OnePasswordResponse fidPasswordById(String id, String login) throws Exception;

    Boolean restore(String changeId, String userLogin) throws JsonProcessingException;
}
