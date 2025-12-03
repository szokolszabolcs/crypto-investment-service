package com.xm.crypto.service;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.xm.crypto.model.dto.CryptoCsvDto;
import com.xm.crypto.model.entity.CryptoEntity;
import com.xm.crypto.model.mapper.CryptoCsvDtoMapper;
import com.xm.crypto.repository.CryptoRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
public class CryptoDataLoaderService {
    private static final String CRYPTO_CSV_FILE_POSTFIX = "_values.csv";

    private static final Logger logger = LoggerFactory.getLogger(CryptoDataLoaderService.class);

    @Value("${crypto.prices.dir}")
    private String cryptoPricesDir;

    private final CryptoRepository cryptoRepository;
    private final CryptoCsvDtoMapper cryptoCsvDtoMapper;

    public CryptoDataLoaderService(
            CryptoRepository cryptoRepository,
            CryptoCsvDtoMapper cryptoCsvDtoMapper
    ) {
        this.cryptoRepository = cryptoRepository;
        this.cryptoCsvDtoMapper = cryptoCsvDtoMapper;
    }

    @PostConstruct
    public void loadCryptoData() {
        logger.info("Loading crypto data on startup from location: {}", cryptoPricesDir);

        try {
            File[] cryptoCsvFiles = resolveCryptoCsvFiles(cryptoPricesDir);
            if (cryptoCsvFiles == null || cryptoCsvFiles.length == 0) {
                logger.error("No valid crypto CSV files were found on the provided path: {}", cryptoPricesDir);
                throw new IllegalArgumentException("No valid crypto CSV files were found on the provided path: " + cryptoPricesDir);
            }

            Arrays.stream(cryptoCsvFiles)
                    .forEach(this::processCryptoCsvFile);
        } catch (Exception exception) {
            throw new RuntimeException("An error occurred during the loading of the crypto data", exception);
        }

        logger.info("Crypto data was successfully loaded.");
    }

    private File[] resolveCryptoCsvFiles(String path) throws IOException {
        if (path.startsWith("classpath:")) {
            String resourcePath = path.substring("classpath:".length());
            ClassPathResource resource = new ClassPathResource(resourcePath);
            File dir = resource.getFile();
            return dir.listFiles((d, name) -> name.endsWith(CRYPTO_CSV_FILE_POSTFIX));
        } else {
            File dir = new File(path);
            return dir.listFiles((d, name) -> name.endsWith(CRYPTO_CSV_FILE_POSTFIX));
        }
    }

    private void processCryptoCsvFile(File file) {
        List<CryptoCsvDto> cryptoCsvDtos = readCryptoCsv(file);
        List<CryptoEntity> cryptoEntities = cryptoCsvDtos.stream()
                .map(cryptoCsvDtoMapper::toEntity)
                .toList();
        cryptoRepository.saveAll(cryptoEntities);
    }

    private List<CryptoCsvDto> readCryptoCsv(File csvFile) {
        try (var input = csvFile.toURI().toURL().openStream()) {
            var mapper = new CsvMapper();
            var schema = CsvSchema.emptySchema().withHeader();
            return mapper.readerFor(CryptoCsvDto.class)
                    .with(schema)
                    .<CryptoCsvDto>readValues(input)
                    .readAll();
        } catch (Exception exception) {
            logger.warn("Failed to read file: {}", csvFile.getName(), exception);
            return List.of();
        }
    }
}
