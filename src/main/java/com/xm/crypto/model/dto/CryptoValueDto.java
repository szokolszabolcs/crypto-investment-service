package com.xm.crypto.model.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CryptoValueDto(
        LocalDateTime timestamp,
        BigDecimal price
) {
}
