package com.Mbakara.Banking_System.serviceImpl;

import com.Mbakara.Banking_System.dto.*;
import com.Mbakara.Banking_System.entity.CreateUser;
import com.Mbakara.Banking_System.repository.CreateUserRepository;
import com.Mbakara.Banking_System.service.CreateUserService;
import com.Mbakara.Banking_System.service.EmailService;
import com.Mbakara.Banking_System.service.TransactionService;
import com.Mbakara.Banking_System.util.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;

@Component
public class CreateUserServiceImpl implements CreateUserService {

    @Autowired
    CreateUserRepository createUserRepository;


    @Autowired
    TransactionService transactionService;

    // Will need the mailService sender, here for the user, so it has to be autowired below.
    @Autowired
    EmailService emailService;

    @Override
    public UserBankResponseDTO creatAccount(BankUserRequestDTO bankUserRequestDTO) {
        /** In creating an account, you want to save a new User to your database(So the process of
         creating a new user is as well as instantiating an object of a new user).
         */

        // This first Step is to check if that current user you are about to create an account for actually exist

        if (createUserRepository.existsByEmail(bankUserRequestDTO.getEmail())) { /** Returning a custom message, if the current userExistByEmail,
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

        CreateUser saveUser = createUserRepository.save(newUser);

        /** After creating a newUer and saving the user,
         * I'm calling the method of emailSender Alert. So to send emailAlert
         * Also creating an object of email details below too.
         */

        EmailDetailsDTO emailDetailsDTO = EmailDetailsDTO.builder()
               // .recipient(saveUser.getEmail())// The recipient of the email is going to be the saved User.getEmail(Who the email is sent to).
               // .recipient("gloryanwana39@gmail.com")
                //.recipient(bankUserRequestDTO.getEmail())
                .recipient(saveUser.getEmail())

                .subject("ACCOUNT CREATION")
                .messageBody("Congratulations! Your Account Has Been Successfully Created." +
                        "\n Account Details: \n Account Name: " + saveUser.getFirstName() + " "
                + saveUser.getLastName() + " " + saveUser.getOtherName() + " \nAccount Number: "
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
    public UserBankResponseDTO balanceEnquiry(CustomerEnquiryRequestDTO requestDTO) {
        Boolean isAccountExist = createUserRepository.existsByAccountNumber(requestDTO.getAccountNumber());
        if (!isAccountExist){
            return UserBankResponseDTO.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        CreateUser foundUser = createUserRepository.findByAccountNumber(requestDTO.getAccountNumber());
        return UserBankResponseDTO.builder()
                .responseCode(AccountUtils.ACCOUNT_FOUND_CODE)
                .responseMessage(AccountUtils.ACCOUNT_FOUND_SUCCESS)
                .accountInfo(AccountInfo.builder() // Trying to build acctEnquiry, so we return some few inform about the acct.
                        .accountBalance(foundUser.getAccountBalance()) // This account balance will be getting from the found User.
                        .accountNumber(requestDTO.getAccountNumber()) // Use same accountNo provided from line 114.
                        .accountName(foundUser.getFirstName() + " " + foundUser.getLastName()+ " "+ foundUser.getOtherName())
                        .build())
                .build();
    }
    @Override
    public String nameEnquiry(CustomerEnquiryRequestDTO request) {
       boolean isAccountExist = createUserRepository.existsByAccountNumber(request.getAccountNumber());
       if (!isAccountExist){
           return AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE;
       }

        CreateUser foundUser = createUserRepository.findByAccountNumber(request.getAccountNumber());
       return foundUser.getFirstName() + " " + foundUser.getLastName() + " " + foundUser.getOtherName();
    }

    @Override
    public UserBankResponseDTO creditAccount(CreditDebitRequestDTO request) {
        boolean isAccountExist = createUserRepository.existsByAccountNumber(request.getAccountNumber());
        if (!isAccountExist){
            return UserBankResponseDTO.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        /** If the above account doesn't exist, you retrieve the correct user from the db, so the retrieved account will be from the CreditDTO
         *  request, which will give the particular accountNo to a specific user.
         *  Next thing will be to update the information of the user.
         */

        CreateUser userToCredit = createUserRepository.findByAccountNumber(request.getAccountNumber());
        userToCredit.setAccountBalance(userToCredit.getAccountBalance().add(request.getAmount()));
        createUserRepository.save(userToCredit);

        //Building an Object of Transaction to save each transaction
        TransactionsDTO customer1 = TransactionsDTO.builder()
                .accountNumber(userToCredit.getAccountNumber())// The acctNo will be the userAcct to credit.
                .transactionType("CREDIT")
                .amountInvolve(request.getAmount())// AMount will be gotten from the request
                .build();

        transactionService.saveTransaction(customer1);

        return UserBankResponseDTO.builder()
                .responseCode(AccountUtils.ACCOUNT_CREDITED_SUCCESS_CODE)
                .responseMessage(AccountUtils.ACCOUNT_CREDITED_SUCCESS_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountName(userToCredit.getFirstName() + " " + userToCredit.getLastName() + " " + userToCredit.getOtherName())
                        .accountBalance(userToCredit.getAccountBalance())
                        .accountNumber(request.getAccountNumber())
                        .build())
                .build();
    }

    @Override
    public UserBankResponseDTO debitAccount(CreditDebitRequestDTO request) {
        /** To debit an account,
              .Check if the account exist
                Check if the intended debited amount is not more than the current balance
         */

        boolean isAccountExist = createUserRepository.existsByAccountNumber(request.getAccountNumber());
        if (!isAccountExist){
            return UserBankResponseDTO.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        CreateUser userToDebit = createUserRepository.findByAccountNumber(request.getAccountNumber());
        BigInteger availableBalance = userToDebit.getAccountBalance().toBigInteger();
        BigInteger debitAmount = request.getAmount().toBigInteger();
        if (availableBalance.intValue() < debitAmount.intValue()){
            return UserBankResponseDTO.builder()
                    .responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
                    .responseMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE)
                    .accountInfo(null)
                    .build();
        }



        else {
            userToDebit.setAccountBalance(userToDebit.getAccountBalance().subtract(request.getAmount()));
            createUserRepository.save(userToDebit);

            TransactionsDTO customer2 = TransactionsDTO.builder()
                    .accountNumber(userToDebit.getAccountNumber())
                    .transactionType("DEBIT")
                    .amountInvolve(request.getAmount())
                    .build();

            transactionService.saveTransaction(customer2);
            return UserBankResponseDTO.builder()
                    .responseCode(AccountUtils.ACCOUNT_DEBITED_SUCCESS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_DEBITED_SUCCESS_MESSAGE)
                    .accountInfo(AccountInfo.builder()
                            .accountNumber(request.getAccountNumber())
                            .accountName(userToDebit.getFirstName()+ " " + userToDebit.getLastName()+ " " +
                                    userToDebit.getOtherName())
                            .accountBalance(userToDebit.getAccountBalance())
                            .build())
                    .build();
        }


    }

    @Override
    public UserBankResponseDTO transferCash(TransferRequestDTO request) {
        // Check if the destinationAccount Exist, since the sourceAccount is already existing
        boolean isDestinationAccountExist = createUserRepository.existsByAccountNumber(request.getDestinationAccountNumber());

        if (!isDestinationAccountExist){
            return UserBankResponseDTO.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

       CreateUser sourceAccountToDebit = createUserRepository.findByAccountNumber(request.getSourceAccountNumber());
        // To debit the sourceAccount, do a check to see if the amount is more than the current balance
        if (request.getAmount().compareTo(sourceAccountToDebit.getAccountBalance()) > 0){
            return UserBankResponseDTO.builder()
                    .responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
                    .responseMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        //Perform the debit logic, and save the current User.
        sourceAccountToDebit.setAccountBalance(sourceAccountToDebit.getAccountBalance().subtract(request.getAmount()));
        String sourceUserName = sourceAccountToDebit.getFirstName() + " " + sourceAccountToDebit.getLastName()
                + " " + sourceAccountToDebit.getOtherName();
        createUserRepository.save(sourceAccountToDebit);

        // Performing EmailAlert logic for debiting
        EmailDetailsDTO debitAlert = EmailDetailsDTO.builder()
                .recipient("DEBIT ALERT ")
                .subject(sourceAccountToDebit.getEmail())
                .messageBody("The Sum of " + request.getAmount() + " has been deducted from your account! Your current balance is"
                        + sourceAccountToDebit.getAccountBalance())
                .build();
        emailService.sendEmailAlert(debitAlert);

        //Credit the destinationAccount
        CreateUser destinationAccountCredited = createUserRepository.findByAccountNumber(request.getDestinationAccountNumber());
//        destinationAccountCredited.setAccountBalance(destinationAccountCredited.getAccountBalance().add(request.getAmount()));
        String recipientUserName = destinationAccountCredited.getFirstName() + " " + destinationAccountCredited.getLastName()
                + " " + destinationAccountCredited.getOtherName();
        createUserRepository.save(destinationAccountCredited);

        // Performing EmailAlert logic for crediting
        EmailDetailsDTO creditAlert = EmailDetailsDTO.builder()
                .recipient("CREDIT ALERT")
                .subject(destinationAccountCredited.getEmail())
                .messageBody("The Sum of " + request.getAmount() + " has been sent to your account from" +  sourceUserName + " Your current balance is "
                        + destinationAccountCredited.getAccountBalance())
                .build();

        emailService.sendEmailAlert(creditAlert);

        TransactionsDTO customer3 = TransactionsDTO.builder()
                .accountNumber(destinationAccountCredited.getAccountNumber())
                .transactionType("DEBIT")
                .amountInvolve(request.getAmount())
                .build();

        transactionService.saveTransaction(customer3);

        return UserBankResponseDTO.builder()
                .responseCode(AccountUtils.TRANSFER_SUCCESS_CODE)
                .responseMessage(AccountUtils.TRANSFER_SUCCESS_MESSAGE)
                .build();

    }


}
