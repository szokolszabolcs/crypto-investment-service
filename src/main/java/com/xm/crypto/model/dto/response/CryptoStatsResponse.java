package com.xm.crypto.model.dto.response;

import com.xm.crypto.model.dto.CryptoStatsDto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response containing statistics about a crypto")
public record CryptoStatsResponse(
        @Schema(description = "Statistics about a crypto")
        CryptoStatsDto stats
) {
}
