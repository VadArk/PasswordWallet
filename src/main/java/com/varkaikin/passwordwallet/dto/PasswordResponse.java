package com.varkaikin.passwordwallet.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PasswordResponse {

    private List<PasswordsContent> content;
    private String totalPages;
    private String totalElements;
    private String number;


}
