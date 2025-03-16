package com.Mbakara.Banking_System.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class AccountDeletionResponseDTO {

    private String responseCode;
    private String responseMessage;
}
