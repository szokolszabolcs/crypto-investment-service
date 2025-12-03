package com.xm.crypto.model.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record Crypto(
        LocalDateTime timestamp,
        String symbol,
        BigDecimal price
) {
}
