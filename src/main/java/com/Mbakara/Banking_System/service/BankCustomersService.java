package com.Mbakara.Banking_System.service;

import com.Mbakara.Banking_System.dto.*;
import org.springframework.stereotype.Service;

@Service
public interface BankCustomersService {

    UserBankResponseDTO creatAccount(BankUserRequestDTO bankUserRequestDTO);

    // Balance Enquiry gives information about the user.
    UserBankResponseDTO balanceEnquiry(CustomerEnquiryRequestDTO customerEnquiryRequestDTO);
    String nameEnquiry(CustomerEnquiryRequestDTO request);
    UserBankResponseDTO creditAccount(CreditDebitRequestDTO request);
    UserBankResponseDTO debitAccount(CreditDebitRequestDTO request);
    UserBankResponseDTO transferCash(TransferRequestDTO request);
    UserBankResponseDTO loginUser(LoginDTO request);

}
