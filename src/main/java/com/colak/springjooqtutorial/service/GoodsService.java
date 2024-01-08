package com.colak.springjooqtutorial.service;

import com.colak.springjooqtutorial.dto.GoodsDto;
import org.jooq.generated.public_.tables.Goods;

import java.util.UUID;

public interface GoodsService {

    GoodsDto create(GoodsDto goodsDto);

    GoodsDto update(GoodsDto goodsDto);

    GoodsDto getById(UUID id);

    void delete(UUID id);
}
