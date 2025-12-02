package com.xm.crypto.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "crypto_values")
public class CryptoEntity {

    @EmbeddedId
    private CryptoKey id;

    @Column(nullable = false)
    private BigDecimal price;

    public CryptoEntity() {
    }

    public CryptoEntity(long timestamp, String symbol, BigDecimal price) {
        this.id = new CryptoKey(timestamp, symbol);
        this.price = price;
    }

    public CryptoKey getId() {
        return id;
    }

    public void setId(CryptoKey id) {
        this.id = id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
