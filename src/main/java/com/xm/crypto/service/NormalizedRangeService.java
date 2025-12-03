package com.xm.crypto.service;

import com.xm.crypto.model.dto.NormalizedCryptoDto;

import java.time.LocalDate;
import java.util.List;

/**
 * Service interface for calculating normalized range statistics for cryptocurrencies.
 * Provides methods to list all cryptos by normalized range and to find the crypto with the highest normalized range for a specific day.
 */
public interface NormalizedRangeService {

    /**
     * Returns a descending sorted list of all cryptos, comparing the normalized range
     * (i.e., (max - min) / min) for each symbol.
     *
     * @return a list of {@link NormalizedCryptoDto} sorted by normalized range in descending order
     */
    List<NormalizedCryptoDto> listByNormalizedRange();

    /**
     * Returns the crypto with the highest normalized range for a specific day.
     * The normalized range is calculated as (max - min) / min for each symbol within the given date.
     *
     * @param date the date for which to find the highest normalized range
     * @return a {@link NormalizedCryptoDto} representing the crypto with the highest normalized range
     * @throws NoCryptoDataFoundException if no data is found for the requested date
     */
    NormalizedCryptoDto getHighestNormalizedRange(LocalDate date);
}
