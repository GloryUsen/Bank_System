package com.Mbakara.Banking_System.controller;

import com.Mbakara.Banking_System.dto.*;
import com.Mbakara.Banking_System.service.BankCustomersService;
import com.Mbakara.Banking_System.util.AccountUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/user")
@Tag(name = "Users Account Management APIs")
public class CustomersController {

    @Autowired
    BankCustomersService bankCustomersService;

    @Qualifier("bankCustomersService")

//    @Autowired
//    CreditDebitRequestDTO creditDebitRequestDTO;

    @Operation(
          summary = "Create New User Account",
            description = "Creating A New User And Assigning An Account ID"

    )

    @ApiResponse(
            responseCode = "201",
            description = "Http status 201 CREATED"

    )

    @PostMapping("/createAccount")
    public UserBankResponseDTO createAccount(@RequestBody BankUserRequestDTO request){
        //System.out.println("hello world");
        return bankCustomersService.creatAccount(request);

    }

    @PostMapping("/login")
    public ResponseEntity<APIResponse> loginUser(@RequestBody LoginDTO loginDTO){
        LoginResponse loginResponse = bankCustomersService.loginUser(loginDTO);
        return ResponseEntity.ok(new APIResponse(AccountUtils.OPERATION_SUCCESS_CODE,  "Login Successful", true, loginResponse));
    }

    @Operation(
            summary = "Balance Enquiry",
            description = "Given An Account Number, Check How Much The User Has"

    )

    @ApiResponse(
            responseCode = "200",
            description = "Http status 200 SUCCESS"

    )

    @GetMapping("/balanceEnquiry/{accountNumber}")
    public ResponseEntity<APIResponse> balanceEnquiry(@PathVariable String accountNumber){
        AccountInfo info = bankCustomersService.balanceEnquiry(accountNumber);
        return ResponseEntity.ok(new APIResponse(AccountUtils.OPERATION_SUCCESS_CODE, "Account Info Retrieved Successful", true, info));

    }

//    @Operation(
//            summary = "Name Enquiry",
//            description = "Confirm a User's name before Transaction"
//    )
//
//    @ApiResponse(
//            responseCode = "200",
//            description = "Http status 200 SUCCESS"
//    )

//    @GetMapping("/nameEnquiry/{accountNumber}")
//    public String nameEnquiry(@PathVariable String accountNumber){
//        return bankCustomersService.nameEnquiry(accountNumber);
//    }

    @GetMapping("/name_Enquiry{accountNumber}")
    public ResponseEntity<APIResponse> nameEnquiry(@PathVariable String accountNumber){
        AccountInfo response = bankCustomersService.nameEnquiry(accountNumber);
        return ResponseEntity.ok(new APIResponse(AccountUtils.OPERATION_SUCCESS_CODE, "Account Info Retrieved Successful", true, response));
    }




}
