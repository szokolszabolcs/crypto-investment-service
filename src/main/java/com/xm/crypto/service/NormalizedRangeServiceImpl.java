package com.xm.crypto.service;

import com.xm.crypto.exception.NoCryptoDataFoundException;
import com.xm.crypto.model.domain.NormalizedCrypto;
import com.xm.crypto.model.dto.NormalizedCryptoDto;
import com.xm.crypto.model.mapper.NormalizedCryptoMapper;
import com.xm.crypto.repository.CryptoRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * Service implementation for calculating normalized range statistics for cryptocurrencies.
 * Provides methods to list all cryptos by normalized range and to find the crypto with the highest normalized range for a specific day.
 */
@Service
public class NormalizedRangeServiceImpl implements NormalizedRangeService {

    private final CryptoRepository cryptoRepository;
    private final NormalizedCryptoMapper normalizedCryptoMapper;

    /**
     * Constructs a new NormalizedRangeServiceImpl with the required dependencies.
     *
     * @param cryptoRepository       the repository for accessing crypto data
     * @param normalizedCryptoMapper the mapper for converting domain objects to DTOs
     */
    public NormalizedRangeServiceImpl(CryptoRepository cryptoRepository, NormalizedCryptoMapper normalizedCryptoMapper) {
        this.cryptoRepository = cryptoRepository;
        this.normalizedCryptoMapper = normalizedCryptoMapper;
    }

    /**
     * Returns a descending sorted list of all cryptos, comparing the normalized range
     * (i.e., (max - min) / min) for each symbol.
     *
     * @return a list of {@link NormalizedCryptoDto} sorted by normalized range in descending order
     */
    @Override
    public List<NormalizedCryptoDto> listByNormalizedRange() {
        List<String> symbols = cryptoRepository.findAllSymbols();

        return symbols.stream()
                .map(symbol -> {
                    BigDecimal min = cryptoRepository.findMinPriceBySymbol(symbol);
                    BigDecimal max = cryptoRepository.findMaxPriceBySymbol(symbol);
                    if (min == null || min.compareTo(BigDecimal.ZERO) == 0) {
                        return null; // Avoid division by zero
                    }
                    BigDecimal normalizedRange = (max.subtract(min)).divide(min, 8, RoundingMode.HALF_UP);
                    return new NormalizedCrypto(symbol, normalizedRange);
                })
                .filter(Objects::nonNull)
                .sorted(Comparator.comparing(NormalizedCrypto::normalizedRange).reversed())
                .map(normalizedCryptoMapper::toDto)
                .toList();
    }

    /**
     * Returns the crypto with the highest normalized range for a specific day.
     * The normalized range is calculated as (max - min) / min for each symbol within the given date.
     *
     * @param date the date for which to find the highest normalized range
     * @return a {@link NormalizedCryptoDto} representing the crypto with the highest normalized range
     * @throws NoCryptoDataFoundException if no data is found for the requested date
     */
    @Override
    public NormalizedCryptoDto getHighestNormalizedRange(LocalDate date) {
        long startMillis = date.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli();
        long endMillis = date.plusDays(1).atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli();
        List<String> symbols = cryptoRepository.findAllSymbols();

        return symbols.stream()
                .map(symbol -> {
                    BigDecimal min = cryptoRepository.findMinPriceBySymbolAndTimeRange(symbol, startMillis, endMillis);
                    BigDecimal max = cryptoRepository.findMaxPriceBySymbolAndTimeRange(symbol, startMillis, endMillis);
                    if (min == null || min.compareTo(BigDecimal.ZERO) == 0) {
                        return null; // Avoid division by zero
                    }
                    BigDecimal normalizedRange = (max.subtract(min)).divide(min, 8, RoundingMode.HALF_UP);
                    return new NormalizedCrypto(symbol, normalizedRange);
                })
                .filter(Objects::nonNull)
                .max(Comparator.comparing(NormalizedCrypto::normalizedRange))
                .map(normalizedCryptoMapper::toDto)
                .orElseThrow(() -> new NoCryptoDataFoundException("No crypto data found for the requested date: " + date));
    }
}
