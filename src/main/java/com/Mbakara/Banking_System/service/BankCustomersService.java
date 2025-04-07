package com.Mbakara.Banking_System.service;

import com.Mbakara.Banking_System.dto.*;
import org.springframework.stereotype.Service;

@Service
public interface BankCustomersService {

    UserBankResponseDTO creatAccount(BankUserRequestDTO bankUserRequestDTO);


    // Balance Enquiry gives information about the user.
    AccountInfo balanceEnquiry(String accountNumber);
    AccountInfo nameEnquiry(String accountNumber);
    UserBankResponseDTO creditAccount(CreditDebitRequestDTO request);
    UserBankResponseDTO debitAccount(CreditDebitRequestDTO request);
    UserBankResponseDTO transferCash(TransferRequestDTO request);
    LoginResponse loginUser(LoginDTO request);
    AccountDeletionResponseDTO deleteAccount(AccountDeletionRequestDTO request);



}
