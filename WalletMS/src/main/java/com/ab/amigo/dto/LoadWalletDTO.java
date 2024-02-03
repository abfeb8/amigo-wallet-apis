package com.ab.amigo.dto;

import javax.validation.constraints.NotNull;

public record LoadWalletDTO(
        @NotNull(message = "wallet id is required")
        Integer walletId,
        @NotNull(message = "recharge amount is required")
        Double rechargeAmount,
        @NotNull(message = "bank account id is required")
        Integer bankAccount
) {
}
