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

/**
 * Service responsible for loading cryptocurrency data from CSV files into the database on application startup.
 * The service locates CSV files, parses them, maps them to entities, and persists them using the repository.
 */
@Service
public class CryptoDataLoaderService {
    private static final String CRYPTO_CSV_FILE_POSTFIX = "_values.csv";

    private static final Logger logger = LoggerFactory.getLogger(CryptoDataLoaderService.class);

    /**
     * The directory path (can be classpath or filesystem) where crypto CSV files are located.
     */
    @Value("${crypto.prices.dir}")
    private String cryptoPricesDir;

    private final CryptoRepository cryptoRepository;
    private final CryptoCsvDtoMapper cryptoCsvDtoMapper;

    /**
     * Constructs a new CryptoDataLoaderService with the required dependencies.
     *
     * @param cryptoRepository   The repository for persisting crypto entities
     * @param cryptoCsvDtoMapper The mapper for converting CSV DTOs to entities
     */
    public CryptoDataLoaderService(
            CryptoRepository cryptoRepository,
            CryptoCsvDtoMapper cryptoCsvDtoMapper
    ) {
        this.cryptoRepository = cryptoRepository;
        this.cryptoCsvDtoMapper = cryptoCsvDtoMapper;
    }

    /**
     * Loads crypto data from CSV files into the database on application startup.
     * It throws a runtime exception if loading fails and the application will be stopped in that case.
     */
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

    /**
     * Resolves and returns all CSV files with the expected postfix from the given path.
     * Supports both classpath and filesystem locations.
     *
     * @param path The directory path (can be prefixed with "classpath:")
     * @return An array of CSV files, or null if none found
     * @throws IOException if an I/O error occurs while accessing the files
     */
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

    /**
     * Processes a single CSV file: parses its contents, maps DTOs to entities, and saves them to the database.
     *
     * @param file The CSV file to process
     */
    private void processCryptoCsvFile(File file) {
        List<CryptoCsvDto> cryptoCsvDtos = readCryptoCsv(file);
        List<CryptoEntity> cryptoEntities = cryptoCsvDtos.stream()
                .map(cryptoCsvDtoMapper::toEntity)
                .toList();
        cryptoRepository.saveAll(cryptoEntities);
    }

    /**
     * Reads and parses a CSV file into a list of {@link CryptoCsvDto} objects.
     * Returns an empty list if reading or parsing fails.
     *
     * @param csvFile The CSV file to read
     * @return A list of parsed {@link CryptoCsvDto} objects, or an empty list on error
     */
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
