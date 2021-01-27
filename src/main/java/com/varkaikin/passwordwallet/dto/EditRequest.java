package com.varkaikin.passwordwallet.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditRequest {

    private String passwordId;
    private String userLogin;
    private String newPassword;
    private String newLogin;
    private String newWeb;
    private String newDescription;


}
