package com.Mbakara.Banking_System.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountDeletionRequestDTO {

    private String accountNumber;
    private String password;
}
