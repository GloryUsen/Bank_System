package com.Mbakara.Banking_System.util;

import java.time.Year;

public class AccountUtils {

    //  Also creating Custom messages.
    public static final String ACCOUNT_EXIST_CODE = "001";
    public static final String ACCOUNT_EXIST_MESSAGE = "User already Exist";
    public static final String ACCOUNT_CREATION_SUCCESS_CODE = "002";
    public static final String ACCOUNT_CREATION_SUCCESS_MESSAGE = "Account has been successfully created";


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
