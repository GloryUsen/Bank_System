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

    private String recipient;


    private String messageBody;


    private String subject;


    private String attachment;


}
