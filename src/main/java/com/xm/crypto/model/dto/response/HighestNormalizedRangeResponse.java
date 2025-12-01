package com.xm.crypto.model.dto.response;

import com.xm.crypto.model.dto.NormalizedCryptoDto;

public record HighestNormalizedRangeResponse(
        NormalizedCryptoDto highestNormalizedRangeCrypto
) {
}
