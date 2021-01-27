package com.varkaikin.passwordwallet.dto;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class LoginResponse {

    String status;
    LocalDateTime lastSuccess;
    LocalDateTime lastUnsuccessful;

}
