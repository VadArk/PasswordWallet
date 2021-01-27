package com.varkaikin.passwordwallet.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserDTORegister {

    private String login;
    private String password;
    private Boolean isPasswordSavedAsHash;
}
