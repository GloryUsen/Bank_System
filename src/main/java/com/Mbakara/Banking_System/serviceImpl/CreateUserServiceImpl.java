package com.Mbakara.Banking_System.serviceImpl;

import com.Mbakara.Banking_System.dto.AccountInfo;
import com.Mbakara.Banking_System.dto.BankUserRequestDTO;
import com.Mbakara.Banking_System.dto.EmailDetailsDTO;
import com.Mbakara.Banking_System.dto.UserBankResponseDTO;
import com.Mbakara.Banking_System.entity.CreateUser;
import com.Mbakara.Banking_System.repository.CreateUserRepository;
import com.Mbakara.Banking_System.service.CreateUserService;
import com.Mbakara.Banking_System.service.EmailService;
import com.Mbakara.Banking_System.util.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CreateUserServiceImpl implements CreateUserService {

    @Autowired
    CreateUserRepository bankUserRepository;

    // Will need the mailService sender, here for the user, so it has to be autowired below.
    @Autowired
    EmailService emailService;

    @Override
    public UserBankResponseDTO creatAccount(BankUserRequestDTO bankUserRequestDTO) {
        /** In creating an account, you want to save a new User to your database(So the process of
         creating a new user is as well as instantiating an object of a new user).
         */

        // This first Step is to check if that current user you are about to create an account for actually exist

        if (bankUserRepository.existsByEmail(bankUserRequestDTO.getEmail())) { /** Returning a custom message, if the current userExistByEmail,
         (i.e, if the email exist, you return the Custom errorCode/Message.
         **/
        return UserBankResponseDTO.builder()
                    .responseCode(AccountUtils.ACCOUNT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_EXIST_MESSAGE)
                   .accountInfo(null) // This means the user doesn't have an account with the bank.
                   .build();

        }

        /**
          But if the user doesn't exist, you create a new object of a user(That's creating an acct for the new user).
         * Filling in the detail of the new user by accepting any details provided by the user.
         * Save the user to the db by using userRepo.
         * Then return a custom success message,

         */

        CreateUser newUser = CreateUser.builder()
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

        CreateUser saveUser = bankUserRepository.save(newUser);

        /** After creating a newUer and saving the user,
         * I'm calling the method of emailSender Alert.
         * Also creating an object of email details below too.
         */

        EmailDetailsDTO emailDetailsDTO = EmailDetailsDTO.builder()
               // .recipient(saveUser.getEmail())// The recipient of the email is going to be the saved User.getEmail(Who the email is sent to).
               // .recipient("gloryanwana39@gmail.com")
                .recipient(bankUserRequestDTO.getEmail())

                .subject("ACCOUNT CREATION")
                .messageBody("Congratulations! Your Account Has Been Successfully Created." +
                        "\n Account Details: \n Account Name: " + bankUserRequestDTO.getFirstName() + " "
                + bankUserRequestDTO.getLastName() + " " + bankUserRequestDTO.getOtherName() + " \nAccount Number: "
                        + saveUser.getAccountNumber())
                .build();

      //  emailService.sendEmailAlert(new EmailDetailsDTO());
        emailService.sendEmailAlert(emailDetailsDTO);


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

    @Override
    public UserBankResponseDTO balanceEnquiry(BankUserRequestDTO bankUserRequestDTO) {
        return null;
    }

//    @Override
//    public UserBankResponseDTO registerUser(BankUserRequestDTO bankUserRequestDTO) {
//        return null;
//    }
}
