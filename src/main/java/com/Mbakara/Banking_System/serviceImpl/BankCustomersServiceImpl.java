package com.Mbakara.Banking_System.serviceImpl;

import com.Mbakara.Banking_System.config.JwtTokenProvider;
import com.Mbakara.Banking_System.dto.*;
import com.Mbakara.Banking_System.entity.BankCustomers;
import com.Mbakara.Banking_System.entity.Role;
import com.Mbakara.Banking_System.repository.BankCustomersRepository;
import com.Mbakara.Banking_System.service.BankCustomersService;
import com.Mbakara.Banking_System.service.EmailService;
import com.Mbakara.Banking_System.service.TransactionService;
import com.Mbakara.Banking_System.util.AccountUtils;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.math.BigInteger;

@Service
@AllArgsConstructor
public class BankCustomersServiceImpl implements BankCustomersService {

    @Autowired
    BankCustomersRepository createUserRepository;


    @Autowired
    TransactionService transactionService;


    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

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

        BankCustomers newUser = BankCustomers.builder()
                .firstName(bankUserRequestDTO.getFirstName())
                .lastName(bankUserRequestDTO.getLastName())
                .otherName(bankUserRequestDTO.getOtherName())
                .email(bankUserRequestDTO.getEmail())
                .password(passwordEncoder.encode(bankUserRequestDTO.getPassword()))
                .address(bankUserRequestDTO.getAddress())
                .gender(bankUserRequestDTO.getGender())
                .phoneNumber(bankUserRequestDTO.getPhoneNumber())
                .alternativePhoneNumber(bankUserRequestDTO.getAlternativePhoneNumber())
                .stateOfOrigin(bankUserRequestDTO.getStateOfOrigin())
                .accountNumber(AccountUtils.generateAccountNumber())
                .accountBalance(BigDecimal.ZERO)
                .status("ACTIVE")
                .role(Role.valueOf("ROLE_ADMIN"))
                .build();

        BankCustomers saveUser = createUserRepository.save(newUser);

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


    public UserBankResponseDTO loginUser(LoginDTO loginDTO){
        Authentication authentication = null;
        authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword())
        );

        EmailDetailsDTO loginAlert = EmailDetailsDTO.builder()
                .subject("You're logged in!")
                .recipient(loginDTO.getEmail())
                .messageBody("You logged into your account. If you did not initiate this request, Kindly contact your bank")
                .build();

        emailService.sendEmailAlert(loginAlert);
        return UserBankResponseDTO.builder()
                .responseCode("Login Successful")
                .responseMessage(jwtTokenProvider.generateToken(authentication))
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

        BankCustomers foundUser = createUserRepository.findByAccountNumber(requestDTO.getAccountNumber());
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

        BankCustomers foundUser = createUserRepository.findByAccountNumber(request.getAccountNumber());
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

        BankCustomers userToCredit = createUserRepository.findByAccountNumber(request.getAccountNumber());
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

        BankCustomers userToDebit = createUserRepository.findByAccountNumber(request.getAccountNumber());
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

//    @Override
//    public UserBankResponseDTO transferCash(TransferRequestDTO request) {
//        // Check if the destinationAccount Exist, since the sourceAccount is already existing
//        boolean isDestinationAccountExist = createUserRepository.existsByAccountNumber(request.getDestinationAccountNumber());
//
//        if (!isDestinationAccountExist){
//            return UserBankResponseDTO.builder()
//                    .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
//                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
//                    .accountInfo(null)
//                    .build();
//        }
//
//       CreateUser sourceAccountToDebit = createUserRepository.findByAccountNumber(request.getSourceAccountNumber());
//        // To debit the sourceAccount, do a check to see if the amount is more than the current balance
//        if (request.getAmount().compareTo(sourceAccountToDebit.getAccountBalance()) > 0){
//            return UserBankResponseDTO.builder()
//                    .responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
//                    .responseMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE)
//                    .accountInfo(null)
//                    .build();
//        }
//
//        //Perform the debit logic, and save the current User.
//        sourceAccountToDebit.setAccountBalance(sourceAccountToDebit.getAccountBalance().subtract(request.getAmount()));
//        String sourceUserName = sourceAccountToDebit.getFirstName() + " " + sourceAccountToDebit.getLastName()
//                + " " + sourceAccountToDebit.getOtherName();
//        createUserRepository.save(sourceAccountToDebit);
//
//        // Performing EmailAlert logic for debiting
//        try {  //new
//            EmailDetailsDTO debitAlert = EmailDetailsDTO.builder()
//                    //.recipient("DEBIT ALERT")
//                    .recipient(sourceAccountToDebit.getEmail())
//                    .subject("DEBIT ALERT")
//                    .messageBody("The Sum of " + request.getAmount() + " has been deducted from your account! Your current balance is "
//                            + sourceAccountToDebit.getAccountBalance())
//                    .build();
//            emailService.sendEmailAlert(debitAlert);
//
//    } catch (
//    MailException e) {
//        System.err.println("Failed to send email: " + e.getMessage());
//        throw new RuntimeException("Email sending failed. Please check SMTP settings.", e);
//    }  // new
//
//        //Credit the destinationAccount
//        CreateUser destinationAccountCredited = createUserRepository.findByAccountNumber(request.getDestinationAccountNumber());
//        destinationAccountCredited.setAccountBalance(destinationAccountCredited.getAccountBalance().add(request.getAmount()));
//        String recipientUserName = destinationAccountCredited.getFirstName() + " " + destinationAccountCredited.getLastName()
//                + " " + destinationAccountCredited.getOtherName();
//        createUserRepository.save(destinationAccountCredited);
//
//        // Performing EmailAlert logic for crediting
//        EmailDetailsDTO creditAlert = EmailDetailsDTO.builder()
//                .recipient(destinationAccountCredited.getEmail())
//                .subject("CREDIT ALERT")
//                .messageBody("The Sum of " + request.getAmount() + " has been sent to your account from " +  sourceUserName + " Your current balance is "
//                        + destinationAccountCredited.getAccountBalance())
//                .build();
//
//        emailService.sendEmailAlert(creditAlert);
//
//        TransactionsDTO customer3 = TransactionsDTO.builder()
//                .accountNumber(destinationAccountCredited.getAccountNumber())
//                .transactionType("DEBIT")
//                .amountInvolve(request.getAmount())
//                .build();
//
//        transactionService.saveTransaction(customer3);
//
//        return UserBankResponseDTO.builder()
//                .responseCode(AccountUtils.TRANSFER_SUCCESS_CODE)
//                .responseMessage(AccountUtils.TRANSFER_SUCCESS_MESSAGE)
//                .build();
//
//    }
//


//    @Transactional
//    public UserBankResponseDTO transferCash(TransferRequestDTO request) {
//        // Check if the destination account exists
//        CreateUser destinationAccountCredited = createUserRepository.findByAccountNumber(request.getDestinationAccountNumber());
//
//        if (destinationAccountCredited == null) {
//            return UserBankResponseDTO.builder()
//                    .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
//                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
//                    .accountInfo(null)
//                    .build();
//        }
//
//        // Fetch source account
//        CreateUser sourceAccountToDebit = createUserRepository.findByAccountNumber(request.getSourceAccountNumber());
//
//        // Check if the sourceAccount Exist
//        if (sourceAccountToDebit == null){
//            return UserBankResponseDTO.builder()
//                    .responseCode(AccountUtils.SOURCE_ACCOUNT_NOT_EXISTS_CODE)
//                    .responseMessage(AccountUtils.SOURCE_ACCOUNT_NOT_EXISTS_MESSAGE)
//                    .accountInfo(null)
//                    .build();
//        }
//
//
//        // Check for sufficient balance
//        if (request.getAmount().compareTo(sourceAccountToDebit.getAccountBalance()) > 0) {
//            return UserBankResponseDTO.builder()
//                    .responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
//                    .responseMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE)
//                    .accountInfo(null)
//                    .build();
//
//
//
//            // Get the Balance before Deduction
//            String beforeBalance = sourceAccountToDebit.getAccountBalance().toString();
//
//            // Deducting Balance from Source Account
//            sourceAccountToDebit.setAccountBalance(sourceAccountToDebit.getAccountBalance().subtract(request.getAmount()));
//
//
//            // Save the source account balance update
//            createUserRepository.save(sourceAccountToDebit);
//
//            // Get the Balance after Deduction
//            String afterBalance = sourceAccountToDebit.getAccountBalance().toString();
//
//            // Return the balance details in the response.
//            return UserBankResponseDTO.builder()
//                    .responseCode(AccountUtils.BALANCE_AFTER_DEDUCTION_CODE)
//                    .responseMessage(AccountUtils.BALANCE_AFTER_DEDUCTION_MESSAGE)
//                    .accountInfo(null)
//                    .build();
//        }
//
//        // Perform the debit logic and save
//        sourceAccountToDebit.setAccountBalance(sourceAccountToDebit.getAccountBalance().subtract(request.getAmount()));
//        createUserRepository.save(sourceAccountToDebit);
//
//        // Send debit alert email
//        try {
//            EmailDetailsDTO debitAlert = EmailDetailsDTO.builder()
//                    .recipient(sourceAccountToDebit.getEmail())
//                    .subject("DEBIT ALERT")
//                    .messageBody("The Sum of " + request.getAmount() + " has been deducted from your account! Your current balance is "
//                            + sourceAccountToDebit.getAccountBalance())
//                    .build();
//            emailService.sendEmailAlert(debitAlert);
//
//
//        } catch (MailException e) {
//            throw new RuntimeException("Email sending failed. Please check SMTP settings.", e);
//        }
//
//        // Perform the credit logic
//        destinationAccountCredited.setAccountBalance(destinationAccountCredited.getAccountBalance().add(request.getAmount()));
//        createUserRepository.save(destinationAccountCredited);
//
//        // Send credit alert email
//        EmailDetailsDTO creditAlert = EmailDetailsDTO.builder()
//                .recipient(destinationAccountCredited.getEmail())
//                .subject("CREDIT ALERT")
//                .messageBody("The Sum of " + request.getAmount() + " has been sent to your account from " +
//                        sourceAccountToDebit.getFirstName() + " " + sourceAccountToDebit.getLastName() +
//                        " Your current balance is " + destinationAccountCredited.getAccountBalance())
//                .build();
//
//        emailService.sendEmailAlert(creditAlert);
//
//        // Save transaction history
//        TransactionsDTO transactionRecord = TransactionsDTO.builder()
//                .accountNumber(destinationAccountCredited.getAccountNumber())
//                .transactionType("CREDIT")  // Corrected transaction type
//                .amountInvolve(request.getAmount())
//                .build();
//
//        transactionService.saveTransaction(transactionRecord);
//
//        return UserBankResponseDTO.builder()
//                .responseCode(AccountUtils.TRANSFER_SUCCESS_CODE)
//                .responseMessage(AccountUtils.TRANSFER_SUCCESS_MESSAGE)
//                .build();
//    }

    @Transactional
    public UserBankResponseDTO transferCash(TransferRequestDTO request) {

        // Check if destination account exists
        BankCustomers destinationAccount = createUserRepository.findByAccountNumber(request.getDestinationAccountNumber());
        if (destinationAccount == null) {
            return UserBankResponseDTO.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        // Check if source account exists
        BankCustomers sourceAccount = createUserRepository.findByAccountNumber(request.getSourceAccountNumber());
        if (sourceAccount == null) {
            return UserBankResponseDTO.builder()
                    .responseCode(AccountUtils.SOURCE_ACCOUNT_NOT_EXISTS_CODE)  // Fixed incorrect response code
                    .responseMessage(AccountUtils.SOURCE_ACCOUNT_NOT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        //  Check for sufficient balance
        if (request.getAmount().compareTo(sourceAccount.getAccountBalance()) > 0) {
            return UserBankResponseDTO.builder()
                    .responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
                    .responseMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        // Save balance before and after deduction
        String beforeBalance = sourceAccount.getAccountBalance().toString();

        // Deduct balance from source account
        sourceAccount.setAccountBalance(sourceAccount.getAccountBalance().subtract(request.getAmount()));
        createUserRepository.save(sourceAccount);

        String afterBalance = sourceAccount.getAccountBalance().toString();  // Balance after deduction

        // Perform credit logic
        destinationAccount.setAccountBalance(destinationAccount.getAccountBalance().add(request.getAmount()));
        createUserRepository.save(destinationAccount);

        // Send debit alert email (but don't fail transaction if email fails)
        try {
            emailService.sendEmailAlert(EmailDetailsDTO.builder()
                    .recipient(sourceAccount.getEmail())
                    .subject("DEBIT ALERT")
                    .messageBody("The sum of " + request.getAmount() + " has been deducted from your account. Your current balance is " + sourceAccount.getAccountBalance())
                    .build());
        } catch (MailException e) {
            System.err.println("Failed to send debit email. Continuing transaction...");
        }

        //  Send credit alert email
        try {
            emailService.sendEmailAlert(EmailDetailsDTO.builder()
                    .recipient(destinationAccount.getEmail())
                    .subject("CREDIT ALERT")
                    .messageBody("The sum of " + request.getAmount() + " has been credited to your account from " +
                            sourceAccount.getFirstName() + " " + sourceAccount.getLastName() +
                            ". Your current balance is " + destinationAccount.getAccountBalance())
                    .build());
        } catch (MailException e) {
            System.err.println("Failed to send credit email. Continuing transaction...");
        }

        // Save transaction history
        transactionService.saveTransaction(TransactionsDTO.builder()
                .accountNumber(destinationAccount.getAccountNumber())
                .transactionType("CREDIT")
                .amountInvolve(request.getAmount())
                .build());

        transactionService.saveTransaction(TransactionsDTO.builder()
                .accountNumber(sourceAccount.getAccountNumber())
                .transactionType("DEBIT")
                .amountInvolve(request.getAmount())
                .build());

        // Return success response with balance details
        return UserBankResponseDTO.builder()
                .responseCode(AccountUtils.TRANSFER_SUCCESS_CODE)
                .responseMessage(AccountUtils.TRANSFER_SUCCESS_MESSAGE)
                .accountInfo(null)
                .build();
    }



}


