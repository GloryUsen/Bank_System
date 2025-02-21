package com.Mbakara.Banking_System.controller;

import com.Mbakara.Banking_System.dto.BankUserRequestDTO;
import com.Mbakara.Banking_System.dto.UserBankResponseDTO;
import com.Mbakara.Banking_System.service.CreateUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class CreateUserController {

    @Autowired
    CreateUserService bankUserService;

    @PostMapping("/create")
    public UserBankResponseDTO createAccount(@RequestBody BankUserRequestDTO request){
        System.out.println("hello world");
        return bankUserService.creatAccount(request);

    }



}
