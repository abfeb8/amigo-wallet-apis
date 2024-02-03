package com.ab.amigo.dto;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public record BankDTO(
        @NotNull(message = "Account Number is required")
        Integer accNum,
        @NotNull(message = "Bank code is required")
        @Pattern(regexp = "^[a-zA-Z]{4}\\d{6,8}$", message = "Bank code is invalid")
        String bankCode,
        @NotNull(message = "Account holder name is required")
        @Pattern(regexp = "^[a-zA-Z ]*$", message = "Account holder’s name should contain only alphabets and spaces")
        String accHolderName
) {
}
