package com.varkaikin.passwordwallet.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DataChangeResponse {

    private String id;
    private String previousValueOfRecord ;
    private String presentValueOfRecord ;
    private String time;
    private String actionType;

}
