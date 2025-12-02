package com.xm.crypto.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(name = "CryptoValue", description = "Values for crypto stats")
public record CryptoValueDto(
        @Schema(description = "Timestamp of the crypto value", example = "2025-11-02T12:26:24.5364398")
        LocalDateTime timestamp,

        @Schema(description = "Price of a crypto for the data point", example = "46813.21")
        BigDecimal price
) {
}
