package com.varkaikin.passwordwallet.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShowPasswordRequest {

    private String passwordId;
    private String masterPassword;
}
