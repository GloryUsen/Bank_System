package com.Mbakara.Banking_System.dto;

// DTO helps to separate object creation from Entity itself.
// DTO also help collect data from the user to sent to the database

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class BankUserRequestDTO {

    private String firstName;
    private String lastName;
    private String otherName;
    private String email;
    private String address;
    private String gender;
    private String phoneNumber;
    private String stateOfOrigin;
    private String alternativePhoneNumber;

}
