package com.xm.crypto.service;

import com.xm.crypto.model.dto.NormalizedCryptoDto;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class NormalizedRangeServiceImpl implements NormalizedRangeService{

    @Override
    public List<NormalizedCryptoDto> listByNormalizedRange() {
        // TODO implement
        return List.of(
                new NormalizedCryptoDto("BTC", BigDecimal.TEN),
                new NormalizedCryptoDto("ETH", BigDecimal.TWO)
        );
    }

    @Override
    public NormalizedCryptoDto getHighestNormalizedRange(LocalDate date) {
        // TODO implement
        return new NormalizedCryptoDto(
                "BTC",
                BigDecimal.TEN
        );
    }
}
