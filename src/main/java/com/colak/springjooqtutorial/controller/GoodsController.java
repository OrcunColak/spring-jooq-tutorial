package com.colak.springjooqtutorial.controller;

import com.colak.springjooqtutorial.dto.GoodsDto;
import com.colak.springjooqtutorial.service.GoodsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class GoodsController {

    private final GoodsService goodsService;

    @PostMapping("/create")
    public GoodsDto create(@RequestBody GoodsDto goodsDto) {
        return goodsService.create(goodsDto);
    }

    @GetMapping("/{id}")
    public GoodsDto getById(@PathVariable UUID id) {
        return goodsService.getById(id);
    }

    @PutMapping("/update")
    public GoodsDto update(@RequestBody GoodsDto goodsDto) {
        return goodsService.update(goodsDto);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable UUID id) {
        goodsService.delete(id);
    }

}
