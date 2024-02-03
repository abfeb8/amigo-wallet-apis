package com.ab.amigo.dto;

import java.util.List;

public record MerchantDTO(
        String name,
        List<String> utilities
) {
}
