package com.Mbakara.Banking_System.controller;

import com.Mbakara.Banking_System.dto.BankUserRequestDTO;
import com.Mbakara.Banking_System.dto.UserBankResponseDTO;
import com.Mbakara.Banking_System.service.BankUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bank_user")
public class BankUserController {

    @Autowired
    BankUserService bankUserService;

    @PostMapping
    public UserBankResponseDTO createAccount(@RequestBody BankUserRequestDTO request){
        return bankUserService.creatAccount(request);

    }

}
