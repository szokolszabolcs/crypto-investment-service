package com.xm.crypto.controller;

import com.xm.crypto.model.dto.response.CryptoStatsResponse;
import com.xm.crypto.model.dto.response.HighestNormalizedRangeResponse;
import com.xm.crypto.model.dto.response.NormalizedCryptosResponse;
import com.xm.crypto.service.CryptoStatsService;
import com.xm.crypto.service.NormalizedRangeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/cryptos")
public class CryptoController {

    private final NormalizedRangeService normalizedRangeService;
    private final CryptoStatsService cryptoStatsService;

    public CryptoController(
            NormalizedRangeService normalizedRangeService,
            CryptoStatsService cryptoStatsService
    ){
        this.normalizedRangeService = normalizedRangeService;
        this.cryptoStatsService = cryptoStatsService;
    }

    @GetMapping("/list-by-normalized-range")
    public NormalizedCryptosResponse listByNormalizedRange(){
        return new NormalizedCryptosResponse(normalizedRangeService.listByNormalizedRange());
    }

    @GetMapping("/{symbol}/stats")
    public CryptoStatsResponse getStats(@PathVariable String symbol){
        return new CryptoStatsResponse(cryptoStatsService.getStats(symbol));
    }

    @GetMapping("/highest-normalized-range")
    public HighestNormalizedRangeResponse getHighestNormalizedRange(@RequestParam LocalDate date){
        return new HighestNormalizedRangeResponse(normalizedRangeService.getHighestNormalizedRange(date));
    }
}
