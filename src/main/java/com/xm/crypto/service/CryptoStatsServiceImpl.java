package com.xm.crypto.service;

import com.xm.crypto.exception.NoCryptoDataFoundException;
import com.xm.crypto.model.dto.CryptoStatsDto;
import com.xm.crypto.model.dto.CryptoValueDto;
import com.xm.crypto.model.mapper.CryptoEntityMapper;
import com.xm.crypto.repository.CryptoRepository;
import org.springframework.stereotype.Service;

import java.util.function.Supplier;

/**
 * Service implementation for retrieving statistics about a specific cryptocurrency.
 * Provides oldest, newest, minimum, and maximum price data for a given symbol.
 */
@Service
public class CryptoStatsServiceImpl implements CryptoStatsService {

    private final CryptoRepository cryptoRepository;
    private final CryptoEntityMapper cryptoEntityMapper;

    /**
     * Constructs a new CryptoStatsServiceImpl with the required dependencies.
     *
     * @param cryptoRepository   The repository for accessing crypto data
     * @param cryptoEntityMapper The mapper for converting entities to domain objects
     */
    public CryptoStatsServiceImpl(CryptoRepository cryptoRepository, CryptoEntityMapper cryptoEntityMapper) {
        this.cryptoRepository = cryptoRepository;
        this.cryptoEntityMapper = cryptoEntityMapper;
    }

    /**
     * Retrieves statistics for the given crypto symbol, including the oldest, newest,
     * minimum, and maximum price data points.
     *
     * @param symbol the crypto symbol (case-insensitive)
     * @return a {@link CryptoStatsDto} containing the stats for the symbol
     * @throws NoCryptoDataFoundException if no data is found for the given symbol
     */
    @Override
    public CryptoStatsDto getStats(String symbol) {
        var upperCaseSymbol = symbol.toUpperCase();

        var oldest = cryptoRepository.findFirstByIdSymbolOrderByIdTimestampAsc(upperCaseSymbol)
                .map(cryptoEntityMapper::toDomain)
                .orElseThrow(noCryptoDataFoundException(upperCaseSymbol));

        var newest = cryptoRepository.findFirstByIdSymbolOrderByIdTimestampDesc(upperCaseSymbol)
                .map(cryptoEntityMapper::toDomain)
                .orElseThrow(noCryptoDataFoundException(upperCaseSymbol));

        var min = cryptoRepository.findFirstByIdSymbolOrderByPriceAsc(upperCaseSymbol)
                .map(cryptoEntityMapper::toDomain)
                .orElseThrow(noCryptoDataFoundException(upperCaseSymbol));

        var max = cryptoRepository.findFirstByIdSymbolOrderByPriceDesc(upperCaseSymbol)
                .map(cryptoEntityMapper::toDomain)
                .orElseThrow(noCryptoDataFoundException(upperCaseSymbol));

        return new CryptoStatsDto(
                upperCaseSymbol,
                new CryptoValueDto(oldest.timestamp(), oldest.price()),
                new CryptoValueDto(newest.timestamp(), newest.price()),
                new CryptoValueDto(min.timestamp(), min.price()),
                new CryptoValueDto(max.timestamp(), max.price())
        );
    }

    /**
     * Returns a supplier for a {@link NoCryptoDataFoundException} with a message
     * indicating that no data was found for the given symbol.
     *
     * @param symbol the crypto symbol
     * @return a supplier of {@link NoCryptoDataFoundException}
     */
    private static Supplier<NoCryptoDataFoundException> noCryptoDataFoundException(String symbol) {
        return () -> new NoCryptoDataFoundException("There is no data for the requested crypto: " + symbol);
    }
}