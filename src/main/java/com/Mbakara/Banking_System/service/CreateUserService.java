package com.Mbakara.Banking_System.service;

import com.Mbakara.Banking_System.dto.BankUserRequestDTO;
import com.Mbakara.Banking_System.dto.UserBankResponseDTO;

public interface CreateUserService {

    UserBankResponseDTO creatAccount(BankUserRequestDTO bankUserRequestDTO);
    UserBankResponseDTO balanceEnquiry(BankUserRequestDTO bankUserRequestDTO);
}
