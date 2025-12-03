package com.xm.crypto.service;

import com.xm.crypto.model.dto.CryptoStatsDto;

/**
 * Service interface for retrieving statistics about a specific cryptocurrency.
 * Provides methods to obtain the oldest, newest, minimum, and maximum price data for a given symbol.
 */
public interface CryptoStatsService {

    /**
     * Retrieves statistics for the given crypto symbol, including the oldest, newest,
     * minimum, and maximum price data points.
     *
     * @param symbol the crypto symbol (case-insensitive)
     * @return a {@link CryptoStatsDto} containing the stats for the symbol
     * @throws NoCryptoDataFoundException if no data is found for the given symbol
     */
    CryptoStatsDto getStats(String symbol);
}
