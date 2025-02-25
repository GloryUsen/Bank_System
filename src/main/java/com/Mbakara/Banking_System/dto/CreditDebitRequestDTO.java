package com.Mbakara.Banking_System.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class CreditDebitRequestDTO {
    @Schema(
            name = "User's AccountNumber"
    )
    private String accountNumber;

    @Schema(
            name = "Amount To Update"
    )
    private BigDecimal amount;
}
