package com.ab.amigo.dto;

import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

public record WalletToBankTransferDTO(
        @NotNull(message = "Wallet Id is required")
        Integer walletId,
        @NotNull(message = "Amount is required")
        @Positive(message = "Amount should be greater than 0")
        @Digits(integer = 10, fraction = 2, message = "Amount can only contain 2 digits after the decimal")
        BigDecimal amount,
        @NotNull(message = "Bank details are required")
        @Valid
        BankDTO bankDTO
) {
}
