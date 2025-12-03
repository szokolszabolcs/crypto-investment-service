package com.xm.crypto.service;

import com.xm.crypto.repository.CryptoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(
        properties = {
                "crypto.prices.dir=classpath:prices"
        }
)
class CryptoDataLoaderServiceIntegrationTest {

    @Autowired
    private CryptoRepository cryptoRepository;

    @Test
    @DisplayName("Should load crypto data from CSV files into the database on startup")
    void shouldLoadCryptoDataFromCsvFiles() {
        // WHEN: The Spring context loads, @PostConstruct triggers data loading

        // THEN: Data should be present in the database
        long count = cryptoRepository.count();
        assertTrue(count > 0, "At least on crypto should be present in the database");

        // Check the existence of specific symbols
        List<String> symbols = cryptoRepository.findAllSymbols();
        assertTrue(symbols.contains("BTC"), "BTC symbol should be present");
        assertTrue(symbols.contains("ETH"), "ETH symbol should be present");
    }
}
