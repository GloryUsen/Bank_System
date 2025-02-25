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

public class TransferRequestDTO {

    @Schema(
            name = "SourceAccountNumber"
    )
    private String sourceAccountNumber;

    @Schema(
            name = "Destination For Transaction"
    )
    private String destinationAccountNumber;

    @Schema(
            name = "Amount For Transaction"
    )
    private BigDecimal amount;
}
