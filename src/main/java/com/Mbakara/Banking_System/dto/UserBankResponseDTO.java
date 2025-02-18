package com.Mbakara.Banking_System.dto;

import lombok.Builder;

@Builder


public class UserBankResponseDTO {

    private String responseCode;
    private String responseMessage;
    private AccountInfo accountInfo;
}
