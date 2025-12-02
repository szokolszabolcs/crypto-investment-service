package com.xm.crypto.controller;

import com.xm.crypto.model.dto.ErrorResponse;
import com.xm.crypto.model.dto.response.CryptoStatsResponse;
import com.xm.crypto.model.dto.response.HighestNormalizedRangeResponse;
import com.xm.crypto.model.dto.response.NormalizedCryptosResponse;
import com.xm.crypto.service.CryptoStatsService;
import com.xm.crypto.service.NormalizedRangeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/cryptos")
@Tag(name = "Crypto Controller", description = "API for listing the cryptos and retrieve statistics about them")
public class CryptoController {

    private final NormalizedRangeService normalizedRangeService;
    private final CryptoStatsService cryptoStatsService;

    public CryptoController(
            NormalizedRangeService normalizedRangeService,
            CryptoStatsService cryptoStatsService
    ) {
        this.normalizedRangeService = normalizedRangeService;
        this.cryptoStatsService = cryptoStatsService;
    }

    @GetMapping("/list-by-normalized-range")
    @Operation(summary = "List cryptos sorted by normalized range", description = "Returns a descending sorted list of all cryptos by normalized range")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successful retrieval of the cryptos",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = NormalizedCryptosResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
    })
    public NormalizedCryptosResponse listByNormalizedRange() {
        return new NormalizedCryptosResponse(normalizedRangeService.listByNormalizedRange());
    }

    @GetMapping("/{symbol}/stats")
    @Operation(summary = "Get statistics for a crypto", description = "Returns statistics about a crypto: oldest/newest/min/max values")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successful retrieval of the statistics",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CryptoStatsResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not found - There is no data for the requested crypto",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
    })
    public CryptoStatsResponse getStats(
            @Parameter(
                    description = "Symbol of the crypto to which the statistic should be retrieved",
                    example = "BTC"
            )
            @PathVariable String symbol
    ) {
        return new CryptoStatsResponse(cryptoStatsService.getStats(symbol));
    }

    @GetMapping("/highest-normalized-range")
    @Operation(summary = "Get highest normalized range", description = "Returns the crypto with the highest normalized range for the requested day")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successful retrieval of the highest normalized range",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = HighestNormalizedRangeResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request - The provided parameters are invalid",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "404",
                    description = "Not found - There is no data for the requested day",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
    })
    public HighestNormalizedRangeResponse getHighestNormalizedRange(
            @Parameter(
                    description = "The requested day. The crypto with the highest normalized range will be returned for the day specified here.",
                    example = "2022-01-01"
            )
            @RequestParam LocalDate date
    ) {
        return new HighestNormalizedRangeResponse(normalizedRangeService.getHighestNormalizedRange(date));
    }
}
