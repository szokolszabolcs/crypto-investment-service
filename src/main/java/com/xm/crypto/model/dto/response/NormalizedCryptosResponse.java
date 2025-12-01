package com.xm.crypto.model.dto.response;

import com.xm.crypto.model.dto.NormalizedCryptoDto;

import java.util.List;

public record NormalizedCryptosResponse(
        List<NormalizedCryptoDto> normalizedCryptos
) {
}
