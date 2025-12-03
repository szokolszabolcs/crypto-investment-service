package com.xm.crypto.service;

import com.xm.crypto.exception.NoCryptoDataFoundException;
import com.xm.crypto.model.domain.Crypto;
import com.xm.crypto.model.dto.CryptoStatsDto;
import com.xm.crypto.model.entity.CryptoEntity;
import com.xm.crypto.model.mapper.CryptoEntityMapper;
import com.xm.crypto.repository.CryptoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link CryptoStatsServiceImpl}.
 */
class CryptoStatsServiceImplTest {
    private static final String EXPECTED_MESSAGE = "There is no data for the requested crypto: ";

    private CryptoRepository cryptoRepository;
    private CryptoEntityMapper cryptoEntityMapper;
    private CryptoStatsServiceImpl service;

    @BeforeEach
    void setUp() {
        cryptoRepository = mock(CryptoRepository.class);
        cryptoEntityMapper = mock(CryptoEntityMapper.class);
        service = new CryptoStatsServiceImpl(cryptoRepository, cryptoEntityMapper);
    }

    @Test
    void getStats_returnsStats_whenDataExists() {
        // GIVEN
        String symbol = "btc";
        String upperCaseSymbol = "BTC";
        LocalDateTime now = LocalDateTime.now();
        BigDecimal price = BigDecimal.valueOf(100);

        CryptoEntity entity = mock(CryptoEntity.class);
        Crypto crypto = new Crypto(now, upperCaseSymbol, price);

        when(cryptoRepository.findFirstByIdSymbolOrderByIdTimestampAsc(upperCaseSymbol)).thenReturn(Optional.of(entity));
        when(cryptoRepository.findFirstByIdSymbolOrderByIdTimestampDesc(upperCaseSymbol)).thenReturn(Optional.of(entity));
        when(cryptoRepository.findFirstByIdSymbolOrderByPriceAsc(upperCaseSymbol)).thenReturn(Optional.of(entity));
        when(cryptoRepository.findFirstByIdSymbolOrderByPriceDesc(upperCaseSymbol)).thenReturn(Optional.of(entity));
        when(cryptoEntityMapper.toDomain(entity)).thenReturn(crypto);

        // WHEN
        CryptoStatsDto result = service.getStats(symbol);

        // THEN
        assertNotNull(result);
        assertEquals(upperCaseSymbol, result.symbol());
        assertEquals(now, result.oldest().timestamp());
        assertEquals(price, result.oldest().price());
        assertEquals(now, result.newest().timestamp());
        assertEquals(price, result.newest().price());
        assertEquals(now, result.min().timestamp());
        assertEquals(price, result.min().price());
        assertEquals(now, result.max().timestamp());
        assertEquals(price, result.max().price());
    }

    @Test
    void getStats_throwsException_whenOldestIsMissing() {
        // GIVEN
        String symbol = "btc";
        String upperCaseSymbol = "BTC";

        when(cryptoRepository.findFirstByIdSymbolOrderByIdTimestampAsc(upperCaseSymbol)).thenReturn(Optional.empty());

        // WHEN & THEN
        NoCryptoDataFoundException ex = assertThrows(
                NoCryptoDataFoundException.class,
                () -> service.getStats(symbol)
        );
        assertEquals(EXPECTED_MESSAGE + upperCaseSymbol, ex.getMessage());
    }

    @Test
    void getStats_throwsException_whenNewestIsMissing() {
        // GIVEN
        String symbol = "btc";
        String upperCaseSymbol = "BTC";
        CryptoEntity entity = mock(CryptoEntity.class);
        Crypto crypto = new Crypto(LocalDateTime.now(), upperCaseSymbol, BigDecimal.ONE);

        when(cryptoRepository.findFirstByIdSymbolOrderByIdTimestampAsc(upperCaseSymbol)).thenReturn(Optional.of(entity));
        when(cryptoRepository.findFirstByIdSymbolOrderByIdTimestampDesc(upperCaseSymbol)).thenReturn(Optional.empty());
        when(cryptoEntityMapper.toDomain(entity)).thenReturn(crypto);

        // WHEN & THEN
        NoCryptoDataFoundException ex = assertThrows(
                NoCryptoDataFoundException.class,
                () -> service.getStats(symbol)
        );
        assertEquals(EXPECTED_MESSAGE + upperCaseSymbol, ex.getMessage());
    }

    @Test
    void getStats_throwsException_whenMinIsMissing() {
        // GIVEN
        String symbol = "btc";
        String upperCaseSymbol = "BTC";
        CryptoEntity entity = mock(CryptoEntity.class);
        Crypto crypto = new Crypto(LocalDateTime.now(), upperCaseSymbol, BigDecimal.ONE);

        when(cryptoRepository.findFirstByIdSymbolOrderByIdTimestampAsc(upperCaseSymbol)).thenReturn(Optional.of(entity));
        when(cryptoRepository.findFirstByIdSymbolOrderByIdTimestampDesc(upperCaseSymbol)).thenReturn(Optional.of(entity));
        when(cryptoRepository.findFirstByIdSymbolOrderByPriceAsc(upperCaseSymbol)).thenReturn(Optional.empty());
        when(cryptoEntityMapper.toDomain(entity)).thenReturn(crypto);

        // WHEN & THEN
        NoCryptoDataFoundException ex = assertThrows(
                NoCryptoDataFoundException.class,
                () -> service.getStats(symbol)
        );
        assertEquals(EXPECTED_MESSAGE + upperCaseSymbol, ex.getMessage());
    }

    @Test
    void getStats_throwsException_whenMaxIsMissing() {
        // GIVEN
        String symbol = "btc";
        String upperCaseSymbol = "BTC";
        CryptoEntity entity = mock(CryptoEntity.class);
        Crypto crypto = new Crypto(LocalDateTime.now(), upperCaseSymbol, BigDecimal.ONE);

        when(cryptoRepository.findFirstByIdSymbolOrderByIdTimestampAsc(upperCaseSymbol)).thenReturn(Optional.of(entity));
        when(cryptoRepository.findFirstByIdSymbolOrderByIdTimestampDesc(upperCaseSymbol)).thenReturn(Optional.of(entity));
        when(cryptoRepository.findFirstByIdSymbolOrderByPriceAsc(upperCaseSymbol)).thenReturn(Optional.of(entity));
        when(cryptoRepository.findFirstByIdSymbolOrderByPriceDesc(upperCaseSymbol)).thenReturn(Optional.empty());
        when(cryptoEntityMapper.toDomain(entity)).thenReturn(crypto);

        // WHEN & THEN
        NoCryptoDataFoundException ex = assertThrows(
                NoCryptoDataFoundException.class,
                () -> service.getStats(symbol)
        );
        assertEquals(EXPECTED_MESSAGE + upperCaseSymbol, ex.getMessage());
    }
}
