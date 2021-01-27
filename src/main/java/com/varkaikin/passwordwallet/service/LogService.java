package com.varkaikin.passwordwallet.service;

import com.varkaikin.passwordwallet.model.Log;

public interface LogService {


    Integer getLoginStatus(String login, Boolean result, String remoteAddr);
    Log findLastSuccessful(String ipAddress, String login);
    Log findLastUnSuccessful(String ipAddress, String login);

    Log resetIp(String remoteAddr);
}
