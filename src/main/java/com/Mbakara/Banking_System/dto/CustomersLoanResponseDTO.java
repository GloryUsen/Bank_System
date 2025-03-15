package com.Mbakara.Banking_System.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class CustomersLoanResponseDTO {
    private String responseCode;
    private String responseMessage;
    private CustomersLoanInfoDTO loanInfo;
}
