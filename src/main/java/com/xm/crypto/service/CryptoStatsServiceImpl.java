package com.xm.crypto.service;

import com.xm.crypto.exception.NoCryptoDataFoundException;
import com.xm.crypto.model.dto.CryptoStatsDto;
import com.xm.crypto.model.dto.CryptoValueDto;
import com.xm.crypto.model.mapper.CryptoEntityMapper;
import com.xm.crypto.repository.CryptoRepository;
import org.springframework.stereotype.Service;

import java.util.function.Supplier;

@Service
public class CryptoStatsServiceImpl implements CryptoStatsService {

    private final CryptoRepository cryptoRepository;
    private final CryptoEntityMapper cryptoEntityMapper;

    public CryptoStatsServiceImpl(CryptoRepository cryptoRepository, CryptoEntityMapper cryptoEntityMapper) {
        this.cryptoRepository = cryptoRepository;
        this.cryptoEntityMapper = cryptoEntityMapper;
    }

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

    private static Supplier<NoCryptoDataFoundException> noCryptoDataFoundException(String symbol) {
        return () -> new NoCryptoDataFoundException("There is no data for the requested crypto: " + symbol);
    }
}
