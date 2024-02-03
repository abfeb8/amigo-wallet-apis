package com.ab.amigo.dto;

import java.math.BigDecimal;

public record BillDTO(
        String merchant,
        String utility,
        BigDecimal amount
) {
}
