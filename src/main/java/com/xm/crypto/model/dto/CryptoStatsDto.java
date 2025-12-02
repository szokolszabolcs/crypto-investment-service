package com.xm.crypto.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "CryptoStats", description = "Oldest/newest/min/max values for a requested crypto")
public record CryptoStatsDto(
        @Schema(description = "Symbol of a crypto", example = "BTC")
        String symbol,

        @Schema(description = "The oldest available value of a crypto")
        CryptoValueDto oldest,

        @Schema(description = "The newest available value of a crypto")
        CryptoValueDto newest,

        @Schema(description = "The minimum value of a crypto")
        CryptoValueDto min,

        @Schema(description = "The maximum value of a crypto")
        CryptoValueDto max
) {
}
