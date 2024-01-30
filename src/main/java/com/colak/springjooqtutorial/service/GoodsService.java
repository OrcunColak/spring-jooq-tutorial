package com.colak.springjooqtutorial.service;

import com.colak.springjooqtutorial.dto.GoodsDto;

import java.util.List;
import java.util.UUID;

public interface GoodsService {

    GoodsDto create(GoodsDto goodsDto);

    List<GoodsDto> findAll();

    GoodsDto update(GoodsDto goodsDto);

    GoodsDto getById(UUID id);

    void delete(UUID id);
}
