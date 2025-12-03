package com.xm.crypto.service;

import com.xm.crypto.exception.NoCryptoDataFoundException;
import com.xm.crypto.model.domain.NormalizedCrypto;
import com.xm.crypto.model.dto.NormalizedCryptoDto;
import com.xm.crypto.model.mapper.NormalizedCryptoMapper;
import com.xm.crypto.repository.CryptoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class NormalizedRangeServiceImplTest {

    private CryptoRepository cryptoRepository;
    private NormalizedCryptoMapper normalizedCryptoMapper;
    private NormalizedRangeServiceImpl service;

    @BeforeEach
    void setUp() {
        cryptoRepository = mock(CryptoRepository.class);
        normalizedCryptoMapper = mock(NormalizedCryptoMapper.class);
        service = new NormalizedRangeServiceImpl(cryptoRepository, normalizedCryptoMapper);
    }

    @Test
    void listByNormalizedRange_returnsSortedList_whenDataExists() {
        // GIVEN
        List<String> symbols = List.of("BTC", "ETH");
        when(cryptoRepository.findAllSymbols()).thenReturn(symbols);

        // BTC: min=100, max=200, normalizedRange=1.0
        // ETH: min=50, max=100, normalizedRange=1.0
        when(cryptoRepository.findMinPriceBySymbol("BTC")).thenReturn(BigDecimal.valueOf(100));
        when(cryptoRepository.findMaxPriceBySymbol("BTC")).thenReturn(BigDecimal.valueOf(200));
        when(cryptoRepository.findMinPriceBySymbol("ETH")).thenReturn(BigDecimal.valueOf(50));
        when(cryptoRepository.findMaxPriceBySymbol("ETH")).thenReturn(BigDecimal.valueOf(100));

        NormalizedCrypto btcNorm = new NormalizedCrypto("BTC", BigDecimal.valueOf(100000000, 8));
        NormalizedCrypto ethNorm = new NormalizedCrypto("ETH", BigDecimal.valueOf(100000000, 8));
        NormalizedCryptoDto btcDto = new NormalizedCryptoDto("BTC", BigDecimal.valueOf(100000000, 8));
        NormalizedCryptoDto ethDto = new NormalizedCryptoDto("ETH", BigDecimal.valueOf(100000000, 8));

        when(normalizedCryptoMapper.toDto(eq(btcNorm))).thenReturn(btcDto);
        when(normalizedCryptoMapper.toDto(eq(ethNorm))).thenReturn(ethDto);

        // WHEN
        List<NormalizedCryptoDto> result = service.listByNormalizedRange();

        // THEN
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(dto -> dto.symbol().equals("BTC")));
        assertTrue(result.stream().anyMatch(dto -> dto.symbol().equals("ETH")));
    }

    @Test
    void listByNormalizedRange_skipsSymbol_whenMinIsNullOrZero() {
        // GIVEN
        List<String> symbols = List.of("BTC", "ETH");
        when(cryptoRepository.findAllSymbols()).thenReturn(symbols);

        // BTC: min is null, should be skipped
        when(cryptoRepository.findMinPriceBySymbol("BTC")).thenReturn(null);
        // ETH: min is zero, should be skipped
        when(cryptoRepository.findMinPriceBySymbol("ETH")).thenReturn(BigDecimal.ZERO);

        // WHEN
        List<NormalizedCryptoDto> result = service.listByNormalizedRange();

        // THEN
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getHighestNormalizedRange_returnsDto_whenDataExists() {
        // GIVEN
        List<String> symbols = List.of("BTC", "ETH");
        when(cryptoRepository.findAllSymbols()).thenReturn(symbols);

        LocalDate date = LocalDate.of(2022, 1, 1);
        long startMillis = date.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli();
        long endMillis = date.plusDays(1).atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli();

        // BTC: min=100, max=200, normalizedRange=1.0
        // ETH: min=50, max=150, normalizedRange=2.0 (should be highest)
        when(cryptoRepository.findMinPriceBySymbolAndTimeRange("BTC", startMillis, endMillis)).thenReturn(BigDecimal.valueOf(100));
        when(cryptoRepository.findMaxPriceBySymbolAndTimeRange("BTC", startMillis, endMillis)).thenReturn(BigDecimal.valueOf(200));
        when(cryptoRepository.findMinPriceBySymbolAndTimeRange("ETH", startMillis, endMillis)).thenReturn(BigDecimal.valueOf(50));
        when(cryptoRepository.findMaxPriceBySymbolAndTimeRange("ETH", startMillis, endMillis)).thenReturn(BigDecimal.valueOf(150));

        NormalizedCrypto ethNorm = new NormalizedCrypto("ETH", BigDecimal.valueOf(200000000, 8));
        NormalizedCryptoDto ethDto = new NormalizedCryptoDto("ETH", BigDecimal.valueOf(200000000, 8));

        when(normalizedCryptoMapper.toDto(ethNorm)).thenReturn(ethDto);

        // WHEN
        NormalizedCryptoDto result = service.getHighestNormalizedRange(date);

        // THEN
        assertNotNull(result);
        assertEquals("ETH", result.symbol());
        assertEquals(BigDecimal.valueOf(200000000, 8), result.normalizedRange());
    }

    @Test
    void getHighestNormalizedRange_throwsException_whenNoValidData() {
        // GIVEN
        List<String> symbols = List.of("BTC");
        when(cryptoRepository.findAllSymbols()).thenReturn(symbols);

        LocalDate date = LocalDate.of(2022, 1, 1);
        long startMillis = date.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli();
        long endMillis = date.plusDays(1).atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli();

        // BTC: min is null, so should be skipped
        when(cryptoRepository.findMinPriceBySymbolAndTimeRange("BTC", startMillis, endMillis)).thenReturn(null);

        // WHEN & THEN
        assertThrows(NoCryptoDataFoundException.class, () -> service.getHighestNormalizedRange(date));
    }
}
