package com.varkaikin.passwordwallet.service;

import com.varkaikin.passwordwallet.dto.DataChangeResponse;
import com.varkaikin.passwordwallet.model.DataChange;

import java.util.List;

public interface DataChangeService {

    List<DataChangeResponse> findAllByUserLoginAndPasswordId(String login, String passwordId);
    DataChange findById (String id);
}
