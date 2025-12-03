package com.xm.crypto.repository;

import com.xm.crypto.model.entity.CryptoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface CryptoRepository extends JpaRepository<CryptoEntity, Long> {

    Optional<CryptoEntity> findFirstByIdSymbolOrderByIdTimestampAsc(String symbol);

    Optional<CryptoEntity> findFirstByIdSymbolOrderByIdTimestampDesc(String symbol);

    Optional<CryptoEntity> findFirstByIdSymbolOrderByPriceAsc(String symbol);

    Optional<CryptoEntity> findFirstByIdSymbolOrderByPriceDesc(String symbol);

    @Query("SELECT DISTINCT c.id.symbol FROM CryptoEntity c")
    List<String> findAllSymbols();

    @Query("SELECT MIN(c.price) FROM CryptoEntity c WHERE c.id.symbol = :symbol")
    BigDecimal findMinPriceBySymbol(@Param("symbol") String symbol);

    @Query("SELECT MAX(c.price) FROM CryptoEntity c WHERE c.id.symbol = :symbol")
    BigDecimal findMaxPriceBySymbol(@Param("symbol") String symbol);

    @Query("""
            SELECT MIN(c.price)
            FROM CryptoEntity c
            WHERE c.id.symbol = :symbol
              AND c.id.timestamp >= :startMillis
              AND c.id.timestamp < :endMillis
            """)
    BigDecimal findMinPriceBySymbolAndTimeRange(
            @Param("symbol") String symbol,
            @Param("startMillis") long startMillis,
            @Param("endMillis") long endMillis
    );

    @Query("""
            SELECT MAX(c.price)
            FROM CryptoEntity c
            WHERE c.id.symbol = :symbol
              AND c.id.timestamp >= :startMillis
              AND c.id.timestamp < :endMillis
            """)
    BigDecimal findMaxPriceBySymbolAndTimeRange(
            @Param("symbol") String symbol,
            @Param("startMillis") long startMillis,
            @Param("endMillis") long endMillis
    );
}
