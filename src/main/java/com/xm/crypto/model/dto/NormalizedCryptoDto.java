package com.xm.crypto.model.dto;

import java.math.BigDecimal;

public record NormalizedCryptoDto(
        String symbol,
        BigDecimal normalizedRange
) {
}
