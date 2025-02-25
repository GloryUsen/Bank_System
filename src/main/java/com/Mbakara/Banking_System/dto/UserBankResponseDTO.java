package com.Mbakara.Banking_System.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor


public class UserBankResponseDTO {

    @Schema(
            name = "Status code"
    )

    private String responseCode;

    @Schema(
            name = "Message Body"
    )
    private String responseMessage;

    @Schema(
            name = "user's Information"
    )
    private AccountInfo accountInfo;
}
