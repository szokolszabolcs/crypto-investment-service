package com.xm.crypto.model.domain;

import java.math.BigDecimal;

public record NormalizedCrypto(
        String symbol,
        BigDecimal normalizedRange
) {
}
