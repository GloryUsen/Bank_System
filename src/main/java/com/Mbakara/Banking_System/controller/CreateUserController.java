package com.Mbakara.Banking_System.controller;

import com.Mbakara.Banking_System.dto.*;
import com.Mbakara.Banking_System.service.CreateUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/user")
@Tag(name = "Users Account Management APIs")
public class CreateUserController {

    @Autowired
     CreateUserService createUserService;

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
        return createUserService.creatAccount(request);

    }

    @PostMapping("/login")
    public UserBankResponseDTO loginUser(@RequestBody LoginDTO loginDTO){
        return createUserService.loginUser(loginDTO);
    }

    @Operation(
            summary = "Balance Enquiry",
            description = "Given An Account Number, Check How Much The User Has"

    )

    @ApiResponse(
            responseCode = "200",
            description = "Http status 200 SUCCESS"

    )

    @GetMapping("/balanceEnquiry")
    public UserBankResponseDTO balanceEnquiry(@RequestBody CustomerEnquiryRequestDTO request){
        return createUserService.balanceEnquiry(request);

    }

    @Operation(
            summary = "Name Enquiry",
            description = "Confirm a User's name before Transaction"
    )

    @ApiResponse(
            responseCode = "200",
            description = "Http status 200 SUCCESS"
    )

    @GetMapping("/nameEnquiry")
    public String nameEnquiry(@RequestBody CustomerEnquiryRequestDTO request){
        return createUserService.nameEnquiry(request);
    }

    @Operation(
            summary = "Credit An Account",
            description = "Creating A User Account After Deposit"
    )

    @ApiResponse(
            responseCode = "201",
            description = "Http status 201 Account Credited"
    )

    @PostMapping("credit")
    public UserBankResponseDTO creditAccount(@RequestBody CreditDebitRequestDTO request){
        return createUserService.creditAccount(request);
    }

    @Operation(
            summary = "Debiting An Account",
            description = "Deduction Of Money From The User's Account"
    )

    @ApiResponse(
            responseCode = "200",
            description = "Http status 200 Account Debited"
    )

    @PostMapping("debit")
    public UserBankResponseDTO debitAccount(@RequestBody CreditDebitRequestDTO request){
        return createUserService.debitAccount(request);
    }

    @Operation(
            summary = "Transferring Fund",
            description = "Moving Funds From One Account To Another"
    )

    @ApiResponse(
            responseCode = "200",
            description = "Http status 200 Transfer Successful"
    )

    @PostMapping("transfer")
    public UserBankResponseDTO transferCash(@RequestBody TransferRequestDTO request){
        return createUserService.transferCash(request);
    }



}
