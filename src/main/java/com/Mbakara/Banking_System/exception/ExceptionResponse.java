package com.Mbakara.Banking_System.exception;


import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionResponse {


    private LocalDateTime timeStamp;
    private HttpStatus status;
    private Integer error;
    private String message;



    public ExceptionResponse(HttpStatus status, Integer error, String message){
       this.status = status;
       this.error = error;
        this.message = message;
        this.timeStamp = LocalDateTime.now();
    }



}
