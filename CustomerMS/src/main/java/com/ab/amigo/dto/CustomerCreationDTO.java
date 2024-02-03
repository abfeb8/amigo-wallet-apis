package com.ab.amigo.dto;

import javax.validation.constraints.NotNull;

public record CustomerCreationDTO(
        @NotNull(message = "First name is required")
        String fristName,
        String lastName,
        @NotNull(message = "email is required")
        String email,
        @NotNull(message = "password is required")
        String password,
        Boolean isAdmin,
        String region
) {
}
