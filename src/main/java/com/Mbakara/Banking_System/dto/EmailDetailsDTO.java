package com.Mbakara.Banking_System.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class EmailDetailsDTO {
    @Schema(
            name = "Recipient To receive"
    )
    private String recipient;

    @Schema(
            name = "Message Body"
    )
    private String messageBody;

    @Schema(
            name = "Context Of The Message"
    )
    private String subject;

    @Schema(
            name = "File Attached To The Email"
    )
    private String attachment;


}
