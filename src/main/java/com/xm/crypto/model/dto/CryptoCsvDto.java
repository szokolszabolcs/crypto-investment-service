package com.xm.crypto.model.dto;

import java.math.BigDecimal;

public record CryptoCsvDto(
        long timestamp,
        String symbol,
        BigDecimal price
) {
}
