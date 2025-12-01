package com.xm.crypto.service;

import com.xm.crypto.model.dto.CryptoStatsDto;

public interface CryptoStatsService {
    CryptoStatsDto getStats(String symbol);
}
