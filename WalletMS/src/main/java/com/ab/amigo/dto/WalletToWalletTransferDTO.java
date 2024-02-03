package com.ab.amigo.dto;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

public record WalletToWalletTransferDTO(
        @NotNull(message = "walletId is required")
        Integer walletId,
        @NotNull(message = "receiverEmailId is required")
        @Email(message = "Invalid Receiver Email Id")
        String receiverEmailId,
        @NotNull(message = "Amount is required")
        @Positive(message = "Amount should be greater than 0")
        @Digits(integer = 10, fraction = 2, message = "Amount can contain only 2 digits after the decimal")
        BigDecimal amount
) {
}
