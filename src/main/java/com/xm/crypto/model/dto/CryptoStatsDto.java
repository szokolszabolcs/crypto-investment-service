package com.xm.crypto.model.dto;

public record CryptoStatsDto(
       String symbol,
       CryptoValueDto oldest,
       CryptoValueDto newest,
       CryptoValueDto min,
       CryptoValueDto max
) {
}
