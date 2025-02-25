package com.Mbakara.Banking_System.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class CustomerEnquiryRequestDTO {

    @Schema(
            name = "User's accountNumber"
    )
    private String accountNumber;
}
