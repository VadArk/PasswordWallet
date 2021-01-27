package com.varkaikin.passwordwallet.response;

import java.util.List;

public class JwtResponse {

    private String token;
    private String type = "Bearer";

    private String username;
    private List<String> roles;

    public JwtResponse(String accessToken, String username,  List<String> roles) {
        this.token = accessToken;

        this.username = username;
        this.roles = roles;
    }
}
