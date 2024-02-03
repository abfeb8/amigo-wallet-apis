package com.ab.amigo.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

public record ChangePassDTO(
        @NotNull(message = "email id is required")
        @Email(message = "email id format is incorrect")
        String emailId,
        @NotNull(message = "old password is required")
        String oldPass,
        @NotNull(message = "new password is required")
        String newPass
) {
}
