package com.xm.crypto.model.entity;

import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class CryptoKey {
    private long timestamp;
    private String symbol;

    public CryptoKey() {
    }

    public CryptoKey(long timestamp, String symbol) {
        this.timestamp = timestamp;
        this.symbol = symbol;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CryptoKey that)) return false;
        return timestamp == that.timestamp && Objects.equals(symbol, that.symbol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timestamp, symbol);
    }
}
