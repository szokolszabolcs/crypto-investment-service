package com.xm.crypto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static com.xm.crypto.model.dto.ErrorCode.CRYPTO_DATA_NOT_FOUND;
import static com.xm.crypto.model.dto.ErrorCode.INVALID_PARAMETER;
import static com.xm.crypto.model.dto.ErrorCode.MISSING_PARAMETER;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        properties = {
                "crypto.prices.dir=classpath:prices"
        }
)
@AutoConfigureMockMvc
class CryptoInvestmentServiceApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Should return sorted list of cryptos by normalized range")
    void testListByNormalizedRange() throws Exception {
        mockMvc.perform(get("/cryptos/list-by-normalized-range")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.normalizedCryptos").isArray())
                .andExpect(jsonPath("$.normalizedCryptos[0].symbol").exists())
                .andExpect(jsonPath("$.normalizedCryptos[0].normalizedRange").exists());
    }

    @Test
    @DisplayName("Should return stats for a valid crypto symbol")
    void testGetStats() throws Exception {
        mockMvc.perform(get("/cryptos/BTC/stats")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stats.symbol").value("BTC"))
                .andExpect(jsonPath("$.stats.oldest").exists())
                .andExpect(jsonPath("$.stats.newest").exists())
                .andExpect(jsonPath("$.stats.min").exists())
                .andExpect(jsonPath("$.stats.max").exists())
                .andExpect(jsonPath("$.stats.oldest.timestamp").exists())
                .andExpect(jsonPath("$.stats.oldest.price").exists())
                .andExpect(jsonPath("$.stats.newest.timestamp").exists())
                .andExpect(jsonPath("$.stats.newest.price").exists())
                .andExpect(jsonPath("$.stats.min.timestamp").exists())
                .andExpect(jsonPath("$.stats.min.price").exists())
                .andExpect(jsonPath("$.stats.max.timestamp").exists())
                .andExpect(jsonPath("$.stats.max.price").exists());
    }

    @Test
    @DisplayName("Should return 404 for stats of unknown symbol")
    void testGetStatsNotFound() throws Exception {
        mockMvc.perform(get("/cryptos/UNKNOWN/stats")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value(CRYPTO_DATA_NOT_FOUND.name()))
                .andExpect(jsonPath("$.message").value("There is no data for the requested crypto: UNKNOWN"));
    }

    @Test
    @DisplayName("Should return crypto with highest normalized range for a specific day")
    void testGetHighestNormalizedRange() throws Exception {
        mockMvc.perform(get("/cryptos/highest-normalized-range")
                        .param("date", "2022-01-01")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.highestNormalizedRangeCrypto").exists())
                .andExpect(jsonPath("$.highestNormalizedRangeCrypto.symbol").exists())
                .andExpect(jsonPath("$.highestNormalizedRangeCrypto.normalizedRange").exists());
    }

    @Test
    @DisplayName("Should return 400 Bad Request if date parameter is missing for highest normalized range")
    void testGetHighestNormalizedRangeMissingDate() throws Exception {
        mockMvc.perform(get("/cryptos/highest-normalized-range")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(MISSING_PARAMETER.name()))
                .andExpect(jsonPath("$.message").value("Required parameter 'date' is missing."));
    }

    @Test
    @DisplayName("Should return 400 Bad Request if date parameter is invalid for highest normalized range")
    void testGetHighestNormalizedRangeInvalidDate() throws Exception {
        mockMvc.perform(get("/cryptos/highest-normalized-range")
                        .param("date", "not-a-date")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(INVALID_PARAMETER.name()))
                .andExpect(jsonPath("$.message").value("An invalid value was provided for 'date' parameter."));
    }

    @Test
    @DisplayName("Should return 404 if no data is found for date in highest normalized range")
    void testGetHighestNormalizedRangeNoData() throws Exception {
        mockMvc.perform(get("/cryptos/highest-normalized-range")
                        .param("date", "1999-01-01")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value(CRYPTO_DATA_NOT_FOUND.name()))
                .andExpect(jsonPath("$.message").value("No crypto data found for the requested date: 1999-01-01"));
    }
}
