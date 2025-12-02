package com.xm.crypto.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(name = "NormalizedCrypto", description = "Symbol and normalized range for a given crypto")
public record NormalizedCryptoDto(
        @Schema(description = "Symbol of a crypto", example = "BTC")
        String symbol,

        @Schema(description = "Normalized range (i.e. (max-min)/min)", example = "46813.21")
        BigDecimal normalizedRange
) {
}
