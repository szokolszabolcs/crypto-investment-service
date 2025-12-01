package com.xm.crypto.service;

import com.xm.crypto.model.dto.NormalizedCryptoDto;

import java.time.LocalDate;
import java.util.List;

public interface NormalizedRangeService {

    List<NormalizedCryptoDto> listByNormalizedRange();

    NormalizedCryptoDto getHighestNormalizedRange(LocalDate date);
}
