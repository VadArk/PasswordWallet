package com.varkaikin.passwordwallet.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordsContent {

    private String id;
    private String login;
    private String password;
    private String web_address;
    private String description;
    private String ownerId;

}
