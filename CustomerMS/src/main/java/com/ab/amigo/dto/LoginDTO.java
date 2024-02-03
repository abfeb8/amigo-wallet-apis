package com.ab.amigo.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

public record LoginDTO(
        @NotNull(message = "emailId is required")
        @Email(message = "email Id format incorrect")
        String emailId,
        @NotNull(message = "password is required")
        String password
) {
}
