package com.varkaikin.passwordwallet.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeUserPasswordRequest {

    private String login;
    private String oldPassword;
    private String newPassword;
    private Boolean isPasswordSavedAsHash;

}
