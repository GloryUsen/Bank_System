package com.Mbakara.Banking_System.util;

import java.time.Year;

public class AccountUtils {

    //  Also creating Custom messages.
    public static final String ACCOUNT_EXIST_CODE = "001";
    public static final String ACCOUNT_EXIST_MESSAGE = "User already Exist";
    public static final String ACCOUNT_CREATION_SUCCESS_CODE = "002";
    public static final String ACCOUNT_CREATION_SUCCESS_MESSAGE = "Account has been successfully created";
    public static final String ACCOUNT_NOT_EXIST_CODE = "003";
    public static final String ACCOUNT_NOT_EXIST_MESSAGE = "User with the provided account Number does not Exist";
    public static final String ACCOUNT_FOUND_CODE = "004";
    public static final String ACCOUNT_FOUND_SUCCESS = "User Account Found";
    public static final String ACCOUNT_CREDITED_SUCCESS_CODE = " 005 ";
    public static final String ACCOUNT_CREDITED_SUCCESS_MESSAGE = " User Account Was credited Successfully";
    public static final String INSUFFICIENT_BALANCE_CODE = "006";
    public static final String INSUFFICIENT_BALANCE_MESSAGE = "Insufficient Balance";
    public static final String ACCOUNT_DEBITED_SUCCESS_CODE = "007";
    public static final String ACCOUNT_DEBITED_SUCCESS_MESSAGE = " Account Has Been Successfully Debited";
    public static final String TRANSFER_SUCCESS_CODE = "008";
    public static final String TRANSFER_SUCCESS_MESSAGE = " Transfer Successful";
    public static final String SOURCE_ACCOUNT_NOT_EXISTS_CODE = "009";
    public static final String SOURCE_ACCOUNT_NOT_EXISTS_MESSAGE = "Sender's Account Not Found";
    public static final String LOAN_ALREADY_EXISTS_CODE = "010";
    public static final String LOAN_ALREADY_EXISTS_MESSAGE = "Oops! You already have an active loan. Clear loan before applying for a new one. Need help? Contact our support team!";
    public static final String INVALID_LOAN_AMOUNT_CODE = "011";
    public static final String INVALID_LOAN_AMOUNT_MESSAGE = "Invalid loan request! Please enter a valid loan amount. We're here to help you achieve your financial goals!";
    public static final String LOAN_APPROVAL_CODE = "012";
    public static final String LOAN_APPROVAL_MESSAGE = "Congratulations! Your loan has been approved";
    public static final String NO_ACTIVE_LOAN_CODE = "013";
    public static final String NO_ACTIVE_LOAN_MESSAGE = "You currently have no active loan loan linked to your account. If you need any financial support, feel free to apply for a loan today and take control of your finances!";
    public static final String INVALID_REPAYMENT_AMOUNT_CODE = "014";
    public static final String INVALID_REPAYMENT_AMOUNT_MESSAGE = "Invalid repayment amount! Please enter a valid amount between 1 and your outstanding loan balance. Need assistance? Contact our support team!";
    public static final String LOAN_REPAID_SUCCESS_CODE = "015";
    public static final String LOAN_REPAID_SUCCESS_MESSAGE = "Payment successful! You have successfully repaid your loan";





    public static String generateAccountNumber() {

        /** 2023 + randomSixDigits.
         *
         */

        Year currentYear = Year.now();
        int min = 100000;
        int max = 999999;

        // Generate a random number between min and max.
        int randNumber = (int) Math.floor(Math.random() * (max - min + 1) + min);


        /** Convert the current and randomNumber to a String, then concatenate them together
         */

        String year = String.valueOf(currentYear);
        String randomNumber = String.valueOf(randNumber);
        StringBuilder accountNumber = new StringBuilder();

         return accountNumber.append(year).append(randomNumber).toString();
    }

}
