package com.xm.crypto.service;

import com.xm.crypto.model.dto.CryptoStatsDto;
import com.xm.crypto.model.dto.CryptoValueDto;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class CryptoStatsServiceImpl implements CryptoStatsService {

    @Override
    public CryptoStatsDto getStats(String symbol) {
        // TODO implement
        return new CryptoStatsDto(
                symbol,
                new CryptoValueDto(LocalDateTime.now().minusDays(30), BigDecimal.ONE),
                new CryptoValueDto(LocalDateTime.now().minusDays(1), BigDecimal.TWO),
                new CryptoValueDto(LocalDateTime.now().minusDays(10), BigDecimal.ZERO),
                new CryptoValueDto(LocalDateTime.now().minusDays(20), BigDecimal.TEN)
        );
    }
}
