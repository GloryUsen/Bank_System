package com.Mbakara.Banking_System.controller;

import com.Mbakara.Banking_System.dto.*;
import com.Mbakara.Banking_System.service.BankCustomersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
    public UserBankResponseDTO loginUser(@RequestBody LoginDTO loginDTO){
        return bankCustomersService.loginUser(loginDTO);
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
        return bankCustomersService.balanceEnquiry(request);

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
        return bankCustomersService.nameEnquiry(request);
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
        return bankCustomersService.creditAccount(request);
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
        return bankCustomersService.debitAccount(request);
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
        return bankCustomersService.transferCash(request);
    }

    @PostMapping("/delete")
    public ResponseEntity<AccountDeletionResponseDTO> deleteAccount(AccountDeletionRequestDTO request){
        AccountDeletionResponseDTO response = bankCustomersService.deleteAccount(request);
        return ResponseEntity.ok(response);
        // Why is this endpoint not like the rest here?
    }



}
