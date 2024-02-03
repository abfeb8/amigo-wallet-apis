package com.ab.amigo.dto;

public record CustomerDTO(
        Integer id,
        String firstName,
        String lastName,
        String email,
        Boolean isAdmin,
        String region,
        BankDTO bankAccount,
        WalletDTO wallet
) {
}
