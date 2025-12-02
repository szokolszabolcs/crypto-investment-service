package com.xm.crypto.model.dto.response;

import com.xm.crypto.model.dto.NormalizedCryptoDto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response containing a crypto with the highest normalized range")
public record HighestNormalizedRangeResponse(
        @Schema(description = "Details of the normalized crypto")
        NormalizedCryptoDto highestNormalizedRangeCrypto
) {
}
