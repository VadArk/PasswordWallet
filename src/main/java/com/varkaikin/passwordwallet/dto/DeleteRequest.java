package com.varkaikin.passwordwallet.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteRequest {

    private String passwordId;
    private String userLogin;


}
