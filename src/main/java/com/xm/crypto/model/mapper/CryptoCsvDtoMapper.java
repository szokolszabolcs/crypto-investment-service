package com.xm.crypto.model.mapper;

import com.xm.crypto.model.dto.CryptoCsvDto;
import com.xm.crypto.model.entity.CryptoEntity;
import org.springframework.stereotype.Component;

@Component
public class CryptoCsvDtoMapper {

    public CryptoEntity toEntity(CryptoCsvDto dto) {
        return new CryptoEntity(dto.timestamp(), dto.symbol().toUpperCase(), dto.price());
    }
}
