package com.Mbakara.Banking_System.controller;

import com.Mbakara.Banking_System.dto.*;
import com.Mbakara.Banking_System.entity.CustomerTransactions;
import com.Mbakara.Banking_System.service.BankCustomersService;
import com.Mbakara.Banking_System.service.BankStatementService;
import com.itextpdf.text.DocumentException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/bankStatement")

public class BankTransactionController {

    private final BankStatementService bankStatementService;

    @Autowired
    BankCustomersService bankCustomersService;


    @GetMapping()
    public List<CustomerTransactions> generateAccountStatement(@RequestParam String accountNumber,
                                                               @RequestParam String startDate,
                                                               @RequestParam String endDate) throws DocumentException, FileNotFoundException {
        return bankStatementService.generateAccountStatement(accountNumber, startDate, endDate);

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
    public ResponseEntity<AccountDeletionResponseDTO> deleteAccount(@RequestBody AccountDeletionRequestDTO request){
        AccountDeletionResponseDTO response = bankCustomersService.deleteAccount(request);
        return ResponseEntity.ok(response);
        // Why is this endpoint not like the rest here?
    }


}
