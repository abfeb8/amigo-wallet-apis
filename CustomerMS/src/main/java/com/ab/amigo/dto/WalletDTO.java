package com.ab.amigo.dto;

public record WalletDTO(
        Integer walletId,
        String emailId,
        Double balance
) {
}
