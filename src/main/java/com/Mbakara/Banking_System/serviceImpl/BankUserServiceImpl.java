package com.Mbakara.Banking_System.serviceImpl;

import com.Mbakara.Banking_System.dto.AccountInfo;
import com.Mbakara.Banking_System.dto.BankUserRequestDTO;
import com.Mbakara.Banking_System.dto.UserBankResponseDTO;
import com.Mbakara.Banking_System.entity.BankUser;
import com.Mbakara.Banking_System.repository.BankUserRepository;
import com.Mbakara.Banking_System.service.BankUserService;
import com.Mbakara.Banking_System.util.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class BankUserServiceImpl implements BankUserService {

    @Autowired
    BankUserRepository bankUserRepository;

    @Override
    public UserBankResponseDTO creatAccount(BankUserRequestDTO bankUserRequestDTO) {
        /** In creating an account, you want to save a new User to your database(So the process of
         creating a new user is as well as instantiating an object of a new user).
         */

        if (bankUserRepository.existsByEmail(bankUserRequestDTO.getEmail())) { // if the userExistByEmail, get any email the user provides.

            // So if a user exist, you use the DTO response to build a response message from the AccountUtils.

           return UserBankResponseDTO.builder()
                    .responseCode(AccountUtils.ACCOUNT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_EXIST_MESSAGE)
                   .accountInfo(null) // This means the user doesn't have an account with the bank.
                   .build();

        }


        BankUser newUser = BankUser.builder()
                .firstName(bankUserRequestDTO.getFirstName())
                .lastName(bankUserRequestDTO.getLastName())
                .otherName(bankUserRequestDTO.getOtherName())
                .email(bankUserRequestDTO.getEmail())
                .address(bankUserRequestDTO.getAddress())
                .gender(bankUserRequestDTO.getGender())
                .phoneNumber(bankUserRequestDTO.getPhoneNumber())
                .alternativePhoneNumber(bankUserRequestDTO.getAlternativePhoneNumber())
                .stateOfOrigin(bankUserRequestDTO.getStateOfOrigin())
                .accountNumber(AccountUtils.generateAccountNumber())
                .accountBalance(BigDecimal.ZERO)
                .status("ACTIVE")
                .build();

        BankUser saveUser = bankUserRepository.save(newUser);
        return UserBankResponseDTO.builder()
                .responseCode(AccountUtils.ACCOUNT_CREATION_SUCCESS_CODE)
                .responseMessage(AccountUtils.ACCOUNT_CREATION_SUCCESS_MESSAGE)

                // Building an account Information for the user, so you build an object of the account info.
                .accountInfo(AccountInfo.builder()
                        .accountBalance(saveUser.getAccountBalance())
                        .accountNumber(saveUser.getAccountNumber())
                        .accountName(saveUser.getFirstName() + saveUser.getLastName() + saveUser.getOtherName())
                        .build())
                .build();

    }
}
