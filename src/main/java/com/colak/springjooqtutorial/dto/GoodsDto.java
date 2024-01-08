package com.colak.springjooqtutorial.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record GoodsDto(
        UUID id,
        String name,
        BigDecimal price,
        Integer totalCount,
        Integer soldCount,
        boolean deleted
) {
}
