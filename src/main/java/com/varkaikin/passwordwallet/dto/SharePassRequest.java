package com.varkaikin.passwordwallet.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SharePassRequest {

    private String userLogin;
    private String passwordId;
    private String sharedLogin;


}
