package com.colak.springjooqtutorial.service.impl;

import com.colak.springjooqtutorial.dto.GoodsDto;
import com.colak.springjooqtutorial.service.GoodsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.jooq.generated.public_.tables.Goods;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class GoodsServiceImpl implements GoodsService {

    private final DSLContext dslContext;

    @Override
    public GoodsDto create(GoodsDto goodsDto) {
        var id = UUID.randomUUID();
        var result = dslContext.insertInto(Goods.GOODS)
                .values(id,
                        goodsDto.name(),
                        goodsDto.price(),
                        goodsDto.totalCount(),
                        goodsDto.soldCount(),
                        goodsDto.deleted())
                .execute();
        log.info("Inserted with result: {}", result);
        return getById(id);
    }

    @Override
    public GoodsDto update(GoodsDto goodsDto) {
        var updated = dslContext.update(Goods.GOODS)
                .set(Goods.GOODS.ID, goodsDto.id())
                .set(Goods.GOODS.NAME, goodsDto.name())
                .set(Goods.GOODS.PRICE, goodsDto.price())
                .set(Goods.GOODS.SOLD_COUNT, goodsDto.soldCount())
                .set(Goods.GOODS.TOTAL_COUNT, goodsDto.totalCount())
                .set(Goods.GOODS.DELETED, goodsDto.deleted())
                .execute();
        log.info("Successfully updated {} rows", updated);
        return this.getById(goodsDto.id());
    }

    @Override
    public GoodsDto getById(UUID id) {
        final var fetchedRecord = dslContext.select(
                        Goods.GOODS.ID,
                        Goods.GOODS.NAME,
                        Goods.GOODS.PRICE,
                        Goods.GOODS.SOLD_COUNT,
                        Goods.GOODS.TOTAL_COUNT,
                        Goods.GOODS.DELETED
                )
                .from(Goods.GOODS)
                .where(Goods.GOODS.ID.eq(id))
                .fetchOne();
        Assert.notNull(fetchedRecord, "Record with id = " + id + " is not exists");
        return new GoodsDto(
                fetchedRecord.get(Goods.GOODS.ID),
                fetchedRecord.get(Goods.GOODS.NAME),
                fetchedRecord.get(Goods.GOODS.PRICE),
                fetchedRecord.get(Goods.GOODS.SOLD_COUNT),
                fetchedRecord.get(Goods.GOODS.TOTAL_COUNT),
                fetchedRecord.get(Goods.GOODS.DELETED)
        );
    }

    @Override
    public void delete(UUID id) {
        dslContext.update(Goods.GOODS).
                set(Goods.GOODS.DELETED, true)
                .where(Goods.GOODS.ID.eq(id))
                .execute();

        log.info("Successfully deleted the good with id = [" + id + "]");
    }
}
