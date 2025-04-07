package com.Mbakara.Banking_System.dto;


import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class APIResponse {
    private String statusCode;
    private String message;
    private boolean success;
    private Object payLoad;
}
