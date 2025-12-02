package com.xm.crypto.model.mapper;

import com.xm.crypto.model.domain.NormalizedCrypto;
import com.xm.crypto.model.dto.NormalizedCryptoDto;
import org.springframework.stereotype.Component;

@Component
public class NormalizedCryptoMapper {

    public NormalizedCryptoDto toDto(NormalizedCrypto normalizedCrypto) {
        return new NormalizedCryptoDto(normalizedCrypto.symbol(), normalizedCrypto.normalizedRange());
    }
}
