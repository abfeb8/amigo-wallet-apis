package com.ab.amigo.dto;

import javax.validation.constraints.*;
import java.math.BigDecimal;

public record OfferDTO(
        @NotNull(message = "OfferCode is required")
        String offerCode,
        @NotNull(message = "Offer info is required")
        String info,
        @NotNull(message = "percentOff if required")
        @Positive(message = "percentOff can not be negative")
        @DecimalMax(value = "100.00", message = "percentOff max value is 100")
        @Digits(integer = 3, fraction = 2, message = "percentOff can only contain 2 digits after the decimal")
        BigDecimal percentOff
) {
}
