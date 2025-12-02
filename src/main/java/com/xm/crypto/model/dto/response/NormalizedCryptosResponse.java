package com.xm.crypto.model.dto.response;

import com.xm.crypto.model.dto.NormalizedCryptoDto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Response containing the list of normalized cryptos")
public record NormalizedCryptosResponse(
        @Schema(description = "List of normalized cryptos")
        List<NormalizedCryptoDto> normalizedCryptos
) {
}
