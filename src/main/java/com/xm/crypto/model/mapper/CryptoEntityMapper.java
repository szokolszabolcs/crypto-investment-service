package com.xm.crypto.model.mapper;

import com.xm.crypto.model.domain.Crypto;
import com.xm.crypto.model.entity.CryptoEntity;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Component
public class CryptoEntityMapper {

    public Crypto toDomain(CryptoEntity entity) {
        LocalDateTime timestamp = Instant.ofEpochMilli(entity.getId().getTimestamp())
                .atZone(ZoneOffset.UTC)
                .toLocalDateTime();
        String symbol = entity.getId().getSymbol();
        return new Crypto(timestamp, symbol, entity.getPrice());
    }
}
