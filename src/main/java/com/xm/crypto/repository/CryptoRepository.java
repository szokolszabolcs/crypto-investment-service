package com.xm.crypto.repository;

import com.xm.crypto.model.entity.CryptoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CryptoRepository extends JpaRepository<CryptoEntity, Long> {
}
