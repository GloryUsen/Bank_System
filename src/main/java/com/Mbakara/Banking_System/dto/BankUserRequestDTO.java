package com.Mbakara.Banking_System.dto;

// DTO helps to separate object creation from Entity itself.
// DTO also help collect data from the user to sent to the database

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class BankUserRequestDTO {

    @Schema(
            name = "User's Firstname"
    )
    private String firstName;

    @Schema(
            name = "User's Lastname"
    )
    private String lastName;

    @Schema(
            name = "User's OtherName"
    )
    private String otherName;

    @Schema(
            name = "User's Email"
    )
    private String email;

    @Schema(
            name = "User's Password"
    )
    private String password;

    @Schema(
            name = "User's Address"
    )
    private String address;

    @Schema(
            name = "User's Gender"
    )
    private String gender;

    @Schema(
            name = "User's PhoneNumber"
    )
    private String phoneNumber;

    @Schema(
            name = "User's StateOfOrigin"
    )
    private String stateOfOrigin;

    @Schema(
            name = "User's AlternativePhoneNumber"
    )
    private String alternativePhoneNumber;

}
